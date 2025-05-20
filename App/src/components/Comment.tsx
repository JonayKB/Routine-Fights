import { View, Text, TouchableOpacity } from "react-native";
import React from "react";
import Picture from "./Picture";
import { Comment as CommentDomain } from "../utils/Comment";
import DateFormatString from "./DateFormatString";
import { borderColor, cardBgColor, textColor } from "../utils/Utils";
import { useSettingsContext } from "../contexts/SettingsContextProvider";

type Props = {
  navigation: any;
  comment: CommentDomain;
};

const Comment = ({ navigation, comment }: Props) => {
  const { darkmode } = useSettingsContext();

  return (
    <View
      className={`${cardBgColor(
        darkmode
      )} mt-5 w-11/12 mx-auto rounded-xl p-2 flex-row`}
    >
      <TouchableOpacity
        onPress={() =>
          navigation.navigate("ProfileStackNavigation", {
            screen: "Profile",
            params: { email: comment.user.email },
          })
        }
      >
        <Picture
          image={comment.user?.image}
          size={80}
          style={`rounded-full border-2 ${borderColor(darkmode)}`}
        />
      </TouchableOpacity>

      <View className="ml-4 flex-1">
        <Text className={`${textColor(darkmode)} text-2xl font-bold`}>
          {comment.user?.username}
        </Text>
        <Text className={`${textColor(darkmode)} text-lg mt-2`}>
          {comment.message}
        </Text>
        <View className="mt-2">
          <DateFormatString date={comment.createdAt} />
        </View>
      </View>
    </View>
  );
};

export default Comment;
