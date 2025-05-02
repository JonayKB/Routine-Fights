import { View, Text, TouchableOpacity, Image } from "react-native";
import React from "react";
import { translations } from "../../translations/translation";
import { useLanguageContext } from "../contexts/SettingsContextProvider";

type Props = {
    uri: string;
    setUri: React.Dispatch<React.SetStateAction<string>>;
};

const ChangePicture = ({uri, setUri}: Props) => {
  const { language } = useLanguageContext();
  return (
    <View className="items-center">
      <View className="rounded-lg w-full bg-black items-center">
        <Image
          className="justify-center my-5"
          source={{ uri: `file://${uri}` }}
          style={{ width: 140, height: 250 }}
        />
        <TouchableOpacity
          onPress={() => setUri(null)}
          className="border-white border-2 rounded-lg mb-5"
        >
          <Text className="text-white font-bold text-xl text-center px-6 py-2">
            {translations[language || "en-EN"].screens.UploadForm.changePicture}
          </Text>
        </TouchableOpacity>
      </View>
    </View>
  );
};

export default ChangePicture;
