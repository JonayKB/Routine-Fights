import React from "react";
import { Text, TouchableOpacity, View } from "react-native";
import Icon from "react-native-vector-icons/Ionicons";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { colors, palette } from "../styles/theme";
import { iconColor } from "../utils/Utils";

type Props = {
  icon: string;
  count: number;
  label: string;
  onPress: () => void;
};

/** A header icon with an unread counter. */
const IconBadge = ({ icon, count, label, onPress }: Props) => {
  const { darkmode } = useSettingsContext();
  return (
    <TouchableOpacity
      onPress={onPress}
      hitSlop={8}
      accessibilityRole="button"
      accessibilityLabel={count > 0 ? `${label}, ${count}` : label}
      className="ml-4"
    >
      <Icon name={icon} size={23} color={iconColor(darkmode)} />
      {count > 0 && (
        <View
          style={{
            position: "absolute",
            top: -5,
            right: -8,
            minWidth: 18,
            height: 18,
            paddingHorizontal: 4,
            borderRadius: 9,
            backgroundColor: palette.like,
            borderWidth: 2,
            borderColor: colors(darkmode).page,
            alignItems: "center",
            justifyContent: "center",
          }}
        >
          <Text style={{ fontSize: 10, lineHeight: 12 }} className="font-bold text-white">
            {count > 9 ? "9+" : count}
          </Text>
        </View>
      )}
    </TouchableOpacity>
  );
};

export default IconBadge;
