import { View, Text, TouchableOpacity } from "react-native";
import React from "react";
import ProfilePicture from "./ProfilePicture";
import { translations } from "../../translations/translation";
import { useLanguageContext } from "../contexts/SettingsContextProvider";
import { convertQuantityToString } from "../utils/Utils";
import { followUser } from "../repositories/UserRepository";

type Props = {
  navigation: any;
  item: {
    id: string;
    image: string;
    username: string;
    followers: number;
    following: number;
  };
};

const FollowBox = ({ navigation, item }: Props) => {
  const { language } = useLanguageContext();

  return (
    <View>
      <TouchableOpacity
        onPress={() => navigation.navigate("Profile", { id: item.id })}
        className="items-center bg-[#F1FEFC] flex-row mt-5 w-11/12 mx-auto rounded-xl p-2"
      >
        <ProfilePicture image={item.image} size={80} />
        <View className="ml-5">
          <Text className="text-black font-bold text-2xl">{item.username}</Text>
          <Text className="text-black">
            {translations[language || "en-EN"].screens.Profile.followers}:{" "}
            {convertQuantityToString(item.followers)}
          </Text>
          <Text className="text-black">
            {translations[language || "en-EN"].screens.Profile.following}:{" "}
            {convertQuantityToString(item.following)}
          </Text>
        </View>
        <TouchableOpacity className="border-[#E4007C] border-2 rounded-lg ml-5">
          <Text className="text-[#4B0082] font-bold text-xl text-center px-6 py-2">
            {translations[language || "en-EN"].screens.Profile.follow}
          </Text>
        </TouchableOpacity>
      </TouchableOpacity>
    </View>
  );
};

export default FollowBox;
