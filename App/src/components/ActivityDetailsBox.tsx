import { View, Text } from "react-native";
import React from "react";
import { Activity } from "../utils/Activity";
import { translations } from "../../translations/translation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";

type Props = {
  activity: Activity;
};

const ActivityDetailsBox = ({ activity }: Props) => {
  const { language, darkmode } = useSettingsContext();

  return (
    <View
      className={`rounded-2xl w-11/12 px-6 py-5 mt-6 items-center border-2 ${
        darkmode
          ? "bg-[#4B294F] border-[#B28DFF]"
          : "bg-[#E8E2F0] border-[#4B0082]"
      }`}
    >
      <Text
        className={`text-3xl font-bold mb-2 ${
          darkmode ? "text-white" : "text-[#4B0082]"
        }`}
      >
        {activity.name}
      </Text>

      <Text
        className={`text-lg text-center mb-6 ${
          darkmode ? "text-[#E0D3F5]" : "text-[#4B0082]"
        }`}
      >
        {activity.description}
      </Text>

      <Text
        className={`text-xl mb-1 ${
          darkmode ? "text-[#B0A1C1]" : "text-[#4B0082]"
        }`}
      >
        {translations[language || "en-US"].screens.ActivityDetails.numOfTimes}:{" "}
        {activity.timesRequiered}
      </Text>

      <Text
        className={`text-xl ${darkmode ? "text-[#B0A1C1]" : "text-[#4B0082]"}`}
      >
        {translations[language || "en-US"].screens.ActivityDetails.frequency}:{" "}
        {translations[language || "en-US"].screens.ActivityDetails.timeRates[activity.timeRate]}
      </Text>
    </View>
  );
};

export default ActivityDetailsBox;
