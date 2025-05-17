import { View, Text, TouchableOpacity } from "react-native";
import React from "react";
import { Post } from "../utils/Post";
import Picture from "./Picture";

type Props = {
  item: {
    id: string;
    image: string;
  };
  method: (item: Post) => void;
};

const ProfilePost = ({ item, method }: Props) => {
  return (
    <TouchableOpacity
      onPress={() => method(item as Post)}
      className="m-2 rounded-xl shadow-md shadow-black"
    >
      <Picture image={item.image} size={117} height={210} style="rounded-xl" />
      <View className="z-index-10 -mt-8 bg-[#00000070] rounded-xl">
        <Text className="text-white text-xl font-bold text-center">
          Walking
        </Text>
      </View>
    </TouchableOpacity>
  );
};

export default ProfilePost;
