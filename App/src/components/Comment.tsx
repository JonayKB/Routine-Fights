import { View, Text, TouchableOpacity } from "react-native";
import React from "react";
import Avatar from "./Avatar";
import { Comment as CommentDomain } from "../utils/Comment";
import { borderColor, mutedTextColor, textColor } from "../utils/Utils";
import { useSettingsContext } from "../contexts/SettingsContextProvider";

type Props = {
  navigation: any;
  comment: CommentDomain;
};

const Comment = ({ navigation, comment }: Props) => {
  const { darkmode } = useSettingsContext();

  return (
    <View className={`flex-row px-5 py-4 border-b ${borderColor(darkmode)}`}>
      <TouchableOpacity
        activeOpacity={0.7}
        onPress={() =>
          navigation.navigate("ProfileStackNavigation", {
            screen: "Profile",
            params: { email: comment.user.email },
          })
        }
      >
        <Avatar image={comment.user?.image} name={comment.user?.username} size={36} />
      </TouchableOpacity>

      <View className="ml-3 flex-1">
        <View className="flex-row items-center">
          <Text className={`text-sm font-medium ${textColor(darkmode)}`}>
            {comment.user?.username}
          </Text>
          <Text className={`text-xs ml-2 ${mutedTextColor(darkmode)}`}>
            {comment.createdAt?.slice(0, 10)}
          </Text>
        </View>
        <Text className={`text-base mt-1 ${textColor(darkmode)}`}>
          {comment.message}
        </Text>
      </View>
    </View>
  );
};

export default Comment;
