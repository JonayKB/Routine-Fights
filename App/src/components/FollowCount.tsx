import { View, Text, TouchableOpacity } from "react-native";
import React from "react";
import { translations } from "../../translations/translation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { mutedTextColor, textColor } from "../utils/Utils";
import { fonts } from "../styles/theme";

type Props = {
  followers: string;
  following: string;
  email: string;
  navigation: {
    navigate: (screen: string, params: object) => void;
  };
};

const FollowCount = (props: Props) => {
  const { language, darkmode } = useSettingsContext();
  const t = translations[language || "en-EN"].screens.Profile;

  const Stat = ({ value, label, type }: { value: string; label: string; type: string }) => (
    <TouchableOpacity
      activeOpacity={0.6}
      className="mr-6"
      onPress={() =>
        props.navigation.navigate("FollowList", { email: props.email, type })
      }
    >
      <Text style={{ fontFamily: fonts.displayMedium }} className={`text-lg ${textColor(darkmode)}`}>
        {value}
      </Text>
      <Text className={`text-xs ${mutedTextColor(darkmode)}`}>{label}</Text>
    </TouchableOpacity>
  );

  return (
    <View className="flex-row mt-3">
      <Stat value={props.followers} label={t.followers} type="followers" />
      <Stat value={props.following} label={t.following} type="following" />
    </View>
  );
};

export default FollowCount;
