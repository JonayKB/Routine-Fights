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
        <Text className="text-2xl font-bold text-[#333333] mb-2">
          {item.communityEvent?.name}
        </Text>
        <Text className="text-[#333333]">Level: {item.level}</Text>
        <Text className="text-[#333333]">
          Points Needed: {item.communityEvent?.totalRequired}
        </Text>
        <Text className="text-[#333333]">
          End Date: {item.communityEvent?.finishDate.slice(0, 10)}
        </Text>
      </View>
    </View>
  );
};

export default BadgeInfo;
