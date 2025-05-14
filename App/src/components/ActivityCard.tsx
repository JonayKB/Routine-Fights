import { Image, Text, TouchableOpacity } from "react-native";
import React from "react";
import { Activity } from "../utils/Activity";

type Props = {
  navigateFunction: () => void;
  item: Activity;
};

const ActivityCard = (props: Props) => {
  return (
    <TouchableOpacity
      onPress={props.navigateFunction}
      className="w-44 h-56 m-6"
    >
      <Image
        source={{
          uri: props.item.image,
        }}
        className="w-full h-40 rounded-t-2xl border-[#4B0082] border-2"
      />
      <Text className="flex-1 rounded-b-2xl border-[#4B0082] border-2 align-middle text-[#4B0082] text-center text-xl bg-white">{props.item.name}</Text>
    </TouchableOpacity>
  );
};

export default ActivityCard;
