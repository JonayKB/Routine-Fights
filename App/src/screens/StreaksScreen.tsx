import { View, Text, FlatList, RefreshControl, Alert } from "react-native";
import React, { useEffect, useState } from "react";
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
import { translations } from "../../translations/translation";

type Props = NativeStackScreenProps<ActivitiesStackProps, "Streaks">;

const StreaksScreen = ({ navigation, route }: Props) => {
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

  const [activities, setActivities] = useState<ActivityWithStreak[]>([
    addActivity,
  ]);

  useEffect(() => {
    fetchActivities();
  }, []);

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
      Alert.alert("Error", error.response.data);
    } finally {
      setLoad(false);
    }
  };

  const deleteActivity = async (id: string) => {
    try {
      await unsubscribeActivity(id);
      setLoad(true);
    } catch (error) {
      Alert.alert("Error", error.response.data);
    }
  };

  const streakDetails = (activity: ActivityWithStreak) => {
    navigation.navigate("ActivityDetails", {
      activity: activity,
      suscribed: true,
    });
  };

  return (
    <View className={`flex-1 ${bgColor(darkmode)} items-center pt-8`}>
      <Text className={`text-4xl font-bold mb-2 ${textColor(darkmode)}`}>
        {translations[language || "en-EN"].screens.Streaks.myStreaks}
      </Text>

      <FlatList
        refreshControl={
          <RefreshControl refreshing={load} onRefresh={() => setLoad(true)} />
        }
        style={{ width: "100%" }}
        data={activities}
        renderItem={({ item }) => {
          if (item.id === "true") {
            return (
              <AddButton
                navigateFunction={() => navigation.navigate("Activities")}
              />
            );
          }
          return (
            <View>
              <Streak
                streak={item}
                unsubscribeFunction={() => deleteActivity(item.id)}
                selectFunction={() => streakDetails(item)}
              />
            </View>
          );
        }}
        keyExtractor={(item) => item.id}
      />
    </View>
  );
};

export default StreaksScreen;
