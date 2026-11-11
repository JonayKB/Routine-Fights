import React, { useCallback, useEffect, useRef, useState } from "react";
import { FlatList, RefreshControl, Text, TouchableOpacity, View } from "react-native";
import { useFocusEffect } from "@react-navigation/native";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { HomeStackProps } from "../navigation/HomeStackNavigation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { useInbox } from "../contexts/InboxContextProvider";
import { useToast } from "../contexts/FeedbackContextProvider";
import { useTokenContext } from "../contexts/TokenContextProvider";
import { getNotifications, markNotificationsRead } from "../repositories/NotificationRepository";
import { AppNotification, displayName } from "../utils/Notification";
import { errorMessage } from "../utils/graphql";
import { timeAgo } from "../utils/time";
import { translations } from "../../translations/translation";
import { colors } from "../styles/theme";
import { bgColor, mutedTextColor, textColor } from "../utils/Utils";
import ProfileNavigation from "../components/ProfileNavigation";
import Avatar from "../components/Avatar";
import ProgressiveImage from "../components/ProgressiveImage";
import EmptyState from "../components/EmptyState";
import Skeleton from "../components/Skeleton";
import StreakFlame from "../components/StreakFlame";

type Props = NativeStackScreenProps<HomeStackProps, "Notifications">;

const PAGE = 20;

/** "{actor} liked…" with the name in bold. */
const RichText = ({ template, actor, values, color, strong }: {
  template: string;
  actor: string;
  values: Record<string, string | number>;
  color: string;
  strong: string;
}) => {
  const filled = template.replace(/\{(\w+)\}/g, (match, key) =>
    key === "actor" ? match : String(values[key] ?? "")
  );
  const [before, after = ""] = filled.split("{actor}");
  return (
    <Text style={{ color }} className="text-sm leading-5" numberOfLines={3}>
      {before}
      {filled.includes("{actor}") && <Text style={{ color: strong }} className="font-semibold">{actor}</Text>}
      {after}
    </Text>
  );
};

