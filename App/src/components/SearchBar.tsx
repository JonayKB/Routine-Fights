import { View, TextInput } from "react-native";
import React from "react";
import { translations } from "../../translations/translation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";

type Props = {
  searchFunction: (text: string) => void;
};

const SearchBar = (props: Props) => {
  const { language, darkmode } = useSettingsContext();

  return (
    <View className="justify-center items-center">
      <TextInput
        placeholder={translations[language || "en-EN"].screens.Home.search}
        placeholderTextColor={darkmode ? "#B28DFF" : "#4B0082"}
        className={`border-2 rounded-xl text-2xl w-11/12 my-5 pl-3 ${
          darkmode
            ? "border-[#B28DFF] bg-[#333333] text-white"
            : "border-[#4B0082] bg-white text-black"
        }`}
        onChangeText={(text) => props.searchFunction(text)}
      />
    </View>
  );
};

export default SearchBar;
