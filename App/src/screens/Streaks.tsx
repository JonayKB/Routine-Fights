import { View, Text, TouchableOpacity } from "react-native";
import React from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ActivitiesStackProps } from "../navigation/ActivitiesStackNavigation";

type Props = NativeStackScreenProps<ActivitiesStackProps, "Streaks">;

const Streaks = ({ navigation, route }: Props) => {
  return (
    <View className="flex-1 items-center">
      <TouchableOpacity onPress={() => navigation.navigate("Activities")} className="bg-[#4B0082] w-11/12 mt-10 rounded-3xl h-20">
        <Text className="text-8xl font-bold text-center text-white">+</Text>
      </TouchableOpacity>
    </View>
  );
};

export default Streaks;
