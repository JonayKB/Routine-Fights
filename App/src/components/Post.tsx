import { View, TouchableOpacity, Text } from "react-native";
import React, { useEffect, useState } from "react";
import { Post as PostDomain } from "../utils/Post";
import Icon from "react-native-vector-icons/Ionicons";
import Picture from "./Picture";
import { likePost, unLikePost } from "../repositories/PostRepository";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { borderColor, cardBgColor, iconColor, textColor } from "../utils/Utils";

type Props = {
  post: PostDomain;
  navigation: any;
};

const Post = (props: Props) => {
  const [post, setPost] = useState<PostDomain>({} as PostDomain);
  const { darkmode } = useSettingsContext();

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
    <View
      className={`${cardBgColor(darkmode)} flex-row rounded-xl m-5`}
    >
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
            style={`rounded-full border-2 ${borderColor(darkmode)}`}
          />
        </TouchableOpacity>

        <TouchableOpacity onPress={handleLike} className="mt-10 items-center">
          <Icon
            name={post.isLiked ? "heart" : "heart-outline"}
            size={53}
            color={(() => {
              if (post.isLiked) return "#F65261";
              return `${iconColor(darkmode)}`;
            })()}
          />
          <Text
            className={`${textColor(darkmode)} text-center text-lg`}
          >
            {post.likes}
          </Text>
        </TouchableOpacity>

        <TouchableOpacity
          onPress={() =>
            props.navigation.navigate("Comments", { postID: post.id })
          }
          className="mt-10 items-center"
        >
          <Icon
            name="chatbox-outline"
            size={53}
            color={iconColor(darkmode)}
          />
          <Text
            className={`${textColor(darkmode)} text-center text-lg`}
          >
            {post.comments}
          </Text>
        </TouchableOpacity>

        <View className="mt-32 items-center">
          <Icon name="flame" size={53} color="#F65261" />
          <Text
            className={`${textColor(darkmode)} text-center text-lg`}
          >
            {post.streak}
          </Text>
        </View>
      </View>
    </View>
  );
};

export default Post;
