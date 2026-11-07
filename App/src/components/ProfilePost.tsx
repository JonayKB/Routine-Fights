import { View, Text, TouchableOpacity, Dimensions } from "react-native";
import React from "react";
import { Post } from "../utils/Post";
import Picture from "./Picture";

type Props = {
  item: Post;
  method: (item: Post) => void;
};

const GAP = 2;
const COLUMNS = 3;

const ProfilePost = ({ item, method }: Props) => {
  const width = Math.floor(
    (Dimensions.get("window").width - GAP * 2 - GAP * 2 * COLUMNS) / COLUMNS
  );
  const height = Math.round(width * 1.25);

  return (
    <TouchableOpacity
      activeOpacity={0.85}
      onPress={() => method(item)}
      style={{ margin: GAP, width, height }}
    >
      <Picture image={item.image} size={width} height={height} style="" />
      <View className="absolute left-0 right-0 bottom-0 px-2 py-1.5 bg-[#12110FB3]">
        <Text numberOfLines={1} className="text-[11px] text-[#EFE9DE]">
          {item.activity?.name}
        </Text>
      </View>
    </TouchableOpacity>
  );
};

export default ProfilePost;
