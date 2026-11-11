import React, { createContext, useCallback, useContext, useEffect, useMemo, useRef, useState } from "react";
import { Animated, AppState, Easing, Pressable, Text, View } from "react-native";
import Icon from "react-native-vector-icons/Ionicons";
import { useSettingsContext } from "./SettingsContextProvider";
import { getUnreadCounts } from "../repositories/NotificationRepository";
import { getConversation } from "../repositories/ChatRepository";
import { realtime, UserEvent } from "../utils/realtime";
import { onForegroundPush, onPushOpened, PushMessage, setUpPush } from "../utils/push";
import { navigationRef } from "../utils/session";
import { displayName } from "../utils/Notification";
import Avatar from "../components/Avatar";
import { colors, palette } from "../styles/theme";

type Banner = {
  id: number;
  title: string;
  body: string;
  image?: string | null;
  icon?: string;
  onPress?: () => void;
};

type InboxContextType = {
  unreadNotifications: number;
  unreadMessages: number;
  refreshCounts: () => void;
  setUnreadNotifications: (n: number) => void;
  /** The chat on screen: its messages don't raise banners and are read at once. */
  setActiveConversation: (id: string | null) => void;
  /** Live events (messages, typing, read receipts) for the open screens. */
  onEvent: (listener: (event: UserEvent) => void) => () => void;
};

const InboxContext = createContext<InboxContextType>({
  unreadNotifications: 0,
  unreadMessages: 0,
  refreshCounts: () => {},
  setUnreadNotifications: () => {},
  setActiveConversation: () => {},
  onEvent: () => () => {},
});

export const useInbox = () => useContext(InboxContext);

const BANNER_MS = 4000;

/** Opens the private chat with a user, from any tab. */
export const openChat = (email: string, name?: string) => {
  if (!navigationRef.isReady()) return;
  navigationRef.navigate("MainTabNavigation", {
    screen: "Home",
    params: { screen: "Chat", params: { email, name } },
  });
};

/** Opens the screen a push or banner is about. */
export const openFromPush = (data: Record<string, any> | undefined) => {
  if (!data || !navigationRef.isReady()) return;
  const home = (screen: string, params?: object) =>
    navigationRef.navigate("MainTabNavigation", { screen: "Home", params: { screen, params } });
  switch (data.type) {
    case "MESSAGE":
      home("Chat", { conversationId: data.conversationId, email: data.email, name: data.name });
      break;
    case "COMMENT":
      home("Comments", { postID: data.targetId });
      break;
    case "STREAK_RISK":
      navigationRef.navigate("MainTabNavigation", { screen: "UploadForm" });
      break;
    default:
      home("Notifications");
  }
};

/**
 * Everything that reaches the user while signed in: the live connection, push
 * registration, unread badges and the banner shown for news while the app is
 * open. Mounted with the main tabs, so it only runs with a session.
 */
