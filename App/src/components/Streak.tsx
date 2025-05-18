import { View, Text, TouchableOpacity } from "react-native";
import React from "react";
import Icon from "react-native-vector-icons/Ionicons";
import { ActivityWithStreak } from "../utils/Activity";
import { useSettingsContext } from "../contexts/SettingsContextProvider";

type Props = {
  streak: ActivityWithStreak;
  unsubscribeFunction: () => void;
  selectFunction: () => void;
};

const Streak = (props: Props) => {
  const { darkmode } = useSettingsContext();

  return (
    <TouchableOpacity
      onPress={props.selectFunction}
      className={`flex-row items-center w-11/12 h-36 rounded-3xl mt-8 mx-auto ${
        darkmode ? "bg-[#4B294F]" : "bg-[#E8E2F0]"
      }`}
    >
      <View
        className={`w-20 h-20 rounded-full m-5 items-center justify-center ${
          darkmode ? "bg-[#5A2978]" : "bg-[#F65261]"
        }`}
      >
        <Text className="text-3xl font-bold text-white">
          {props.streak?.streak}
        </Text>
      </View>

      <View>
        <Text
          className={`text-3xl font-bold ${
            darkmode ? "text-white" : "text-[#333333]"
          }`}
        >
          {props.streak?.name}
        </Text>
        <Text
          className={`text-2xl mt-4 ${
            darkmode ? "text-white" : "text-[#333333]"
          }`}
        >
          Times remaining:{" "}
          {props.streak?.timesRemaining == null
            ? props.streak?.timesRequiered
            : props.streak?.timesRemaining}
        </Text>
      </View>

      <TouchableOpacity
        onPress={props.unsubscribeFunction}
        className="ml-auto mr-5"
      >
        <Icon name="trash" size={35} color={darkmode ? "#8F7B9E" : "#7D3C98"} />
      </TouchableOpacity>
    </TouchableOpacity>
  );
};

export default Streak;
