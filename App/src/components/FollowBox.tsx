import { View, Text, TouchableOpacity } from "react-native";
import React, { useEffect, useState } from "react";
import Picture from "./Picture";
import { translations } from "../../translations/translation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { convertQuantityToString } from "../utils/Utils";
import { Followers } from "../utils/User";
import { followUser, unfollowUser } from "../repositories/UserRepository";
import { useTokenContext } from "../contexts/TokenContextProvider";

type Props = {
  item: Followers;
  navigateFunction: () => void;
};

const FollowBox = (props: Props) => {
  const [follower, setFollower] = useState<Followers>({} as Followers);
  const { language } = useSettingsContext();
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
    <View>
      <TouchableOpacity
        onPress={props.navigateFunction}
        className="items-center bg-[#F1FEFC] flex-row mt-5 w-11/12 mx-auto rounded-xl p-2"
      >
        <Picture
          image={follower.image}
          size={80}
          style="rounded-full border-2 border-[#4B0082]"
        />
        <View className="ml-5">
          <Text className="text-black font-bold text-2xl">
            {follower.username}
          </Text>
          <Text className="text-black">
            {translations[language || "en-EN"].screens.Profile.followers}:{" "}
            {convertQuantityToString(follower.followers)}
          </Text>
          <Text className="text-black">
            {translations[language || "en-EN"].screens.Profile.following}:{" "}
            {convertQuantityToString(follower.following)}
          </Text>
        </View>
        {(follower.email !== email) && (
          <TouchableOpacity
            className="border-[#E4007C] border-2 rounded-lg ml-5"
            onPress={handleFollow}
          >
            <Text className="text-[#4B0082] font-bold text-xl text-center px-6 py-2">
              {follower.isFollowing
                ? translations[language || "en-EN"].screens.Profile.unfollow
                : translations[language || "en-EN"].screens.Profile.follow}
            </Text>
          </TouchableOpacity>
        )}
      </TouchableOpacity>
    </View>
  );
};

export default FollowBox;
