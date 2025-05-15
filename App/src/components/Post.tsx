import {
  View,
  Image,
  TouchableOpacity,
  TouchableWithoutFeedback,
} from "react-native";
import React from "react";
import { Post as PostDomain } from "../utils/Post";
import Icon from "react-native-vector-icons/Ionicons";
import Picture from "./Picture";

type Props = {
  post: PostDomain;
};

const Post = ({ post }: Props) => {
  return (
    <View className="bg-[#E4D8E9] flex-row rounded-xl m-5">
      <TouchableWithoutFeedback>
        <Picture
          image={post.image}
          size={281}
          height={500}
          style="rounded-xl"
        />
      </TouchableWithoutFeedback>
      <View className="flex-col m-3">
        <TouchableOpacity>
          <Picture
            image={post.user?.image}
            size={53}
            style="rounded-full border-2 border-[#7B5BF2]"
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
        {/* <Text className="text-black text-center text-lg">{post.likes}</Text> */}
        <TouchableOpacity>
          <Icon name="chatbox" size={53} color="#7B5BF2" className="mt-10" />
        </TouchableOpacity>
        {/* <Text className="text-black text-center text-lg">{post.likes}</Text> */}
        <Icon name="flame" size={53} color="#E4007C" className="mt-32" />
        {/* <Text className="text-black text-center text-lg">{post.likes}</Text> */}
      </View>
    </View>
  );
};

export default Post;
