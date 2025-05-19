import { Text, TouchableOpacity } from "react-native";
import React from "react";
import { Activity } from "../utils/Activity";
import Picture from "./Picture";
import { useSettingsContext } from "../contexts/SettingsContextProvider";

type Props = {
  navigateFunction: () => void;
  item: Activity;
};

const ActivityCard = (props: Props) => {
  const { darkmode } = useSettingsContext();
  
  return (
    <TouchableOpacity
      onPress={props.navigateFunction}
      className="w-44 h-56 m-6"
    >
      <Picture
        image={props.item.image}
        size={44}
        height={40}
        style={`w-full h-40 rounded-t-2xl border-2 ${
          darkmode ? "border-[#B28DFF]" : "border-[#4B0082]"
        }`}
      />
      <Text
        className={`flex-1 rounded-b-2xl border-2 text-center text-xl align-middle px-1 ${
          darkmode
            ? "border-[#B28DFF] bg-[#1C1C1E] text-[#B28DFF]"
            : "border-[#4B0082] bg-white text-[#4B0082]"
        }`}
      >
        {props.item.name}
      </Text>
    </TouchableOpacity>
  );
};

export default ActivityCard;
