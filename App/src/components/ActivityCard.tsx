import { Dimensions, Text, TouchableOpacity, View } from "react-native";
import React from "react";
import { Activity } from "../utils/Activity";
import Picture from "./Picture";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { translations } from "../../translations/translation";
import {
  borderColor,
  cardBgColor,
  mutedTextColor,
  textColor,
} from "../utils/Utils";

type Props = {
  navigateFunction: () => void;
  item: Activity;
};

const ActivityCard = (props: Props) => {
  const { darkmode, language } = useSettingsContext();
  
  const width = (Dimensions.get("window").width - 20 * 2 - 12) / 2;
  return (
    <TouchableOpacity
      activeOpacity={0.8}
      onPress={props.navigateFunction}
      style={{ width, margin: 6 }}
      className={`rounded-2xl overflow-hidden border ${cardBgColor(darkmode)} ${borderColor(
        darkmode
      )}`}
    >
      <Picture image={props.item.image} size={width} height={Math.round(width * 0.75)} style="" />
      <View className="px-3 py-3">
        <Text numberOfLines={1} className={`text-sm font-medium ${textColor(darkmode)}`}>
          {props.item.name}
        </Text>
        {!!props.item.timeRate && (
          <Text className={`text-xs mt-0.5 ${mutedTextColor(darkmode)}`}>
            {translations[language || "en-EN"].screens.ActivityForm.timeRates[props.item.timeRate]}
          </Text>
        )}
      </View>
    </TouchableOpacity>
  );
};

export default ActivityCard;
