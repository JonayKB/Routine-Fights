import React from "react";
import { Text, TouchableOpacity, View } from "react-native";
import Icon from "react-native-vector-icons/Ionicons";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import {
  borderColor,
  iconColor,
  mutedTextColor,
  raisedBgColor,
  textColor,
} from "../utils/Utils";

type Props = {
  icon?: string;
  /** Render something richer than an icon, e.g. an unlit flame. */
  visual?: React.ReactNode;
  title: string;
  hint?: string;
  actionLabel?: string;
  onAction?: () => void;
};

const EmptyState = ({ icon, visual, title, hint, actionLabel, onAction }: Props) => {
  const { darkmode } = useSettingsContext();

  return (
    <View className="items-center px-10 py-16">
      {visual ?? (
        <View
          className={`w-14 h-14 rounded-2xl items-center justify-center border ${raisedBgColor(
            darkmode
          )} ${borderColor(darkmode)}`}
        >
          <Icon name={icon ?? "sparkles-outline"} size={22} color={iconColor(darkmode)} />
        </View>
      )}
      <Text className={`text-base font-medium mt-5 text-center ${textColor(darkmode)}`}>
        {title}
      </Text>
      {!!hint && (
        <Text className={`text-sm mt-1.5 text-center ${mutedTextColor(darkmode)}`}>{hint}</Text>
      )}
      {!!actionLabel && !!onAction && (
        <TouchableOpacity
          activeOpacity={0.85}
          onPress={onAction}
          className="h-11 px-5 mt-6 rounded-xl bg-[#C2410C] items-center justify-center"
        >
          <Text className="text-sm font-semibold text-white">{actionLabel}</Text>
        </TouchableOpacity>
      )}
    </View>
  );
};

export default EmptyState;
