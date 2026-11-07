import { RefreshControl, ScrollView, Text, View } from "react-native";
import { useCallback, useEffect, useMemo, useState } from "react";
import { useFocusEffect } from "@react-navigation/native";
import { Badge } from "../utils/Badge";
import { useTokenContext } from "../contexts/TokenContextProvider";
import Avatar from "../components/Avatar";
import Skeleton from "../components/Skeleton";
import EmptyState from "../components/EmptyState";
import Icon from "react-native-vector-icons/Ionicons";
import { Event } from "../utils/Event";
import {
  RankingEntry,
  getCurrentPoints,
  getEventRanking,
  getNearestEvent,
} from "../repositories/EventRepository";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import {
  bgColor,
  borderColor,
  cardBgColor,
  mutedTextColor,
  raisedBgColor,
  secondaryTextColor,
  textColor,
} from "../utils/Utils";
import { fonts } from "../styles/theme";
import { translations } from "../../translations/translation";
import { getBadgesByEvent } from "../repositories/BadgeRepository";
import Picture from "../components/Picture";
import { errorMessage } from "../utils/graphql";
import { useToast } from "../contexts/FeedbackContextProvider";

type Props = {};

const EventsScreen = (props: Props) => {
  const { darkmode, language } = useSettingsContext();
  const toast = useToast();
  const [event, setEvent] = useState<Event>(null);
  const [timeLeft, setTimeLeft] = useState<number>(0);
  const [currentPoints, setCurrentPoints] = useState(0);
  const [load, setLoad] = useState(false);
  const [initialLoading, setInitialLoading] = useState(true);
  const secToMillis = 1000;
  const [badges, setBadges] = useState<Badge[]>([]);
  const [ranking, setRanking] = useState<RankingEntry[]>([]);
  const { email } = useTokenContext();

  // One milestone per badge, evenly spread up to the event goal.
  const milestones = useMemo(() => {
    if (!event?.totalRequired || badges.length === 0) return [];
    const step = event.totalRequired / badges.length;
    return badges.map((_, i) => Math.floor((i + 1) * step));
  }, [event?.totalRequired, badges]);

  const formatTime = (millis: number): string => {
    const totalSeconds = Math.floor(millis / 1000);
    const totalMinutes = Math.floor(totalSeconds / 60);

    const days = Math.floor(totalMinutes / 1440);
    const hours = Math.floor(totalMinutes / 60) % 24;
    const minutes = totalMinutes % 60;
    const seconds = totalSeconds % 60;
    if (days > 0) {
      return `${days}d ${Math.max(0, hours)}h ${Math.max(0, minutes)}m`;
    }
    return `${Math.max(0, hours)}h ${Math.max(0, minutes)}m ${Math.max(
      0,
      seconds
    )}s`;
  };

  // Everything is loaded from the event that was just fetched, not from state,
  // which still holds the previous value inside this function.
  const init = async () => {
    try {
      const nearest = await getNearestEvent();
      setEvent(nearest);
      if (nearest?.id) {
        const [points, eventBadges, top] = await Promise.all([
          getCurrentPoints(nearest.id),
          getBadgesByEvent(nearest.id),
          getEventRanking(nearest.id, 5),
        ]);
        setCurrentPoints(points ?? 0);
        setBadges(eventBadges);
        setRanking(top);
      }
    } catch (error) {
      toast.error(errorMessage(error));
    } finally {
      setLoad(false);
      setInitialLoading(false);
    }
  };

  useEffect(() => {
    init();
  }, []);

  useEffect(() => {
    if (load) init();
  }, [load]);

  useFocusEffect(
    useCallback(() => {
      if (!initialLoading) init();
    }, [initialLoading])
  );

  useEffect(() => {
    if (!event?.finishDate) return;

    const interval = setInterval(() => {
      const nowMillis = new Date().getTime();
      const endDateMillis = Date.parse(event.finishDate);
      const timeRemaining = endDateMillis - nowMillis;

      if (timeRemaining <= 0) {
        clearInterval(interval);
        setTimeLeft(0);
      } else {
        setTimeLeft(timeRemaining);
      }
    }, secToMillis);

    return () => clearInterval(interval);
  }, [event?.finishDate]);

  const t = translations[language || "en-EN"].screens.Events;
  const progress = event?.totalRequired
    ? Math.min(1, (currentPoints ?? 0) / event.totalRequired)
    : 0;

  const completed = !!event?.totalRequired && (currentPoints ?? 0) >= event.totalRequired;

  if (initialLoading) {
    return (
      <View className={`flex-1 px-5 pt-8 ${bgColor(darkmode)}`}>
        <Skeleton width="60%" height={32} />
        <Skeleton width="40%" height={14} style={{ marginTop: 12 }} />
        <Skeleton height={180} radius={16} style={{ marginTop: 32 }} />
        <Skeleton height={120} radius={16} style={{ marginTop: 16 }} />
      </View>
    );
  }

  if (!event) {
    return (
      <ScrollView
        className={`flex-1 ${bgColor(darkmode)}`}
        contentContainerStyle={{ flexGrow: 1, justifyContent: "center" }}
        refreshControl={<RefreshControl refreshing={load} onRefresh={() => setLoad(true)} />}
      >
        <EmptyState icon="trophy-outline" title={t.noEvent} hint={t.noEventHint} />
      </ScrollView>
    );
  }

  return (
    <ScrollView
      className={`flex-1 ${bgColor(darkmode)}`}
      refreshControl={
        <RefreshControl refreshing={load} onRefresh={() => setLoad(true)} />
      }
    >
      <View className="px-5 pt-8">
        <Text
          style={{ fontFamily: fonts.display }}
          className={`text-3xl ${textColor(darkmode)}`}
        >
          {event?.name}
        </Text>
        <Text className={`text-sm mt-1 ${mutedTextColor(darkmode)}`}>
          {event?.startDate?.slice(0, 10)} – {event?.finishDate?.slice(0, 10)}
        </Text>

        <View className="flex-row mt-6">
          <View className="flex-1">
            <Text className={`text-xs ${mutedTextColor(darkmode)}`}>
              {t.timeLeft}
            </Text>
            <Text
              style={{ fontFamily: fonts.displayMedium, fontVariant: ["tabular-nums"] }}
              className={`text-xl mt-1 ${textColor(darkmode)}`}
            >
              {formatTime(timeLeft)}
            </Text>
          </View>
        </View>
      </View>

      <View
        className={`mx-5 mt-6 p-5 rounded-2xl border ${cardBgColor(
          darkmode
        )} ${borderColor(darkmode)}`}
      >
        <Text className={`text-xs ${mutedTextColor(darkmode)}`}>
          {t.progress}
        </Text>
        <View className="flex-row items-end mt-1">
          <Text
            style={{ fontFamily: fonts.display, fontSize: 40, lineHeight: 44 }}
            className={textColor(darkmode)}
          >
            {currentPoints ?? 0}
          </Text>
          <Text className={`text-base ml-2 mb-1 ${secondaryTextColor(darkmode)}`}>
            / {event?.totalRequired ?? 0} pts
          </Text>
          {completed && (
            <View className="flex-row items-center ml-auto mb-1.5 px-2.5 h-7 rounded-lg bg-[#16A34A1F]">
              <Icon name="checkmark" size={14} color="#16A34A" />
              <Text className="text-xs font-medium ml-1 text-[#16A34A]">{t.completed}</Text>
            </View>
          )}
        </View>

        <View className={`h-2.5 mt-5 rounded-full overflow-hidden ${raisedBgColor(darkmode)}`}>
          <View
            className="h-full rounded-full bg-[#C2410C]"
            style={{ width: `${Math.round(progress * 100)}%` }}
          />
        </View>

        {badges.length > 0 && (
          <View className="flex-row justify-between mt-6">
            {milestones.map((ms, i) => {
              const reached = (currentPoints ?? 0) >= ms;
              return (
                <View key={`badge-${ms}`} className="items-center">
                  <View style={{ opacity: reached ? 1 : 0.35 }}>
                    <Picture
                      image={badges[badges.length - 1 - i]?.image}
                      size={48}
                      style="rounded-full"
                    />
                  </View>
                  <Text
                    style={{ fontVariant: ["tabular-nums"] }}
                    className={`text-xs mt-2 ${
                      reached ? textColor(darkmode) : mutedTextColor(darkmode)
                    }`}
                  >
                    {ms} pts
                  </Text>
                </View>
              );
            })}
          </View>
        )}
      </View>

      {ranking.length > 0 && (
        <View
          className={`mx-5 mt-4 rounded-2xl border overflow-hidden ${cardBgColor(
            darkmode
          )} ${borderColor(darkmode)}`}
        >
          <Text className={`text-xs px-5 pt-4 pb-2 ${mutedTextColor(darkmode)}`}>
            {t.ranking}
          </Text>
          {ranking.map((entry, i) => {
            const mine = entry.email === email;
            return (
              <View
                key={entry.email ?? i}
                className={`flex-row items-center px-5 py-3 ${
                  i > 0 ? `border-t ${borderColor(darkmode)}` : ""
                }`}
              >
                <Text
                  style={{ fontFamily: fonts.displayMedium, width: 20, fontVariant: ["tabular-nums"] }}
                  className={`text-sm ${i === 0 ? "text-[#C2410C]" : mutedTextColor(darkmode)}`}
                >
                  {i + 1}
                </Text>
                <Avatar image={entry.image} name={entry.username} size={32} />
                <Text
                  numberOfLines={1}
                  className={`flex-1 ml-3 text-sm ${mine ? "font-semibold" : ""} ${textColor(darkmode)}`}
                >
                  {entry.username}
                  {mine ? ` (${t.you})` : ""}
                </Text>
                <Text
                  style={{ fontFamily: fonts.displayMedium, fontVariant: ["tabular-nums"] }}
                  className={`text-sm ${secondaryTextColor(darkmode)}`}
                >
                  {Math.round(entry.points)} pts
                </Text>
              </View>
            );
          })}
        </View>
      )}

      <Text className={`text-sm mx-5 mt-4 mb-8 ${mutedTextColor(darkmode)}`}>
        {t.finalMessage.replace(/!/g, "").replace(/¡/g, "")}
      </Text>
    </ScrollView>
  );
};

export default EventsScreen;
