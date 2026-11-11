import React, { useCallback, useEffect, useState } from "react";
import { FlatList, RefreshControl, Text, TouchableOpacity, View } from "react-native";
import { useFocusEffect } from "@react-navigation/native";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { HomeStackProps } from "../navigation/HomeStackNavigation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { useInbox } from "../contexts/InboxContextProvider";
import { useToast } from "../contexts/FeedbackContextProvider";
import { getConversations } from "../repositories/ChatRepository";
import { Conversation } from "../utils/Chat";
import { displayName } from "../utils/Notification";
import { errorMessage } from "../utils/graphql";
import { timeAgo } from "../utils/time";
import { translations } from "../../translations/translation";
import { colors, palette } from "../styles/theme";
import { bgColor, mutedTextColor, textColor } from "../utils/Utils";
import ProfileNavigation from "../components/ProfileNavigation";
import Avatar from "../components/Avatar";
import EmptyState from "../components/EmptyState";
import Skeleton from "../components/Skeleton";

type Props = NativeStackScreenProps<HomeStackProps, "Conversations">;

/** The user's private chats, most recent first, updated live. */
const ConversationsScreen = ({ navigation }: Props) => {
  const { darkmode, language } = useSettingsContext();
  const { onEvent, refreshCounts } = useInbox();
  const toast = useToast();
  const t = translations[language || "en-EN"].screens.Inbox;
  const c = colors(darkmode);
  const accent = darkmode ? palette.accentOnDark : palette.accent;
  const [chats, setChats] = useState<Conversation[]>([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [typing, setTyping] = useState<Record<string, number>>({});

  const load = async () => {
    try {
      setChats(await getConversations());
      refreshCounts();
    } catch (error) {
      toast.error(errorMessage(error));
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };

  useFocusEffect(
    useCallback(() => {
      load();
    }, [])
  );

  useEffect(
    () =>
      onEvent((event) => {
        if (event.kind === "MESSAGE" && event.message) {
          const message = event.message;
          setTyping((prev) => ({ ...prev, [message.conversationId]: 0 }));
          setChats((prev) => {
            const existing = prev.find((chat) => chat.id === message.conversationId);
            if (!existing) {
              // A new chat: fetch it to know who it is with.
              load();
              return prev;
            }
            const updated: Conversation = {
              ...existing,
              lastMessage: message,
              updatedAt: message.createdAt,
              unread: message.mine ? existing.unread : existing.unread + 1,
            };
            return [updated, ...prev.filter((chat) => chat.id !== message.conversationId)];
          });
        }
        if (event.kind === "TYPING" && event.conversationId) {
          const id = event.conversationId;
          const until = Date.now() + 4000;
          setTyping((prev) => ({ ...prev, [id]: until }));
          setTimeout(() => setTyping((prev) => (prev[id] === until ? { ...prev, [id]: 0 } : prev)), 4100);
        }
      }),
    []
  );

  const renderItem = ({ item }: { item: Conversation }) => {
    const name = displayName(item.other);
    const unread = item.unread > 0;
    const isTyping = (typing[item.id] ?? 0) > Date.now();
    const last = item.lastMessage;
    const preview = last ? `${last.mine ? `${t.you}: ` : ""}${last.text}` : "";
    return (
      <TouchableOpacity
        activeOpacity={0.7}
        onPress={() =>
          navigation.navigate("Chat", { conversationId: item.id, email: item.other.email, name })
        }
        className="flex-row items-center px-5 py-3"
      >
        <Avatar image={item.other.image} name={name} size={52} />
        <View className="flex-1 ml-3">
          <View className="flex-row items-center">
            <Text
              numberOfLines={1}
              className={`flex-1 text-base ${unread ? "font-semibold" : "font-medium"} ${textColor(darkmode)}`}
            >
              {name}
            </Text>
            <Text style={{ color: unread ? accent : c.textMuted }} className="text-xs ml-2">
              {timeAgo(last?.createdAt ?? item.updatedAt, t.time)}
            </Text>
          </View>
          <View className="flex-row items-center mt-0.5">
            <Text
              numberOfLines={1}
              style={{ color: isTyping ? accent : unread ? c.text : c.textMuted }}
              className={`flex-1 text-sm ${unread ? "font-medium" : ""}`}
            >
              {isTyping ? t.typing : preview}
            </Text>
            {unread && (
              <View
                style={{ backgroundColor: palette.accent, minWidth: 20 }}
                className="h-5 px-1.5 ml-2 rounded-full items-center justify-center"
              >
                <Text className="text-[11px] font-bold text-white">{item.unread > 99 ? "99+" : item.unread}</Text>
              </View>
            )}
          </View>
        </View>
      </TouchableOpacity>
    );
  };

  return (
    <View className={`flex-1 ${bgColor(darkmode)}`}>
      <ProfileNavigation navigation={navigation} message={t.messagesTitle} />
      {loading ? (
        <View className="px-5 pt-4">
          {[0, 1, 2, 3, 4, 5].map((i) => (
            <View key={i} className="flex-row items-center mb-5">
              <Skeleton width={52} height={52} radius={26} />
              <View className="flex-1 ml-3">
                <Skeleton width="45%" height={13} />
                <Skeleton width="75%" height={11} style={{ marginTop: 8 }} />
              </View>
            </View>
          ))}
        </View>
      ) : (
        <FlatList
          data={chats}
          keyExtractor={(chat) => chat.id}
          renderItem={renderItem}
          extraData={typing}
          contentContainerStyle={{ flexGrow: 1, paddingVertical: 8 }}
          refreshControl={
            <RefreshControl
              refreshing={refreshing}
              onRefresh={() => {
                setRefreshing(true);
                load();
              }}
            />
          }
          ListEmptyComponent={
            <EmptyState
              icon="chatbubbles-outline"
              title={t.emptyMessages}
              hint={t.emptyMessagesHint}
              actionLabel={t.findPeople}
              onAction={() => navigation.navigate("Search")}
            />
          }
        />
      )}
    </View>
  );
};

export default ConversationsScreen;
