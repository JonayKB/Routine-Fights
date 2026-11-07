import {
  View,
  TouchableOpacity,
  Text,
  Dimensions,
  Pressable,
  Animated,
  Easing,
  Vibration,
} from "react-native";
import React, { useEffect, useRef, useState } from "react";
import { Post as PostDomain } from "../utils/Post";
import Icon from "react-native-vector-icons/Ionicons";
import Picture from "./Picture";
import Avatar from "./Avatar";
import StreakFlame, { inkFor, tierFor } from "./StreakFlame";
import { likePost, unLikePost } from "../repositories/PostRepository";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import {
  borderColor,
  cardBgColor,
  iconColor,
  mutedTextColor,
  secondaryTextColor,
  textColor,
} from "../utils/Utils";
import { fonts, palette } from "../styles/theme";

type Props = {
  post: PostDomain;
  navigation: any;
};

const CARD_MARGIN = 20;

const Post = (props: Props) => {
  const [post, setPost] = useState<PostDomain>({} as PostDomain);
  const { darkmode, lefthand } = useSettingsContext();
  const photoWidth = Dimensions.get("window").width - CARD_MARGIN * 2 - 2;

  useEffect(() => {
    setPost({ ...props.post });
  }, [props.post]);

  const lastTap = useRef(0);
  const burst = useRef(new Animated.Value(0)).current;

  const showBurst = () => {
    burst.setValue(0);
    Animated.timing(burst, {
      toValue: 1,
      duration: 700,
      easing: Easing.out(Easing.cubic),
      useNativeDriver: true,
    }).start();
  };

  // Double-tap the photo to like it, like most photo apps.
  const onPhotoPress = () => {
    const now = Date.now();
    if (now - lastTap.current < 280) {
      lastTap.current = 0;
      showBurst();
      Vibration.vibrate(10);
      if (!post.isLiked) handleLike();
    } else {
      lastTap.current = now;
    }
  };

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

  const openProfile = () =>
    props.navigation.navigate("ProfileStackNavigation", {
      screen: "Profile",
      params: { email: post.user?.email },
    });

  const streak = post.streak ?? 0;
  const tier = tierFor(streak);
  const displayName = post.user?.username ?? post.user?.email?.split("@")[0];

  return (
    <View
      className={`mx-5 mb-5 rounded-2xl border overflow-hidden ${cardBgColor(
        darkmode
      )} ${borderColor(darkmode)}`}
    >
      <View className="flex-row items-center px-4 py-3">
        <TouchableOpacity onPress={openProfile} activeOpacity={0.7}>
          <Avatar image={post.user?.image} name={displayName} size={36} />
        </TouchableOpacity>

        <TouchableOpacity
          onPress={openProfile}
          activeOpacity={0.7}
          className="flex-1 ml-3"
        >
          <Text
            numberOfLines={1}
            className={`text-sm font-medium ${textColor(darkmode)}`}
          >
            {displayName}
          </Text>
          <Text numberOfLines={1} className={`text-xs ${mutedTextColor(darkmode)}`}>
            {post.activity?.name}
          </Text>
        </TouchableOpacity>

        {streak > 0 && (
          <View className="flex-row items-center">
            <StreakFlame streak={streak} size={14} glow={false} />
            <Text
              style={{ fontFamily: fonts.displayMedium, color: inkFor(tier, darkmode) }}
              className="text-sm ml-1"
            >
              {streak}
            </Text>
          </View>
        )}
      </View>

      <Pressable onPress={onPhotoPress}>
        <Picture
          image={post.image}
          size={photoWidth}
          height={Math.round(photoWidth * 1.25)}
          style=""
        />
        <Animated.View
          pointerEvents="none"
          style={{
            position: "absolute",
            top: 0,
            bottom: 0,
            left: 0,
            right: 0,
            alignItems: "center",
            justifyContent: "center",
            opacity: burst.interpolate({ inputRange: [0, 0.15, 0.7, 1], outputRange: [0, 1, 1, 0] }),
            transform: [
              { scale: burst.interpolate({ inputRange: [0, 0.2, 1], outputRange: [0.6, 1.1, 1] }) },
            ],
          }}
        >
          <Icon
            name="heart"
            size={96}
            color="#FFFFFF"
            style={{ textShadowColor: "rgba(0,0,0,0.35)", textShadowRadius: 16 }}
          />
        </Animated.View>
      </Pressable>

      <View
        className={`flex-row items-center px-3 py-2 ${
          lefthand ? "justify-end" : "justify-start"
        }`}
      >
        <TouchableOpacity
          onPress={handleLike}
          activeOpacity={0.6}
          className="flex-row items-center px-2 py-2"
        >
          <Icon
            name={post.isLiked ? "heart" : "heart-outline"}
            size={22}
            color={post.isLiked ? palette.like : iconColor(darkmode)}
          />
          <Text className={`ml-1.5 text-sm ${secondaryTextColor(darkmode)}`}>
            {post.likes ?? 0}
          </Text>
        </TouchableOpacity>

        <TouchableOpacity
          onPress={() =>
            props.navigation.navigate("Comments", { postID: post.id })
          }
          activeOpacity={0.6}
          className="flex-row items-center px-2 py-2 ml-2"
        >
          <Icon name="chatbubble-outline" size={21} color={iconColor(darkmode)} />
          <Text className={`ml-1.5 text-sm ${secondaryTextColor(darkmode)}`}>
            {post.comments ?? 0}
          </Text>
        </TouchableOpacity>
      </View>
    </View>
  );
};

export default Post;
