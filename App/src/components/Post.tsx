import {
  View,
  TouchableOpacity,
  TouchableWithoutFeedback,
  Text,
} from "react-native";
import React, { useEffect, useState } from "react";
import { Post as PostDomain } from "../utils/Post";
import Icon from "react-native-vector-icons/Ionicons";
import Picture from "./Picture";
import { likePost, unLikePost } from "../repositories/PostRepository";

type Props = {
  post: PostDomain;
  navigation: any;
};

const Post = (props: Props) => {
  const [post, setPost] = useState<PostDomain>({} as PostDomain);

  useEffect(() => {
    setPost({ ...props.post });
  }, [props.post]);

  const handleLike = async () => {
    try {
      if (post.isLiked) {
        await unLikePost(post.id);
      } else {
        await likePost(post.id);
      }
      setPost({
        ...post,
        likes: post.isLiked ? post.likes - 1 : post.likes + 1,
        isLiked: !post.isLiked,
      });
    } catch (error) {
      console.error("Error liking post:", error);
    }
  };

  return (
    <View className="bg-[#E4D8E9] flex-row rounded-xl m-5">
      <View>
        <Picture
          image={post.image}
          size={281}
          height={485}
          style="rounded-xl"
        />
        <View className="z-index-10 -mt-16 bg-[#00000095] rounded-b-xl py-5">
          <Text className="text-white text-xl font-bold text-center">
            {post.activity?.name}
          </Text>
        </View>
      </View>
      <View className="flex-col m-3">
        <TouchableOpacity
          onPress={() =>
            props.navigation.navigate("ProfileStackNavigation", {
              screen: "Profile",
              params: { email: post.user?.email },
            })
          }
        >
          <Picture
            image={post.user?.image}
            size={53}
            style="rounded-full border-2 border-[#7B5BF2]"
          />
        </TouchableOpacity>
        <TouchableOpacity onPress={handleLike}>
          <Icon
            name={post.isLiked ? "heart" : "heart-outline"}
            size={53}
            color="#7B5BF2"
            className="mt-10"
          />
          <Text className="text-black text-center text-lg">{post.likes}</Text>
        </TouchableOpacity>
        <TouchableOpacity
          onPress={() =>
            props.navigation.navigate("Comments", { postID: post.id })
          }
        >
          <Icon name="chatbox" size={53} color="#7B5BF2" className="mt-10" />
          <Text className="text-black text-center text-lg">
            {post.comments}
          </Text>
        </TouchableOpacity>
        <Icon name="flame" size={53} color="#E4007C" className="mt-32" />
        <Text className="text-black text-center text-lg">{post.streak}</Text>
      </View>
    </View>
  );
};

export default Post;
