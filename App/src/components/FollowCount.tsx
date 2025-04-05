import { View, Text, TouchableOpacity } from "react-native";
import React from "react";
import { translations } from "../../translations/translation";
import { useLanguageContext } from "../contexts/LanguageContextProvider";

type Props = {
  followers: string;
  following: string;
  email: string;
  navigation: {
    navigate: (screen: string, params: object) => void;
  };
};

const FollowCount = (props: Props) => {
  const { language } = useLanguageContext();

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
          {translations[language || 'en-EN'].screens.Profile.followers}: {props.followers}
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
          {translations[language || 'en-EN'].screens.Profile.following}: {props.following}
        </Text>
      </TouchableOpacity>
    </View>
  );
};

export default FollowCount;
