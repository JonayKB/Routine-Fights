import { View, Text, TouchableOpacity } from "react-native";
import React from "react";
import Icon from "react-native-vector-icons/Ionicons";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { borderColor, cardBgColor, iconColor } from "../utils/Utils";

type Props = {
  message?: string;
  navigation: any;
};

const ProfileNavigation = (props: Props) => {
  const { darkmode } = useSettingsContext();

  return (
    <View
      className={`flex-row ${cardBgColor(darkmode)} border-b-2 ${borderColor(
        darkmode
      )} p-5`}
    >
      <TouchableOpacity onPress={() => props.navigation.goBack()}>
        <Icon name="arrow-back" size={30} color={iconColor(darkmode)} />
      </TouchableOpacity>
      <Text
        className={`${
          darkmode ? "text-[#B28DFF]" : "text-[#7D3C98]"
        } font-bold text-2xl ml-5`}
      >
        {props.message}
      </Text>
    </View>
  );
};

export default ProfileNavigation;
