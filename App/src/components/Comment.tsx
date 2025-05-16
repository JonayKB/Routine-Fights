import { View, Text, TouchableOpacity } from "react-native";
import React from "react";
import Picture from "./Picture";
import { Comment as CommentDomain } from "../utils/Comment";

type Props = {
  navigation: any;
  comment: CommentDomain;
};

const Comment = ({ navigation, comment }: Props) => {
  return (
    <View className="p-4 border-b border-gray-300">
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
          style="rounded-full border-2 border-[#4B0082]"
        />
      </TouchableOpacity>
      <Text className="text-lg font-bold">{comment.user?.username}</Text>
      <Text className="text-gray-700">{comment.message}</Text>
      <Text className="text-gray-700">{comment.createdAt}</Text>
    </View>
  );
};

export default Comment;
