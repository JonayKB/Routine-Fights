import { View, TouchableOpacity, Text } from "react-native";
import React from "react";
import { launchCamera, launchImageLibrary } from "react-native-image-picker";
import Icon from "react-native-vector-icons/Ionicons";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ImageStackProps } from "../navigation/ImageStackNavigation";
import { translations } from "../../translations/translation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";

type Props = NativeStackScreenProps<ImageStackProps, "Upload">;

const Upload = ({ navigation }: Props) => {
  const { language, darkmode } = useSettingsContext();
  const baseBg = darkmode ? "bg-[#4B294F]" : "bg-[#E8E2F0]";

  return (
    <View className={`flex-1 ${baseBg} justify-center items-center px-4`}>
      <Text
        className={`text-2xl font-bold mb-8 ${
          darkmode ? "text-white" : "text-[#333333]"
        }`}
      >
        {translations[language || "en-EN"].screens.UploadForm.title ||
          "Subir Foto"}
      </Text>

      <TouchableOpacity
        className="flex-row items-center bg-white rounded-2xl p-6 mb-6 w-full"
        onPress={() =>
          launchCamera(
            { mediaType: "photo" },
            ({ assets }) =>
              assets && navigation.navigate("ImageCrop", { uri: assets[0].uri })
          )
        }
      >
        <Icon name="camera-outline" size={36} color="#4B0082" />
        <Text className="ml-4 text-lg font-semibold text-[#4B0082]">
          {translations[language || "en-EN"].screens.UploadForm.takeFromCamera}
        </Text>
      </TouchableOpacity>

      <TouchableOpacity
        className="flex-row items-center bg-white rounded-2xl p-6 w-full"
        onPress={() =>
          launchImageLibrary(
            { mediaType: "photo" },
            ({ assets }) =>
              assets && navigation.navigate("ImageCrop", { uri: assets[0].uri })
          )
        }
      >
        <Icon name="image-outline" size={36} color="#4B0082" />
        <Text className="ml-4 text-lg font-semibold text-[#4B0082]">
          {
            translations[language || "en-EN"].screens.UploadForm
              .selectFromLibrary
          }
        </Text>
      </TouchableOpacity>
    </View>
  );
};

export default Upload;
