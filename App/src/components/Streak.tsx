import { View, Text, TouchableOpacity } from "react-native";
import React from "react";
import Icon from "react-native-vector-icons/Ionicons";
import { ActivityWithStreak } from "../utils/Activity";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { cardBgColor, iconColor, textColor } from "../utils/Utils";
import { translations } from "../../translations/translation";

type Props = {
  streak: ActivityWithStreak;
  unsubscribeFunction: () => void;
  selectFunction: () => void;
};

const Streak = (props: Props) => {
  const { darkmode, language } = useSettingsContext();

  return (
    <TouchableOpacity
      onPress={props.selectFunction}
      className={`flex-row items-center w-11/12 h-36 rounded-3xl mt-8 mx-auto ${cardBgColor(
        darkmode
      )}`}
    >
      <View
        className={`w-20 h-20 rounded-full m-5 items-center justify-center bg-[#F65261]`}
      >
        <Text className="text-3xl font-bold text-white">
          {props.streak?.streak}
        </Text>
      </View>

      <View>
        <Text className={`text-3xl font-bold ${textColor(darkmode)}`}>
          {props.streak?.name}
        </Text>
        <Text className={`text-2xl mt-4 ${textColor(darkmode)}`}>
          {translations[language || "en-EN"].screens.Streaks.timesRemaining}
          {": "}
          {props.streak?.timesRemaining == null
            ? props.streak?.timesRequiered
            : props.streak?.timesRemaining}
        </Text>
      </View>

      <TouchableOpacity
        onPress={props.unsubscribeFunction}
        className="ml-auto mr-5"
      >
        <Icon name="trash" size={35} color={iconColor(darkmode)} />
      </TouchableOpacity>
    </TouchableOpacity>
  );
};

export default Streak;
