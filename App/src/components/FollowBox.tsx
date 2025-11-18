import { View, Text, TouchableOpacity } from "react-native";
import React, { useEffect, useState } from "react";
import Picture from "./Picture";
import { translations } from "../../translations/translation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { cardBgColor, convertQuantityToString } from "../utils/Utils";
import { Followers } from "../utils/User";
import { followUser, unfollowUser } from "../repositories/UserRepository";
import { useTokenContext } from "../contexts/TokenContextProvider";

type Props = {
  item: Followers;
  navigateFunction: () => void;
};

const FollowBox = (props: Props) => {
  const [follower, setFollower] = useState<Followers>({} as Followers);
  const { language, darkmode } = useSettingsContext();
  const { email } = useTokenContext();

  useEffect(() => {
    setFollower({ ...props.item });
  }, [props.item]);

  const handleFollow = async () => {
    try {
      if (follower.isFollowing) {
        await unfollowUser(follower.email);
      } else {
        await followUser(follower.email);
      }
      const updatedFollowers: number = follower.isFollowing
        ? Math.max(0, follower.followers - 1)
        : follower.followers + 1;

      setFollower({
        ...follower,
        isFollowing: !follower.isFollowing,
        followers: updatedFollowers,
      });
    } catch (error) {
      console.error("Error following/unfollowing user:", error);
    }
  };

  return (
    <View className="w-11/12 mx-auto my-3">
      <TouchableOpacity
        onPress={props.navigateFunction}
        className={`flex-row items-center rounded-2xl p-4 ${cardBgColor(
          darkmode
        )}`}
      >
        <Picture
          image={follower.image}
          size={80}
          style={`rounded-full border-2 ${
            darkmode ? "border-[#B28DFF]" : "border-[#4B0082]"
          }`}
        />
        <View className="ml-5 flex-1">
          <Text
            className={`font-bold text-2xl mb-1 ${
              darkmode ? "text-[#B28DFF]" : "text-[#4B0082]"
            }`}
          >
            {follower.username}
          </Text>
          <Text className={`${darkmode ? "text-white" : "text-[#333333]"}`}>
            {translations[language || "en-US"].screens.Profile.followers}:{" "}
            {convertQuantityToString(follower.followers)}
          </Text>
          <Text className={`${darkmode ? "text-white" : "text-[#333333]"}`}>
            {translations[language || "en-US"].screens.Profile.following}:{" "}
            {convertQuantityToString(follower.following)}
          </Text>
        </View>
        {follower.email !== email && (
          <TouchableOpacity
            className="border-2 border-[#F65261] rounded-lg px-4 py-2 ml-3"
            onPress={handleFollow}
          >
            <Text
              className={`font-bold text-base text-center ${
                darkmode ? "text-[#B28DFF]" : "text-[#4B0082]"
              }`}
            >
              {follower.isFollowing
                ? translations[language || "en-US"].screens.Profile.unfollow
                : translations[language || "en-US"].screens.Profile.follow}
            </Text>
          </TouchableOpacity>
        )}
      </TouchableOpacity>
    </View>
  );
};

export default FollowBox;
