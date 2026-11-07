import { View, Text } from "react-native";
import React from "react";
import { Activity } from "../utils/Activity";
import { translations } from "../../translations/translation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import {
  borderColor,
  cardBgColor,
  mutedTextColor,
  secondaryTextColor,
  textColor,
} from "../utils/Utils";
import { fonts } from "../styles/theme";

type Props = {
  activity: Activity;
};

const ActivityDetailsBox = ({ activity }: Props) => {
  const { language, darkmode } = useSettingsContext();

  const t = translations[language || "en-EN"].screens.ActivityDetails;

  const Fact = ({ label, value }: { label: string; value: string }) => (
    <View className="flex-1">
      <Text className={`text-xs ${mutedTextColor(darkmode)}`}>{label}</Text>
      <Text
        style={{ fontFamily: fonts.displayMedium }}
        className={`text-lg mt-1 ${textColor(darkmode)}`}
      >
        {value}
      </Text>
    </View>
  );

  return (
    <View className="px-5 pt-5">
      <Text
        style={{ fontFamily: fonts.display }}
        className={`text-2xl ${textColor(darkmode)}`}
      >
        {activity.name}
      </Text>
      {!!activity.description && (
        <Text className={`text-base mt-2 ${secondaryTextColor(darkmode)}`}>
          {activity.description}
        </Text>
      )}

      <View
        className={`flex-row mt-5 p-4 rounded-2xl border ${cardBgColor(
          darkmode
        )} ${borderColor(darkmode)}`}
      >
        <Fact label={t.numOfTimes} value={String(activity.timesRequiered ?? "")} />
        <Fact label={t.frequency} value={t.timeRates[activity.timeRate] ?? ""} />
      </View>
    </View>
  );
};

export default ActivityDetailsBox;
