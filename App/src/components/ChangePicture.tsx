import { View, Text, TouchableOpacity, Image } from "react-native";
import React from "react";
import { translations } from "../../translations/translation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { useImageContext } from "../contexts/ImageContextProvider";

type Props = {
  uri: string;
  setUri: React.Dispatch<React.SetStateAction<string>>;
};

const ChangePicture = ({ uri, setUri }: Props) => {
  const { language } = useSettingsContext();
  const { width } = useImageContext();

  return (
    <View className="items-center">
      <View className="w-full bg-black items-center">
        <Image
          className="justify-center my-5"
          source={{ uri: `file://${uri}` }}
          style={{ width: width === 1 ? 250 : 140, height: 250 }}
        />
        <TouchableOpacity
          onPress={() => setUri(null)}
          className="border-white border-2 rounded-lg mb-5"
        >
          <Text className="text-white font-bold text-xl text-center px-6 py-2">
            {translations[language || "en-US"].screens.UploadForm.changePicture}
          </Text>
        </TouchableOpacity>
      </View>
    </View>
  );
};

export default ChangePicture;
