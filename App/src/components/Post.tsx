import { View, Image, Text, TouchableOpacity, TouchableWithoutFeedback } from "react-native";
import React from "react";
import { Post as PostDomain } from "../utils/Post";
import Icon from "react-native-vector-icons/Ionicons";

type Props = {
  post: PostDomain;
};

const Post = ({ post }: Props) => {
  return (
    <View className="bg-[#E4D8E9] flex-row rounded-xl m-5">
      <TouchableWithoutFeedback>
        <Image
          source={{ uri: post.image }}
          height={500}
          width={281}
          className="rounded-xl"
        />
      </TouchableWithoutFeedback>
      <View className="flex-col m-3">
        <TouchableOpacity>
          <Image
            source={{ uri: post.image }}
            height={53}
            width={53}
            className="rounded-full border-2 border-[#7B5BF2]"
          />
        </TouchableOpacity>
        <TouchableOpacity>
          <Icon
            name="heart-outline"
            size={53}
            color="#7B5BF2"
            className="mt-10"
          />
        </TouchableOpacity>
        <Text className="text-black text-center text-lg">{post.likes}</Text>
        <TouchableOpacity>
          <Icon name="chatbox" size={53} color="#7B5BF2" className="mt-10" />
        </TouchableOpacity>
        <Text className="text-black text-center text-lg">{post.likes}</Text>
        <Icon name="flame" size={53} color="#E4007C" className="mt-32" />
        <Text className="text-black text-center text-lg">{post.likes}</Text>
      </View>
    </View>
  );
};

export default Post;
