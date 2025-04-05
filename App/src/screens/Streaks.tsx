import {
  View,
  Text,
  TouchableOpacity,
  FlatList,
  RefreshControl,
} from "react-native";
import React, { useState } from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ActivitiesStackProps } from "../navigation/ActivitiesStackNavigation";
import { Activity } from "../utils/Activity";
import Streak from "../components/Streak";

type Props = NativeStackScreenProps<ActivitiesStackProps, "Streaks">;

const Streaks = ({ navigation, route }: Props) => {
  const [load, setLoad] = useState<boolean>(false);

  const activities: Activity[] = [
    {
      id: "1",
      name: "Activity 1",
      description: "Description 1",
      image: "https://picsum.photos/200/300",
      timeRate: "1",
      timesRequired: "1",
      category: "1",
    },
    {
      id: "2",
      name: "Activity 2",
      description: "Description 2",
      image: "https://picsum.photos/200/300",
      timeRate: "2",
      timesRequired: "2",
      category: "2",
    },
    {
      id: "3",
      name: "Activity 3",
      description: "Description 3",
      image: "https://picsum.photos/200/300",
      timeRate: "3",
      timesRequired: "3",
      category: "3",
    },
    {
      id: "7",
      name: "Activity 3",
      description: "Description 3",
      image: "https://picsum.photos/200/300",
      timeRate: "3",
      timesRequired: "3",
      category: "3",
    },
    {
      id: "4",
      name: "Activity 3",
      description: "Description 3",
      image: "https://picsum.photos/200/300",
      timeRate: "3",
      timesRequired: "3",
      category: "3",
    },
    {
      id: "5",
      name: "Activity 3",
      description: "Description 3",
      image: "https://picsum.photos/200/300",
      timeRate: "3",
      timesRequired: "3",
      category: "3",
    },
    {
      id: "true",
      name: null,
      description: null,
      image: null,
      timeRate: "3",
      timesRequired: null,
      category: null,
    },
  ];

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
