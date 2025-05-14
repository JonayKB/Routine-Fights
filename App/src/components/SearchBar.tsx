import { View, TextInput } from "react-native";
import React from "react";
import { translations } from "../../translations/translation";
import { useLanguageContext } from "../contexts/SettingsContextProvider";
import style from "../styles/Styles.json";

type Props = {
    searchFunction: (text: string) => void;
};

const SearchBar = (props: Props) => {
  const { language } = useLanguageContext();

  return (
    <View className="justify-center items-center">
      <TextInput
        placeholder={translations[language || "en-EN"].screens.Home.search}
        placeholderTextColor="#4B0082"
        className="border-[#4B0082] border-2 rounded-xl bg-white text-2xl w-11/12 my-5 pl-3 text-black"
        onChangeText={(text) => props.searchFunction(text)}
      />
    </View>
  );
};

export default SearchBar;