const NotificationsScreen = ({ navigation }: Props) => {
  const { darkmode, language } = useSettingsContext();
  const { setUnreadNotifications } = useInbox();
  const toast = useToast();
  const { email } = useTokenContext();
  const t = translations[language || "en-EN"].screens.Inbox;
  const c = colors(darkmode);
  const [items, setItems] = useState<AppNotification[]>([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const page = useRef(0);
  const done = useRef(false);
  const loadingMore = useRef(false);

  const load = async (reset: boolean) => {
    if (!reset && (done.current || loadingMore.current)) return;
    loadingMore.current = true;
    const next = reset ? 0 : page.current + 1;
    try {
      const result = await getNotifications(next, PAGE);
      page.current = next;
      done.current = result.length < PAGE;
      setItems((prev) => (reset ? result : [...prev, ...result.filter((n) => !prev.some((p) => p.id === n.id))]));
      if (reset && result.some((n) => !n.read)) {
        // Seen now; they keep the "new" look until the next visit.
        markNotificationsRead().catch(() => {});
        setUnreadNotifications(0);
      }
    } catch (error) {
      toast.error(errorMessage(error));
    } finally {
      loadingMore.current = false;
      setLoading(false);
      setRefreshing(false);
    }
  };

  useFocusEffect(
    useCallback(() => {
      load(true);
    }, [])
  );

  useEffect(() => {
    setUnreadNotifications(0);
  }, []);

  const open = (n: AppNotification) => {
    switch (n.type) {
      case "LIKE":
      case "COMMENT":
        if (n.targetId) navigation.navigate("Comments", { postID: n.targetId });
        break;
      case "FOLLOW":
        if (n.actorCount === 1 && n.actors[0]) {
          navigation.navigate("ProfileStackNavigation", { screen: "Profile", params: { email: n.actors[0].email } });
        } else {
          navigation.navigate("ProfileStackNavigation", {
            screen: "FollowList",
            params: { email, type: "followers" } as any,
          });
        }
        break;
      case "STREAK_RISK":
        (navigation.getParent() as any)?.navigate("UploadForm");
        break;
    }
  };

  const textFor = (n: AppNotification) => {
    const many = n.actorCount > 1;
    switch (n.type) {
      case "LIKE":
        return many ? t.likeMany : t.likeOne;
      case "COMMENT":
        return many ? t.commentMany : t.commentOne;
      case "FOLLOW":
        return many ? t.followMany : t.followOne;
      default:
        return t.streakRisk;
    }
  };

  const Visual = ({ n }: { n: AppNotification }) => {
    if (n.type === "STREAK_RISK") {
      return (
        <View style={{ width: 44, height: 44 }} className="items-center justify-center">
          <StreakFlame streak={Number(n.extra ?? 1)} size={30} glow={false} />
        </View>
      );
    }
    const [first, second] = n.actors;
    if (!second) return <Avatar image={first?.image} name={displayName(first)} size={44} />;
    // Two faces overlapping say "several people" at a glance.
    return (
      <View style={{ width: 44, height: 44 }}>
        <View style={{ position: "absolute", top: 0, right: 0 }}>
          <Avatar image={second.image} name={displayName(second)} size={30} />
        </View>
        <View
          style={{ position: "absolute", bottom: 0, left: 0, borderRadius: 18, borderWidth: 2, borderColor: c.page }}
        >
          <Avatar image={first?.image} name={displayName(first)} size={30} />
        </View>
      </View>
    );
  };

  const renderItem = ({ item, index }: { item: AppNotification; index: number }) => {
    const header =
      index === 0 || items[index - 1].read !== item.read ? (
        <Text className={`px-5 pt-5 pb-2 text-sm font-semibold ${textColor(darkmode)}`}>
          {item.read ? t.earlier : t.new}
        </Text>
      ) : null;
    return (
      <>
        {header}
        <TouchableOpacity
          activeOpacity={0.7}
          onPress={() => open(item)}
          className="flex-row items-center px-5 py-3"
          style={!item.read ? { backgroundColor: darkmode ? "#FB923C12" : "#C2410C0D" } : undefined}
        >
          <Visual n={item} />
          <View className="flex-1 ml-3 mr-2">
            <RichText
              template={textFor(item)}
              actor={displayName(item.actors[0])}
              values={{
                others: Math.max(0, item.actorCount - 1),
                preview: item.preview ?? "",
                activity: item.preview ?? "",
                streak: item.extra ?? "",
              }}
              color={c.textSecondary}
              strong={c.text}
            />
            <Text className={`text-xs mt-1 ${mutedTextColor(darkmode)}`}>{timeAgo(item.updatedAt, t.time)}</Text>
          </View>
          {item.type === "STREAK_RISK" ? (
            <View className="px-3 h-8 rounded-full items-center justify-center bg-[#C2410C]">
              <Text className="text-xs font-semibold text-white">{t.postNow}</Text>
            </View>
          ) : item.targetImage ? (
            <View style={{ borderRadius: 8, overflow: "hidden" }}>
              <ProgressiveImage image={item.targetImage} width={44} height={55} />
            </View>
          ) : null}
        </TouchableOpacity>
      </>
    );
  };

  return (
    <View className={`flex-1 ${bgColor(darkmode)}`}>
      <ProfileNavigation navigation={navigation} message={t.notificationsTitle} />
      {loading ? (
        <View className="px-5 pt-5">
          {[0, 1, 2, 3, 4].map((i) => (
            <View key={i} className="flex-row items-center mb-5">
              <Skeleton width={44} height={44} radius={22} />
              <View className="flex-1 ml-3">
                <Skeleton width="85%" height={12} />
                <Skeleton width="30%" height={10} style={{ marginTop: 8 }} />
              </View>
              <Skeleton width={44} height={55} radius={8} style={{ marginLeft: 8 }} />
            </View>
          ))}
        </View>
      ) : (
        <FlatList
          data={items}
          keyExtractor={(n) => n.id}
          renderItem={renderItem}
          contentContainerStyle={{ flexGrow: 1, paddingBottom: 24 }}
          refreshControl={
            <RefreshControl
              refreshing={refreshing}
              onRefresh={() => {
                setRefreshing(true);
                load(true);
              }}
            />
          }
          onEndReached={() => load(false)}
          onEndReachedThreshold={0.4}
          ListEmptyComponent={
            <EmptyState
              icon="notifications-outline"
              title={t.emptyNotifications}
              hint={t.emptyNotificationsHint}
            />
          }
        />
      )}
    </View>
  );
};

export default NotificationsScreen;
