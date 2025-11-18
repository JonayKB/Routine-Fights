import { View, Text, TouchableOpacity } from "react-native";
import React from "react";
import { translations } from "../../translations/translation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";

type Props = {
  followers: string;
  following: string;
  email: string;
  navigation: {
    navigate: (screen: string, params: object) => void;
  };
};

const FollowCount = (props: Props) => {
  const { language } = useSettingsContext();

  return (
    <View className="flex-1 flex-col">
      <TouchableOpacity
        onPress={() =>
          props.navigation.navigate("FollowList", {
            email: props.email,
            type: "followers",
          })
        }
      >
        <Text className="text-black text-lg">
          {translations[language || 'en-US'].screens.Profile.followers}: {props.followers}
        </Text>
      </TouchableOpacity>
      <TouchableOpacity
        onPress={() =>
          props.navigation.navigate("FollowList", {
            email: props.email,
            type: "following",
          })
        }
      >
        <Text className="text-black text-lg">
          {translations[language || 'en-US'].screens.Profile.following}: {props.following}
        </Text>
      </TouchableOpacity>
    </View>
  );
};

export default FollowCount;
