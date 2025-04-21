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
import { Activity } from "../utils/Activity";
import Streak from "../components/Streak";
import { getActivities } from "../repositories/StreakRepository";

type Props = NativeStackScreenProps<ActivitiesStackProps, "Streaks">;

const Streaks = ({ navigation, route }: Props) => {
  const [load, setLoad] = useState<boolean>(false);

  const addActivity: Activity = {
    id: "true",
    name: null,
    description: null,
    image: null,
    timeRate: "3",
    timesRequired: null,
    category: null,
  };
  
  const [activities, setActivities] = useState<Activity[]>([addActivity]);

  useEffect(() => {
    const fetchActivities = async () => {
      const list = await getActivities();
      list.push(addActivity);
      setActivities(list);
    };
    fetchActivities();
  }, []);

  return (
    <View className="flex-1 items-center">
      <View className="items-center w-full">
        <FlatList
          refreshControl={
            <RefreshControl
              refreshing={load}
              onRefresh={() => {
                setLoad(true);
                setTimeout(() => {
                  setLoad(false);
                }, 1000);
              }}
            />
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
                  <Streak name={item.name} description={item.description} />
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

export default Streaks;
