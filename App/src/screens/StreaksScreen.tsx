import {
  View,
  Text,
  TouchableOpacity,
  FlatList,
  RefreshControl,
} from "react-native";
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

type Props = NativeStackScreenProps<ActivitiesStackProps, "Streaks">;

const StreaksScreen = ({ navigation, route }: Props) => {
  const [load, setLoad] = useState<boolean>(false);
  const { darkmode } = useSettingsContext();

  const addActivity: ActivityWithStreak = {
    id: "true",
    name: null,
    description: null,
    image: null,
    timeRate: "3",
    timesRequiered: null,
    streak: 0,
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
      console.error("Error fetching activities:", error);
    } finally {
      setLoad(false);
    }
  };

  const deleteActivity = async (id: string) => {
    try {
      await unsubscribeActivity(id);
      setLoad(true);
    } catch (error) {
      console.error("Error unsubscribing activity:", error);
    }
  };

  return (
    <View
      className={`flex-1 bg-[#${darkmode ? "2C2C2C" : "CCCCCC"}] items-center`}
    >
      <View className="items-center w-full">
        <FlatList
          refreshControl={
            <RefreshControl refreshing={load} onRefresh={() => setLoad(true)} />
          }
          style={{ width: "100%" }}
          data={activities}
          renderItem={({ item }) => {
            return (
              <View>
                {item.id === "true" ? (
                  <TouchableOpacity
                    onPress={() => navigation.navigate("Activities")}
                    className="items-center w-11/12 rounded-3xl mx-auto bg-[#4B0082] mt-5"
                  >
                    <Text className="text-8xl font-bold text-center text-white">
                      +
                    </Text>
                  </TouchableOpacity>
                ) : (
                  <Streak
                    name={item.name}
                    description={item.description}
                    streak={item.streak}
                    unsubscribeFunction={() => deleteActivity(item.id)}
                  />
                )}
              </View>
            );
          }}
          keyExtractor={(item) => item.id}
        />
      </View>
    </View>
  );
};

export default StreaksScreen;
