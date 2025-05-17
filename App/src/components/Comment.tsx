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
    <View className="bg-[#F1FEFC] mt-5 w-11/12 mx-auto rounded-xl p-2 flex-row">
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
      <View className="ml-4">
        <Text className="text-black text-2xl font-bold">
          {comment.user?.username}
        </Text>
        <Text className="text-black text-lg mt-2">{comment.message}</Text>
        <View className="flex-row justify-between mt-2">
          <Text className="text-black">{comment.createdAt.slice(12, 16)}</Text>
          <Text className="text-black ml-2">
            {comment.createdAt.slice(0, 10)}
          </Text>
        </View>
      </View>
    </View>
  );
};

export default Comment;
