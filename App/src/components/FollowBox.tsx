import { View, Text, TouchableOpacity } from "react-native";
import React, { useEffect, useState } from "react";
import Icon from "react-native-vector-icons/Ionicons";
import Avatar from "./Avatar";
import { translations } from "../../translations/translation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import {
  iconColor,
  borderColor,
  convertQuantityToString,
  mutedTextColor,
  raisedBgColor,
  textColor,
} from "../utils/Utils";
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

  const t = translations[language || "en-EN"].screens.Profile;
  return (
    <TouchableOpacity
      activeOpacity={0.7}
      onPress={props.navigateFunction}
      className={`flex-row items-center px-5 py-3 border-b ${borderColor(darkmode)}`}
    >
      <Avatar image={follower.image} name={follower.username} size={44} />
      <View className="ml-3 flex-1">
        <Text numberOfLines={1} className={`text-base font-medium ${textColor(darkmode)}`}>
          {follower.username}
        </Text>
        <Text className={`text-xs mt-0.5 ${mutedTextColor(darkmode)}`}>
          {convertQuantityToString(follower.followers)} {t.followers}
          {"  ·  "}
          {convertQuantityToString(follower.following)} {t.following}
        </Text>
      </View>
      {follower.email !== email && (
        <TouchableOpacity
          activeOpacity={0.8}
          accessibilityRole="button"
          accessibilityState={{ selected: !!follower.isFollowing }}
          className={`flex-row h-9 px-3 ml-3 rounded-lg items-center justify-center ${
            follower.isFollowing
              ? `border ${borderColor(darkmode)} ${raisedBgColor(darkmode)}`
              : "bg-[#C2410C]"
          }`}
          onPress={handleFollow}
        >
          <Icon
            name={follower.isFollowing ? "checkmark" : "person-add"}
            size={15}
            color={follower.isFollowing ? iconColor(darkmode) : "#FFFFFF"}
          />
          <Text
            className={`ml-1.5 text-sm font-medium ${
              follower.isFollowing ? textColor(darkmode) : "text-white"
            }`}
          >
            {follower.isFollowing ? t.followingButton : t.followButton}
          </Text>
        </TouchableOpacity>
      )}
    </TouchableOpacity>
  );
};

export default FollowBox;
