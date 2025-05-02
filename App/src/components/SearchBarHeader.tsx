import React from "react";
import { TextInput, TouchableOpacity, View } from "react-native";
import Icon from "react-native-vector-icons/Ionicons";
import { translations } from "../../translations/translation";
import { useLanguageContext } from "../contexts/SettingsContextProvider";

type Props = {
  navigation: any;
  searchFunction: (text: string) => void;
};

const SearchBarHeader = (props: Props) => {
  const { language } = useLanguageContext();
  
  return (
    <View className="flex-row bg-[#F1FEFC] border-b-2 border-[#4B0082] p-5 items-center">
      <TouchableOpacity onPress={() => props.navigation.goBack()}>
        <Icon name="arrow-back" size={30} color="#4B0082" />
      </TouchableOpacity>
      <TextInput
        className="flex-1 bg-[#4B0082] rounded-xl p-3 ml-2"
        placeholder={translations[language].screens.Home.search}
        onChangeText={props.searchFunction}
      />
    </View>
  );
};

export default SearchBarHeader;
