import { View, TouchableOpacity, Text } from "react-native";
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
    <View className="bg-[#E8E2F0] flex-row rounded-xl m-5">
      <View>
        <Picture
          image={post.image}
          size={281}
          height={485}
          style="rounded-l-xl"
        />
        <View className="z-10 -mt-16 bg-[#33333395] rounded-bl-xl py-5">
          <Text className="text-white text-xl font-bold text-center">
            {post.activity?.name}
          </Text>
        </View>
      </View>

      <View className="flex-col m-3">
        {/* Avatar con borde “Deep Purple” */}
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
            style="rounded-full border-2 border-[#7D3C98]"
          />
        </TouchableOpacity>

        {/* Like */}
        <TouchableOpacity onPress={handleLike} className="mt-10 items-center">
          <Icon
            name={post.isLiked ? "heart" : "heart-outline"}
            size={53}
            color={post.isLiked ? "#F65261" : "#7D3C98"}
          />
          <Text className="text-[#333333] text-center text-lg">
            {post.likes}
          </Text>
        </TouchableOpacity>

        {/* Comment */}
        <TouchableOpacity
          onPress={() =>
            props.navigation.navigate("Comments", { postID: post.id })
          }
          className="mt-10 items-center"
        >
          <Icon name="chatbox-outline" size={53} color="#7D3C98" />
          <Text className="text-[#333333] text-center text-lg">
            {post.comments}
          </Text>
        </TouchableOpacity>

        {/* Streak / Fire */}
        <View className="mt-32 items-center">
          <Icon name="flame" size={53} color="#F65261" />
          <Text className="text-[#333333] text-center text-lg">
            {post.streak}
          </Text>
        </View>
      </View>
    </View>
  );
};

export default Post;
