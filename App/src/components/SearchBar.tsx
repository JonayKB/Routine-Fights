import { View, TextInput } from "react-native";
import React from "react";
import { translations } from "../../translations/translation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import Icon from "react-native-vector-icons/Ionicons";
import { borderColor, iconColor, raisedBgColor, textColor } from "../utils/Utils";
import { colors } from "../styles/theme";

type Props = {
  searchFunction: (text: string) => void;
};

const SearchBar = (props: Props) => {
  const { language, darkmode } = useSettingsContext();

  return (
    <View className="px-5 pt-4 pb-3">
      <View
        className={`flex-row items-center h-11 px-3 rounded-xl border ${raisedBgColor(
          darkmode
        )} ${borderColor(darkmode)}`}
      >
        <Icon name="search" size={18} color={iconColor(darkmode)} />
        <TextInput
          placeholder={translations[language || "en-EN"].screens.Home.search}
          placeholderTextColor={colors(darkmode).textMuted}
          className={`flex-1 ml-2 py-0 text-base ${textColor(darkmode)}`}
          onChangeText={(text) => props.searchFunction(text)}
        />
      </View>
    </View>
  );
};

export default SearchBar;
