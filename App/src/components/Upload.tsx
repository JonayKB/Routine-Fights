import { View, TouchableOpacity, Text } from "react-native";
import React from "react";
import { launchCamera, launchImageLibrary } from "react-native-image-picker";
import Icon from "react-native-vector-icons/Ionicons";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ImageStackProps } from "../navigation/ImageStackNavigation";
import { translations } from "../../translations/translation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import {
  borderColor,
  cardBgColor,
  iconColor,
  raisedBgColor,
  textColor,
} from "../utils/Utils";

type Props = NativeStackScreenProps<ImageStackProps, "Upload">;

const Upload = ({ navigation }: Props) => {
  const { language, darkmode } = useSettingsContext();

  const t = translations[language || "en-EN"].screens.UploadForm;
  const pick = (source: "camera" | "library") => {
    const launch = source === "camera" ? launchCamera : launchImageLibrary;
    launch(
      { mediaType: "photo" },
      ({ assets }) =>
        assets && navigation.navigate("ImageCrop", { uri: assets[0].uri })
    );
  };

  const Option = ({ icon, label, source }: { icon: string; label: string; source: "camera" | "library" }) => (
    <TouchableOpacity
      activeOpacity={0.7}
      onPress={() => pick(source)}
      className={`flex-1 items-center justify-center py-6 mx-1.5 rounded-xl border ${raisedBgColor(
        darkmode
      )} ${borderColor(darkmode)}`}
    >
      <Icon name={icon} size={24} color={iconColor(darkmode)} />
      <Text
        className={`mt-3 text-sm font-medium text-center px-2 ${textColor(darkmode)}`}
      >
        {label}
      </Text>
    </TouchableOpacity>
  );

  return (
    <View className={`flex-1 ${cardBgColor(darkmode)} justify-center px-4`}>
      <View className="flex-row">
        <Option icon="camera-outline" label={t.takeFromCamera} source="camera" />
        <Option icon="images-outline" label={t.selectFromLibrary} source="library" />
      </View>
    </View>
  );
};

export default Upload;
