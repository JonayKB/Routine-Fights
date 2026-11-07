import { View, Text, TouchableOpacity } from "react-native";
import React from "react";
import Icon from "react-native-vector-icons/Ionicons";
import { ActivityWithStreak } from "../utils/Activity";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import {
  borderColor,
  cardBgColor,
  iconColor,
  mutedTextColor,
  textColor,
} from "../utils/Utils";
import { translations } from "../../translations/translation";
import StreakFlame, { inkFor, nextTierFor, tierFor } from "./StreakFlame";
import { fonts } from "../styles/theme";

type Props = {
  streak: ActivityWithStreak;
  unsubscribeFunction: () => void;
  selectFunction: () => void;
};

export const formatString = (template: string, values: Record<string, string | number>) =>
  template.replace(/\{(\w+)\}/g, (_, key) => String(values[key] ?? ""));

export const streakUnit = (t: any, timeRate: string, value: number) => {
  const units = t.units?.[timeRate] ?? t.units?.daily ?? ["", ""];
  return value === 1 ? units[0] : units[1];
};

export const remainingOf = (activity: ActivityWithStreak) =>
  activity?.timesRemaining == null
    ? Number(activity?.timesRequiered ?? 0)
    : activity.timesRemaining;

const Streak = (props: Props) => {
  const { darkmode, language } = useSettingsContext();
  const t = translations[language || "en-EN"].screens.Streaks;
  const value = props.streak?.streak ?? 0;
  const tier = tierFor(value);
  const next = nextTierFor(value);
  const remaining = remainingOf(props.streak);

  return (
    <TouchableOpacity
      activeOpacity={0.8}
      onPress={props.selectFunction}
      className={`flex-row items-center mx-5 mb-3 px-4 py-4 rounded-2xl border ${cardBgColor(
        darkmode
      )} ${borderColor(darkmode)}`}
    >
      <View className="w-12 items-center">
        <StreakFlame streak={value} size={36} animated glow={false} />
      </View>

      <View className="flex-1 ml-3">
        <Text
          numberOfLines={1}
          className={`text-base font-medium ${textColor(darkmode)}`}
        >
          {props.streak?.name}
        </Text>
        <View className="flex-row items-center mt-1">
          <Text
            style={{ fontFamily: fonts.displayMedium, color: inkFor(tier, darkmode) }}
            className="text-sm"
          >
            {value} {streakUnit(t, props.streak?.timeRate, value)}
          </Text>
          <Text className={`text-sm ${mutedTextColor(darkmode)}`}>
            {"  ·  "}
            {next
              ? formatString(t.toNext, {
                  n: next.min - value,
                  tier: t.tiers[next.key],
                })
              : t.maxTier}
          </Text>
        </View>
      </View>

      <View className="items-end ml-3">
        {remaining > 0 ? (
          <View
            accessibilityLabel={formatString(t.left, { n: remaining })}
            className={`min-w-[22px] h-[22px] px-1.5 rounded-full border items-center justify-center ${
              darkmode ? "border-[#FB923C]" : "border-[#C2410C]"
            }`}
          >
            <Text
              style={{ fontFamily: fonts.displayMedium }}
              className={`text-xs ${darkmode ? "text-[#FB923C]" : "text-[#C2410C]"}`}
            >
              {remaining}
            </Text>
          </View>
        ) : (
          <Icon name="checkmark-circle" size={18} color="#16A34A" />
        )}
      </View>

      <TouchableOpacity
        onPress={props.unsubscribeFunction}
        hitSlop={12}
        className="ml-4"
      >
        <Icon name="trash-outline" size={18} color={iconColor(darkmode)} />
      </TouchableOpacity>
    </TouchableOpacity>
  );
};

export default Streak;
