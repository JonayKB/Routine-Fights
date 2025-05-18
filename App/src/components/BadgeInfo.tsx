import { Text, View } from "react-native";
import React from "react";
import { Badge } from "../utils/Badge";
import Picture from "./Picture";

type Props = {
  item: Badge;
};

const BadgeInfo = ({ item }: Props) => {
  return (
    <View className="flex-row items-center justify-evenly p-6 px-8 bg-white rounded-xl shadow-md">
      <Picture image={item.image} size={100} style="rounded-full mt-4" />
      <View className="ml-8">
        <Text className="text-2xl font-bold text-[#1C1C1E] mb-2">
          {item.communityEvent?.name}
        </Text>
        <Text className="text-[#1C1C1E]">Level: {item.level}</Text>
        <Text className="text-[#1C1C1E]">
          Points Needed: {item.communityEvent?.totalRequired}
        </Text>
        <Text className="text-[#1C1C1E]">
          End Date: {item.communityEvent?.finishDate.slice(0, 10)}
        </Text>
      </View>
    </View>
  );
};

export default BadgeInfo;
