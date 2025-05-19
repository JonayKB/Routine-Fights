import { Text, View } from "react-native";
import React from "react";
import { Badge } from "../utils/Badge";
import Picture from "./Picture";

type Props = {
  item: Badge;
};

const BadgeInfo = ({ item }: Props) => {
  return (
    <View className="flex-row items-center justify-start p-5 bg-[#E8E2F0] rounded-2xl w-11/12 mx-auto my-3">
      <Picture
        image={item.image}
        size={90}
        style="rounded-full border-2 border-[#4B0082]"
      />
      <View className="ml-5 flex-1">
        <Text className="text-2xl font-bold text-[#4B0082] mb-1">
          {item.communityEvent?.name}
        </Text>
        <Text className="text-lg text-[#333333]">Level: {item.level}</Text>
        <Text className="text-lg text-[#333333]">
          Points Needed: {item.communityEvent?.totalRequired}
        </Text>
        <Text className="text-lg text-[#333333]">
          End Date: {item.communityEvent?.finishDate.slice(0, 10)}
        </Text>
      </View>
    </View>
  );
};

export default BadgeInfo;
