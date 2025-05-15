import { View, Text, TouchableOpacity } from "react-native";
import React from "react";
import Picture from "./Picture";
import { translations } from "../../translations/translation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { convertQuantityToString } from "../utils/Utils";
import { Followers } from "../utils/User";

type Props = {
  item: Followers;
  following: boolean;
  followFunction: (item: Followers) => void;
  navigateFunction: () => void;
};

const FollowBox = (props: Props) => {
  const { language } = useSettingsContext();

  return (
    <View>
      <TouchableOpacity
        onPress={props.navigateFunction}
        className="items-center bg-[#F1FEFC] flex-row mt-5 w-11/12 mx-auto rounded-xl p-2"
      >
        <Picture image={props.item.image} size={80} style="rounded-full border-2 border-[#4B0082] filter invert"/>
        <View className="ml-5">
          <Text className="text-black font-bold text-2xl">
            {props.item.username}
          </Text>
          <Text className="text-black">
            {translations[language || "en-EN"].screens.Profile.followers}:{" "}
            {convertQuantityToString(props.item.followers)}
          </Text>
          <Text className="text-black">
            {translations[language || "en-EN"].screens.Profile.following}:{" "}
            {convertQuantityToString(props.item.following)}
          </Text>
        </View>
        <TouchableOpacity
          className="border-[#E4007C] border-2 rounded-lg ml-5"
          onPress={() => props.followFunction(props.item)}
        >
          <Text className="text-[#4B0082] font-bold text-xl text-center px-6 py-2">
            {props.following
              ? translations[language || "en-EN"].screens.Profile.unfollow
              : translations[language || "en-EN"].screens.Profile.follow}
          </Text>
        </TouchableOpacity>
      </TouchableOpacity>
    </View>
  );
};

export default FollowBox;
