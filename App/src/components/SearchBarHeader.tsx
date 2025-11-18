import React from "react";
import { TextInput, TouchableOpacity, View } from "react-native";
import Icon from "react-native-vector-icons/Ionicons";
import { translations } from "../../translations/translation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { borderColor, cardBgColor, iconColor } from "../utils/Utils";

type Props = {
  navigation: any;
  searchFunction: (text: string) => void;
};

const SearchBarHeader = (props: Props) => {
  const { language, darkmode } = useSettingsContext();

  return (
    <View
      className={`flex-row items-center ${cardBgColor(
        darkmode
      )} border-b-2 ${borderColor(darkmode)} px-4 py-3`}
    >
      <TouchableOpacity onPress={() => props.navigation.goBack()}>
        <Icon name="arrow-back" size={28} color={iconColor(darkmode)} />
      </TouchableOpacity>
      <TextInput
        placeholder={translations[language || "en-US"].screens.Home.search}
        placeholderTextColor={darkmode ? "#B28DFF" : "#7D3C98"}
        className={`ml-4 flex-1 rounded-xl border-2 px-4 py-2 text-2xl ${
          darkmode
            ? "bg-[#1C1C1E] text-white border-[#B28DFF]"
            : "bg-white text-black border-[#7D3C98]"
        }`}
        onChangeText={(text) => props.searchFunction(text)}
      />
    </View>
  );
};

export default SearchBarHeader;
