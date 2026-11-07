import { View, Text, TouchableOpacity } from "react-native";
import React from "react";
import Icon from "react-native-vector-icons/Ionicons";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { bgColor, borderColor, iconColor, textColor } from "../utils/Utils";

type Props = {
  message?: string;
  navigation: any;
};

const ProfileNavigation = (props: Props) => {
  const { darkmode } = useSettingsContext();

  return (
    <View
      className={`flex-row items-center h-14 px-3 border-b ${bgColor(
        darkmode
      )} ${borderColor(darkmode)}`}
    >
      <TouchableOpacity
        onPress={() => props.navigation.goBack()}
        hitSlop={10}
        className="w-10 h-10 items-center justify-center"
      >
        <Icon name="arrow-back" size={22} color={iconColor(darkmode)} />
      </TouchableOpacity>
      <Text
        numberOfLines={1}
        className={`flex-1 text-base font-medium ml-2 ${textColor(darkmode)}`}
      >
        {props.message}
      </Text>
    </View>
  );
};

export default ProfileNavigation;
