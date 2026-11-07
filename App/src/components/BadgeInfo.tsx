import { Text, View } from "react-native";
import React from "react";
import { Badge } from "../utils/Badge";
import Picture from "./Picture";

type Props = {
  item: Badge;
};

const BadgeInfo = ({ item }: Props) => {
  return (
    <View className="flex-row items-center justify-start p-5 bg-white rounded-2xl w-11/12 mx-auto my-3">
      <Picture
        image={item.image}
        size={90}
        style="rounded-full border border-[#1F1B161A]"
      />
      <View className="ml-5 flex-1">
        <Text className="text-lg font-semibold text-[#1F1B16] mb-1">
          {item.communityEvent?.name}
        </Text>
        <Text className="text-lg text-[#1F1B16]">Level: {item.level}</Text>
        <Text className="text-lg text-[#1F1B16]">
          Points Needed: {item.communityEvent?.totalRequired}
        </Text>
        <Text className="text-lg text-[#1F1B16]">
          End Date: {item.communityEvent?.finishDate.slice(0, 10)}
        </Text>
      </View>
    </View>
  );
};

export default BadgeInfo;
