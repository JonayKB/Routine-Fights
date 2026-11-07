import { Text, TouchableOpacity } from "react-native";
import React from "react";
import Icon from "react-native-vector-icons/Ionicons";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { borderColor, iconColor, secondaryTextColor } from "../utils/Utils";
import { translations } from "../../translations/translation";

type Props = {
  navigateFunction: () => void;
  label?: string;
};

const AddButton = (props: Props) => {
  const { darkmode, language } = useSettingsContext();

  return (
    <TouchableOpacity
      activeOpacity={0.7}
      onPress={props.navigateFunction}
      className={`flex-row items-center justify-center mx-5 mt-1 h-12 rounded-xl border border-dashed ${borderColor(
        darkmode
      )}`}
    >
      <Icon name="add" size={18} color={iconColor(darkmode)} />
      <Text className={`ml-2 text-sm font-medium ${secondaryTextColor(darkmode)}`}>
        {props.label ??
          translations[language || "en-EN"].screens.Streaks.add}
      </Text>
    </TouchableOpacity>
  );
};

export default AddButton;
