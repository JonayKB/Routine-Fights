import { View, Text, TouchableOpacity } from "react-native";
import React from "react";
import { ActivityWithStreak } from "../utils/Activity";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { translations } from "../../translations/translation";
import StreakFlame, { nextTierFor, tierFor } from "./StreakFlame";
import { formatString, streakUnit } from "./Streak";
import { fonts } from "../styles/theme";

type Props = {
  activity: ActivityWithStreak;
  onPress: () => void;
};

/**
 * The one rich moment of the streaks screen: the hottest streak burning on a
 * charcoal card, so the fire carries the colour and the chrome stays quiet.
 */
const StreakHero = ({ activity, onPress }: Props) => {
  const { language } = useSettingsContext();
  const t = translations[language || "en-EN"].screens.Streaks;
  const value = activity?.streak ?? 0;
  const tier = tierFor(value);
  const next = nextTierFor(value);
  const progress = next
    ? Math.min(1, (value - tier.min) / Math.max(1, next.min - tier.min))
    : 1;

  return (
    <TouchableOpacity
      activeOpacity={0.9}
      onPress={onPress}
      className="mx-5 mb-6 rounded-2xl bg-[#12110F] border border-[#EFE9DE1A] overflow-hidden"
    >
      <View className="flex-row items-center px-6 pt-6 pb-5">
        <View className="w-28 h-32 items-center justify-end">
          <StreakFlame streak={value} size={92} animated />
        </View>

        <View className="flex-1 ml-5">
          <Text className="text-xs text-[#8A847A]">{t.best}</Text>
          <View className="flex-row items-end mt-1">
            <Text
              style={{ fontFamily: fonts.display, fontSize: 52, lineHeight: 56 }}
              className="text-[#EFE9DE]"
            >
              {value}
            </Text>
            <Text className="text-base text-[#AAA398] ml-2 mb-2">
              {streakUnit(t, activity?.timeRate, value)}
            </Text>
          </View>
          <Text
            style={{ fontFamily: fonts.displayMedium, color: tier.outer[0] }}
            className="text-sm"
          >
            {value === 0 ? t.lightIt : t.tiers[tier.key]}
          </Text>
          <Text numberOfLines={1} className="text-sm text-[#AAA398] mt-1">
            {activity?.name}
          </Text>
        </View>
      </View>

      <View className="px-6 pb-6">
        <View className="h-1.5 rounded-full bg-[#EFE9DE1A] overflow-hidden">
          <View
            style={{
              width: `${Math.round(progress * 100)}%`,
              backgroundColor: next ? tierFor(next.min).outer[1] : tier.outer[1],
            }}
            className="h-full rounded-full"
          />
        </View>
        <Text className="text-xs text-[#8A847A] mt-2">
          {next
            ? formatString(t.toNext, {
                n: next.min - value,
                tier: t.tiers[next.key],
              })
            : t.maxTier}
        </Text>
      </View>
    </TouchableOpacity>
  );
};

export default StreakHero;
