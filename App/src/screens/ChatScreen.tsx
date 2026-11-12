import React, { useCallback, useEffect, useMemo, useRef, useState } from "react";
import {
  ActivityIndicator,
  FlatList,
  KeyboardAvoidingView,
  Platform,
  Pressable,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from "react-native";
import { useFocusEffect } from "@react-navigation/native";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { useSafeAreaInsets } from "react-native-safe-area-context";
import Icon from "react-native-vector-icons/Ionicons";
import { HomeStackProps } from "../navigation/HomeStackNavigation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { useInbox } from "../contexts/InboxContextProvider";
import { useToast } from "../contexts/FeedbackContextProvider";
import {
  getConversation,
  getConversationWith,
  getMessages,
  markConversationRead,
  sendMessage,
  sendTyping,
} from "../repositories/ChatRepository";
import { ChatMessage, Conversation, MESSAGE_MAX_LENGTH } from "../utils/Chat";
import { displayName } from "../utils/Notification";
import { errorMessage } from "../utils/graphql";
import { clockTime, dayLabel, isSameDay, parseApiDate } from "../utils/time";
import { formatString } from "../components/Streak";
import { translations } from "../../translations/translation";
import { colors, palette } from "../styles/theme";
import { bgColor, borderColor, iconColor } from "../utils/Utils";
import Avatar from "../components/Avatar";

type Props = NativeStackScreenProps<HomeStackProps, "Chat">;

const PAGE = 40;
const TYPING_EVERY_MS = 3000;
const TYPING_SHOWN_MS = 4000;

const ChatScreen = ({ navigation, route }: Props) => {
  const { email, name: initialName } = route.params;
  const { darkmode, language } = useSettingsContext();
  const { onEvent, setActiveConversation, refreshCounts } = useInbox();
  const toast = useToast();
  const insets = useSafeAreaInsets();
  const t = translations[language || "en-EN"].screens.Inbox;
  const c = colors(darkmode);

  const [conversationId, setConversationId] = useState<string | null>(route.params.conversationId ?? null);
  const [chat, setChat] = useState<Conversation | null>(null);
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [loading, setLoading] = useState(true);
  const [loadingOlder, setLoadingOlder] = useState(false);
  const [text, setText] = useState("");
  // Read on send: state would still hold the text on a quick second tap.
  const textRef = useRef("");
  const [otherTyping, setOtherTyping] = useState(false);
  const allLoaded = useRef(false);
  const lastTypingSent = useRef(0);
  const typingTimer = useRef<ReturnType<typeof setTimeout> | null>(null);
  const idRef = useRef(conversationId);
  idRef.current = conversationId;

  const name = displayName(chat?.other) || initialName || email?.split("@")[0] || "";

  // The tab bar would sit under the composer.
  useFocusEffect(
    useCallback(() => {
      const tabs = navigation.getParent() as any;
      tabs?.setOptions({ tabBarStyle: { display: "none" } });
      return () => tabs?.setOptions({ tabBarStyle: undefined });
    }, [navigation])
  );

  const markRead = useCallback(() => {
    const id = idRef.current;
    if (!id) return;
    markConversationRead(id)
      .then(refreshCounts)
      .catch(() => {});
  }, [refreshCounts]);

  // Find the chat (it may not exist yet) and its latest messages.
  useEffect(() => {
    const open = async () => {
      try {
        const found = conversationId ? await getConversation(conversationId) : await getConversationWith(email);
        setChat(found);
        if (found) {
          setConversationId(found.id);
          idRef.current = found.id;
          const page = await getMessages(found.id, undefined, PAGE);
          allLoaded.current = page.length < PAGE;
          setMessages(page);
          if (found.unread > 0) markRead();
        } else {
          allLoaded.current = true;
        }
      } catch (error) {
        toast.error(errorMessage(error));
      } finally {
        setLoading(false);
      }
    };
    open();
  }, []);

  useFocusEffect(
    useCallback(() => {
      setActiveConversation(conversationId);
      return () => setActiveConversation(null);
    }, [conversationId])
  );

  // Live: new messages, typing and read receipts of this chat.
  useEffect(
    () =>
      onEvent((event) => {
        if (!idRef.current || event.conversationId !== idRef.current) return;
        if (event.kind === "MESSAGE" && event.message) {
          const incoming = event.message;
          setMessages((prev) => {
            if (prev.some((m) => m.id === incoming.id)) return prev;
            // Our own message from another device, or the echo of one being sent here.
            const pending = incoming.mine ? prev.findIndex((m) => m.pending && m.text === incoming.text) : -1;
            if (pending >= 0) {
              const copy = [...prev];
              copy[pending] = incoming;
              return copy;
            }
            return [incoming, ...prev];
          });
          if (!incoming.mine) {
            setOtherTyping(false);
            markRead();
          }
        }
        if (event.kind === "TYPING") {
          setOtherTyping(true);
          if (typingTimer.current) clearTimeout(typingTimer.current);
          typingTimer.current = setTimeout(() => setOtherTyping(false), TYPING_SHOWN_MS);
        }
        if (event.kind === "READ" && event.readAt) {
          setChat((prev) => (prev ? { ...prev, otherReadAt: event.readAt } : prev));
        }
      }),
    []
  );

  useEffect(() => () => {
    if (typingTimer.current) clearTimeout(typingTimer.current);
  }, []);

  const loadOlder = async () => {
    const id = idRef.current;
    const oldest = [...messages].reverse().find((m) => !m.pending);
    if (!id || !oldest || allLoaded.current || loadingOlder) return;
    setLoadingOlder(true);
    try {
      const page = await getMessages(id, oldest.createdAt, PAGE);
      allLoaded.current = page.length < PAGE;
      setMessages((prev) => [...prev, ...page.filter((m) => !prev.some((p) => p.id === m.id))]);
    } catch (error) {
      toast.error(errorMessage(error));
    } finally {
      setLoadingOlder(false);
    }
  };

  const deliver = async (draft: ChatMessage) => {
    try {
      const saved = await sendMessage(draft.text, { conversationId: idRef.current, toEmail: email });
      if (!idRef.current) {
        setConversationId(saved.conversationId);
        idRef.current = saved.conversationId;
        setActiveConversation(saved.conversationId);
      }
      setMessages((prev) => {
        // The live echo may have arrived first.
        if (prev.some((m) => m.id === saved.id)) return prev.filter((m) => m.id !== draft.id);
        return prev.map((m) => (m.id === draft.id ? saved : m));
      });
    } catch (error) {
      setMessages((prev) => prev.map((m) => (m.id === draft.id ? { ...m, pending: false, failed: true } : m)));
      toast.error(errorMessage(error));
    }
  };

  const send = () => {
    const body = textRef.current.trim();
    if (!body) return;
    textRef.current = "";
    const draft: ChatMessage = {
      id: `local-${Date.now()}`,
      conversationId: idRef.current ?? "",
      text: body,
      senderEmail: "",
      createdAt: new Date().toISOString(),
      mine: true,
      pending: true,
    };
    setText("");
    lastTypingSent.current = 0;
    setMessages((prev) => [draft, ...prev]);
    deliver(draft);
  };

  const retry = (message: ChatMessage) => {
    const draft = { ...message, failed: false, pending: true };
    setMessages((prev) => prev.map((m) => (m.id === message.id ? draft : m)));
    deliver(draft);
  };

  const onChangeText = (value: string) => {
    textRef.current = value;
    setText(value);
    const id = idRef.current;
    const now = Date.now();
    if (id && value.trim() && now - lastTypingSent.current > TYPING_EVERY_MS) {
      lastTypingSent.current = now;
      sendTyping(id).catch(() => {});
    }
  };

  // The newest of my messages the other person has already read.
  const seenId = useMemo(() => {
    const readAt = parseApiDate(chat?.otherReadAt);
    if (!readAt) return null;
    const lastMine = messages.find((m) => m.mine && !m.pending && !m.failed);
    const sent = parseApiDate(lastMine?.createdAt);
    return lastMine && sent && sent <= readAt ? lastMine.id : null;
  }, [messages, chat?.otherReadAt]);

  const openProfile = () =>
    navigation.navigate("ProfileStackNavigation", { screen: "Profile", params: { email } });

  const renderItem = ({ item, index }: { item: ChatMessage; index: number }) => {
    // The list is inverted: index + 1 is the message shown above this one.
    const above = messages[index + 1];
    const below = messages[index - 1];
    const newDay = !above || !isSameDay(above.createdAt, item.createdAt);
    const groupedWithBelow = below && below.mine === item.mine && isSameDay(below.createdAt, item.createdAt);
    const groupedWithAbove = above && above.mine === item.mine && !newDay;
    const mine = item.mine;
    const radius = 20;
    const tight = 6;
    return (
      <View>
        {newDay && (
          <Text style={{ color: c.textMuted }} className="text-xs text-center my-4">
            {dayLabel(item.createdAt, t)}
          </Text>
        )}
        <View className={`flex-row px-3 ${mine ? "justify-end" : "justify-start"}`} style={{ marginTop: groupedWithAbove ? 2 : 8 }}>
          <Pressable
            disabled={!item.failed}
            onPress={() => retry(item)}
            style={{
              maxWidth: "78%",
              paddingHorizontal: 14,
              paddingVertical: 9,
              backgroundColor: mine ? palette.accent : c.raised,
              opacity: item.pending ? 0.6 : 1,
              borderTopLeftRadius: !mine && groupedWithAbove ? tight : radius,
              borderBottomLeftRadius: !mine && groupedWithBelow ? tight : radius,
              borderTopRightRadius: mine && groupedWithAbove ? tight : radius,
              borderBottomRightRadius: mine && groupedWithBelow ? tight : radius,
            }}
          >
            <Text selectable style={{ color: mine ? "#FFFFFF" : c.text }} className="text-[15px] leading-5">
              {item.text}
            </Text>
            <Text
              style={{ color: mine ? "#FFFFFFB3" : c.textMuted, alignSelf: "flex-end" }}
              className="text-[10px] mt-1"
            >
              {clockTime(item.pending ? undefined : item.createdAt)}
              {item.pending ? " ···" : ""}
            </Text>
          </Pressable>
        </View>
        {item.failed && (
          <Text style={{ color: palette.like }} className="text-xs text-right px-4 mt-1">
            {t.notSent}
          </Text>
        )}
        {item.id === seenId && (
          <Text style={{ color: c.textMuted }} className="text-xs text-right px-4 mt-1">
            {t.seen}
          </Text>
        )}
      </View>
    );
  };

  const canSend = text.trim().length > 0;

  return (
    <KeyboardAvoidingView
      className={`flex-1 ${bgColor(darkmode)}`}
      behavior={Platform.OS === "ios" ? "padding" : undefined}
    >
      {/* Header */}
      <View className={`flex-row items-center h-14 px-3 border-b ${borderColor(darkmode)}`}>
        <TouchableOpacity
          onPress={() => navigation.goBack()}
          hitSlop={10}
          accessibilityRole="button"
          className="w-10 h-10 items-center justify-center"
        >
          <Icon name="arrow-back" size={22} color={iconColor(darkmode)} />
        </TouchableOpacity>
        <TouchableOpacity onPress={openProfile} activeOpacity={0.7} className="flex-1 flex-row items-center ml-1">
          <Avatar image={chat?.other?.image} name={name} size={34} />
          <View className="ml-3 flex-1">
            <Text numberOfLines={1} style={{ color: c.text }} className="text-base font-semibold">
              {name}
            </Text>
            {otherTyping && (
              <Text style={{ color: darkmode ? palette.accentOnDark : palette.accent }} className="text-xs">
                {t.typing}
              </Text>
            )}
          </View>
        </TouchableOpacity>
      </View>

      {/* Messages */}
      {loading ? (
        <View className="flex-1 items-center justify-center">
          <ActivityIndicator color={darkmode ? palette.accentOnDark : palette.accent} />
        </View>
      ) : messages.length === 0 ? (
        <View className="flex-1 items-center justify-center px-10">
          <Avatar image={chat?.other?.image} name={name} size={72} />
          <Text style={{ color: c.text }} className="text-lg font-semibold mt-4 text-center">
            {formatString(t.sayHi, { name })}
          </Text>
          <Text style={{ color: c.textMuted }} className="text-sm mt-1 text-center">
            {t.sayHiHint}
          </Text>
        </View>
      ) : (
        <FlatList
          inverted
          data={messages}
          keyExtractor={(m) => m.id}
          renderItem={renderItem}
          extraData={seenId}
          onEndReached={loadOlder}
          onEndReachedThreshold={0.3}
          keyboardShouldPersistTaps="handled"
          contentContainerStyle={{ paddingVertical: 12 }}
          ListFooterComponent={
            loadingOlder ? <ActivityIndicator className="my-3" color={c.textMuted} /> : null
          }
        />
      )}

      {/* Composer */}
      <View
        className={`flex-row items-end px-3 pt-2 border-t ${borderColor(darkmode)}`}
        style={{ paddingBottom: Math.max(insets.bottom, 8), backgroundColor: c.page }}
      >
        <TextInput
          value={text}
          onChangeText={onChangeText}
          placeholder={t.placeholder}
          placeholderTextColor={c.textMuted}
          multiline
          maxLength={MESSAGE_MAX_LENGTH}
          accessibilityLabel={t.placeholder}
          style={{
            flex: 1,
            maxHeight: 120,
            minHeight: 42,
            paddingHorizontal: 16,
            paddingTop: Platform.OS === "ios" ? 11 : 8,
            paddingBottom: Platform.OS === "ios" ? 11 : 8,
            borderRadius: 21,
            backgroundColor: c.raised,
            color: c.text,
            fontSize: 15,
          }}
        />
        <TouchableOpacity
          onPress={send}
          disabled={!canSend}
          accessibilityRole="button"
          accessibilityLabel={t.send}
          activeOpacity={0.8}
          style={{
            width: 42,
            height: 42,
            borderRadius: 21,
            marginLeft: 8,
            alignItems: "center",
            justifyContent: "center",
            backgroundColor: canSend ? palette.accent : c.raised,
          }}
        >
          <Icon name="arrow-up" size={20} color={canSend ? "#FFFFFF" : c.textMuted} />
        </TouchableOpacity>
      </View>
    </KeyboardAvoidingView>
  );
};

export default ChatScreen;
