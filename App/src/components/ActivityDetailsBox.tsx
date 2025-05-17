import { View, Text } from "react-native";
import React from "react";
import { Activity } from "../utils/Activity";
import { translations } from "../../translations/translation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";

type Props = {
  activity: Activity;
};

const ActivityDetailsBox = ({ activity }: Props) => {
  const { language } = useSettingsContext();
  
  return (
    <View className="bg-[#E4D8E9] rounded-2xl w-80 h-72 m-12 border-2 border-[#4B0082] justify-evenly items-center">
      <Text className="text-[#4B0082] text-4xl">{activity.name}</Text>
      <Text className="text-[#4B0082] text-lg mb-10">
        {activity.description}
      </Text>
      <Text className="text-[#4B0082] text-xl">
        {translations[language || "en-EN"].screens.ActivityDetails.numOfTimes}:{" "}
        {activity.timesRequiered}
      </Text>
      <Text className="text-[#4B0082] text-xl">
        {translations[language || "en-EN"].screens.ActivityDetails.frequency}:{" "}
        {activity.timeRate}
      </Text>
    </View>
  );
};

export default ActivityDetailsBox;
