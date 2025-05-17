import { View, Text, TouchableOpacity } from "react-native";
import React from "react";
import { Post } from "../utils/Post";
import Picture from "./Picture";

type Props = {
  item: Post;
  method: (item: Post) => void;
};

const ProfilePost = ({ item, method }: Props) => {
  return (
    <TouchableOpacity
      onPress={() => method(item)}
      className="m-2 rounded-xl shadow-md shadow-black"
    >
      <Picture image={item.image} size={117} height={210} style="rounded-xl" />
      <View className="z-index-10 -mt-10 bg-[#00000090] rounded-b-xl py-2">
        <Text className="text-white text-xl font-bold text-center">
          {item.activity?.name}
        </Text>
      </View>
    </TouchableOpacity>
  );
};

export default ProfilePost;