const InboxContextProvider = ({ children }: { children: React.ReactNode }) => {
  const { language, darkmode } = useSettingsContext();
  const [unreadNotifications, setUnreadNotifications] = useState(0);
  const [unreadMessages, setUnreadMessages] = useState(0);
  const [banner, setBanner] = useState<Banner | null>(null);
  const active = useRef<string | null>(null);
  const slide = useRef(new Animated.Value(0)).current;
  const hideTimer = useRef<ReturnType<typeof setTimeout> | null>(null);

  const refreshCounts = useCallback(() => {
    getUnreadCounts()
      .then((c) => {
        setUnreadNotifications(c.notifications);
        setUnreadMessages(c.messages);
      })
      .catch(() => {});
  }, []);

  const hide = useCallback(() => {
    if (hideTimer.current) clearTimeout(hideTimer.current);
    Animated.timing(slide, { toValue: 0, duration: 180, useNativeDriver: true }).start(() => setBanner(null));
  }, [slide]);

  const show = useCallback(
    (next: Omit<Banner, "id">) => {
      if (hideTimer.current) clearTimeout(hideTimer.current);
      setBanner({ ...next, id: Date.now() });
      slide.setValue(0);
      Animated.timing(slide, {
        toValue: 1,
        duration: 260,
        easing: Easing.out(Easing.cubic),
        useNativeDriver: true,
      }).start();
      hideTimer.current = setTimeout(hide, BANNER_MS);
    },
    [slide, hide]
  );

  // Live connection and badges.
  useEffect(() => {
    realtime.start();
    refreshCounts();
    const offEvents = realtime.onEvent(async (event) => {
      if (event.kind === "NOTIFICATION" && event.unreadNotifications != null) {
        setUnreadNotifications(event.unreadNotifications);
      }
      if (event.kind === "MESSAGE" && event.message) {
        const inChat = event.conversationId === active.current;
        if (event.unreadMessages != null && !inChat) setUnreadMessages(event.unreadMessages);
        if (!event.message.mine && !inChat) {
          const chat = await getConversation(event.message.conversationId).catch(() => null);
          const name = displayName(chat?.other) || "";
          show({
            title: name,
            body: event.message.text,
            image: chat?.other?.image,
            onPress: () =>
              openFromPush({
                type: "MESSAGE",
                conversationId: event.message!.conversationId,
                email: chat?.other?.email,
                name,
              }),
          });
        }
      }
    });
    // Reconnecting after a while away: catch up on what was missed.
    const offStatus = realtime.onStatus((connected) => connected && refreshCounts());
    const appState = AppState.addEventListener("change", (state) => {
      if (state === "active") {
        realtime.wake();
        refreshCounts();
      }
    });
    return () => {
      offEvents();
      offStatus();
      appState.remove();
      realtime.stop();
    };
  }, []);

  // Push: registration follows the app language (texts are translated by the API).
  useEffect(() => {
    let stopRefresh: () => void = () => {};
    setUpPush(language || "en-EN").then((stop) => (stopRefresh = stop));
    return () => stopRefresh();
  }, [language]);

  useEffect(() => {
    const offForeground = onForegroundPush((message: PushMessage) => {
      const data = message.data as Record<string, any> | undefined;
      // Messages already arrive through the live connection.
      if (data?.type === "MESSAGE" && (realtime.isConnected() || data.conversationId === active.current)) return;
      refreshCounts();
      show({
        title: message.notification?.title ?? "",
        body: message.notification?.body ?? "",
        icon: data?.type === "STREAK_RISK" ? "flame" : "notifications",
        onPress: () => openFromPush(data),
      });
    });
    const offOpened = onPushOpened((message) => openFromPush(message.data as Record<string, any>));
    return () => {
      offForeground();
      offOpened();
    };
  }, [show]);

  const value = useMemo<InboxContextType>(
    () => ({
      unreadNotifications,
      unreadMessages,
      refreshCounts,
      setUnreadNotifications,
      setActiveConversation: (id) => {
        active.current = id;
      },
      onEvent: (listener) => realtime.onEvent(listener),
    }),
    [unreadNotifications, unreadMessages, refreshCounts]
  );

  const c = colors(darkmode);

  return (
    <InboxContext.Provider value={value}>
      {children}
      {banner && (
        <Animated.View
          pointerEvents="box-none"
          style={{
            position: "absolute",
            // The app root already sits below the status bar (SafeTop).
            top: 8,
            left: 12,
            right: 12,
            opacity: slide,
            transform: [{ translateY: slide.interpolate({ inputRange: [0, 1], outputRange: [-24, 0] }) }],
          }}
        >
          <Pressable
            onPress={() => {
              hide();
              banner.onPress?.();
            }}
            accessibilityRole="button"
            style={{
              flexDirection: "row",
              alignItems: "center",
              padding: 12,
              borderRadius: 16,
              backgroundColor: c.card,
              borderWidth: 1,
              borderColor: c.hairline,
              shadowColor: "#000",
              shadowOpacity: 0.18,
              shadowRadius: 16,
              shadowOffset: { width: 0, height: 6 },
              elevation: 8,
            }}
          >
            {banner.icon ? (
              <View
                style={{ width: 36, height: 36, borderRadius: 18, backgroundColor: c.raised }}
                className="items-center justify-center"
              >
                <Icon name={banner.icon} size={18} color={darkmode ? palette.accentOnDark : palette.accent} />
              </View>
            ) : (
              <Avatar image={banner.image} name={banner.title} size={36} />
            )}
            <View className="flex-1 ml-3">
              {!!banner.title && (
                <Text numberOfLines={1} style={{ color: c.text }} className="text-sm font-semibold">
                  {banner.title}
                </Text>
              )}
              <Text numberOfLines={2} style={{ color: c.textSecondary }} className="text-sm">
                {banner.body}
              </Text>
            </View>
          </Pressable>
        </Animated.View>
      )}
    </InboxContext.Provider>
  );
};

export default InboxContextProvider;
