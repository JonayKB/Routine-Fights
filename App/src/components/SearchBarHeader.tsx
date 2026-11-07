import React from "react";
import { TextInput, TouchableOpacity, View } from "react-native";
import Icon from "react-native-vector-icons/Ionicons";
import { translations } from "../../translations/translation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import {
  bgColor,
  borderColor,
  iconColor,
  raisedBgColor,
  textColor,
} from "../utils/Utils";
import { colors } from "../styles/theme";

type Props = {
  navigation: any;
  searchFunction: (text: string) => void;
};

const SearchBarHeader = (props: Props) => {
  const { language, darkmode } = useSettingsContext();

  return (
    <View
      className={`flex-row items-center h-16 px-3 border-b ${bgColor(
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
      <View
        className={`flex-1 flex-row items-center h-11 ml-1 px-3 rounded-xl border ${raisedBgColor(
          darkmode
        )} ${borderColor(darkmode)}`}
      >
        <Icon name="search" size={18} color={iconColor(darkmode)} />
        <TextInput
          autoFocus
          placeholder={translations[language || "en-EN"].screens.Home.search}
          placeholderTextColor={colors(darkmode).textMuted}
          className={`flex-1 ml-2 py-0 text-base ${textColor(darkmode)}`}
          onChangeText={(text) => props.searchFunction(text)}
        />
      </View>
    </View>
  );
};

export default SearchBarHeader;
