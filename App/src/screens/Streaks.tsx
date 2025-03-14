import { View, Text, TouchableOpacity, FlatList } from "react-native";
import React from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ActivitiesStackProps } from "../navigation/ActivitiesStackNavigation";

type Props = NativeStackScreenProps<ActivitiesStackProps, "Streaks">;

const Streaks = ({ navigation, route }: Props) => {
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
  ];

  return (
    <View className="flex-1 items-center">
      <FlatList
        style={{ width: "100%" }}
        data={activities}
        renderItem={({ item }) => {
          return (
            <View className="flex-row items-center bg-[#7C5AF11A] w-11/12 h-36 rounded-3xl mt-5">
              <View className="w-20 h-20 bg-blue-400 rounded-full m-5"></View>
              <View>
                <Text className="text-black text-3xl">{item.name}</Text>
                <Text className="text-black text-xl">{item.description}</Text>
              </View>
            </View>
          );
        }}
        keyExtractor={(item) => item.id}
      />
      <TouchableOpacity
        onPress={() => navigation.navigate("Activities")}
        className="bg-[#4B0082] w-11/12 mt-10 rounded-3xl h-20"
      >
        <Text className="text-8xl font-bold text-center text-white">+</Text>
      </TouchableOpacity>
    </View>
  );
};

export default Streaks;
