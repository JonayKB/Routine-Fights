import { View, Text, FlatList, RefreshControl } from "react-native";
import React, { useCallback, useEffect, useRef, useState } from "react";
import { useFocusEffect } from "@react-navigation/native";
import Skeleton from "../components/Skeleton";
import EmptyState from "../components/EmptyState";
import StreakFlame from "../components/StreakFlame";
import { formatString, streakUnit } from "../components/Streak";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ActivitiesStackProps } from "../navigation/ActivitiesStackNavigation";
import Streak from "../components/Streak";
import {
  getSubscribedActivities,
  unsubscribeActivity,
} from "../repositories/ActivityRepository";
import { ActivityWithStreak } from "../utils/Activity";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { bgColor, textColor } from "../utils/Utils";
import AddButton from "../components/AddButton";
import StreakHero from "../components/StreakHero";
import { fonts } from "../styles/theme";
import { translations } from "../../translations/translation";
import { errorMessage } from "../utils/graphql";
import { useToast } from "../contexts/FeedbackContextProvider";

type Props = NativeStackScreenProps<ActivitiesStackProps, "Streaks">;

const StreaksScreen = ({ navigation, route }: Props) => {
  const toast = useToast();
  const [load, setLoad] = useState<boolean>(false);
  const { darkmode, language } = useSettingsContext();

  const addActivity: ActivityWithStreak = {
    id: "true",
    name: null,
    description: null,
    image: null,
    timeRate: "daily",
    timesRequiered: null,
    streak: 0,
    timesRemaining: 0,
  };

  const [activities, setActivities] = useState<ActivityWithStreak[]>([]);
  const [initialLoading, setInitialLoading] = useState<boolean>(true);

  useEffect(() => {
    fetchActivities();
  }, []);

  // After posting, coming back here shows the new streak.
  const focusedOnce = useRef(false);
  useFocusEffect(
    useCallback(() => {
      if (!focusedOnce.current) {
        focusedOnce.current = true;
        return;
      }
      fetchActivities();
    }, [])
  );

  useEffect(() => {
    if (load) {
      fetchActivities();
    }
  }, [load]);

  const fetchActivities = async () => {
    try {
      const list = await getSubscribedActivities();
      list.push(addActivity);
      setActivities(list);
    } catch (error) {
      toast.error(errorMessage(error));
    } finally {
      setLoad(false);
      setInitialLoading(false);
    }
  };

  const confirmDelete = async (activity: ActivityWithStreak) => {
    const t = translations[language || "en-EN"].screens;
    const value = activity.streak ?? 0;
    const ok = await toast.confirm({
      title: formatString(t.Streaks.deleteTitle, { name: activity.name }),
      message:
        value > 0
          ? formatString(t.Streaks.deleteBody, {
              n: value,
              unit: streakUnit(t.Streaks, activity.timeRate, value),
            })
          : t.Streaks.deleteBodyNoStreak,
      confirmLabel: t.Streaks.delete,
      cancelLabel: t.Common.cancel,
      destructive: true,
    });
    if (ok) deleteActivity(activity.id);
  };

  const deleteActivity = async (id: string) => {
    try {
      await unsubscribeActivity(id);
      setLoad(true);
    } catch (error) {
      toast.error(errorMessage(error));
    }
  };

  const streakDetails = (activity: ActivityWithStreak) => {
    navigation.navigate("ActivityDetails", {
      activity: activity,
      suscribed: true,
    });
  };

  const hottest = activities
    .filter((a) => a.id !== "true")
    .reduce<ActivityWithStreak | null>(
      (best, a) => (!best || (a.streak ?? 0) > (best.streak ?? 0) ? a : best),
      null
    );

  return (
    <View className={`flex-1 ${bgColor(darkmode)}`}>
      <FlatList
        refreshControl={
          <RefreshControl refreshing={load} onRefresh={() => setLoad(true)} />
        }
        style={{ width: "100%" }}
        contentContainerStyle={{ paddingBottom: 24 }}
        data={activities}
        ListHeaderComponent={
          <View>
            <Text
              style={{ fontFamily: fonts.display }}
              className={`text-3xl px-5 pt-8 pb-5 ${textColor(darkmode)}`}
            >
              {translations[language || "en-EN"].screens.Streaks.myStreaks}
            </Text>
            {!initialLoading && !hottest && (
              <EmptyState
                visual={<StreakFlame streak={0} size={56} />}
                title={translations[language || "en-EN"].screens.Streaks.emptyTitle}
                hint={translations[language || "en-EN"].screens.Streaks.emptyHint}
              />
            )}
            {hottest && (
              <StreakHero
                activity={hottest}
                onPress={() => streakDetails(hottest)}
              />
            )}
          </View>
        }
        ListEmptyComponent={
          initialLoading ? (
            <View className="px-5">
              <Skeleton height={176} radius={16} />
              <Skeleton height={76} radius={16} style={{ marginTop: 24 }} />
              <Skeleton height={76} radius={16} style={{ marginTop: 12 }} />
            </View>
          ) : null
        }
        renderItem={({ item }) => {
          if (item.id === "true") {
            return (
              <AddButton
                navigateFunction={() => navigation.navigate("Activities")}
              />
            );
          }
          return (
            <Streak
              streak={item}
              unsubscribeFunction={() => confirmDelete(item)}
              selectFunction={() => streakDetails(item)}
            />
          );
        }}
        keyExtractor={(item) => item.id}
      />
    </View>
  );
};

export default StreaksScreen;
