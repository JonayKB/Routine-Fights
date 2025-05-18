import {
  Text,
  View,
  TouchableOpacity,
  FlatList,
  RefreshControl,
  TouchableWithoutFeedback,
  Alert,
} from "react-native";
import React, { useEffect, useRef, useState } from "react";
import { UserOut } from "../utils/User";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ProfileStackProps } from "../navigation/ProfileStackNavigation";
import Icon from "react-native-vector-icons/Ionicons";
import {
  getUser,
  getOwnUser,
  followUser,
  unfollowUser,
} from "../repositories/UserRepository";
import { convertQuantityToString } from "../utils/Utils";
import ProfilePost from "../components/ProfilePost";
import FollowCount from "../components/FollowCount";
import Picture from "../components/Picture";
import ProfileNavigation from "../components/ProfileNavigation";
import { translations } from "../../translations/translation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import Post from "../components/Post";
import { Post as PostDomain } from "../utils/Post";
import { getUserPosts } from "../repositories/PostRepository";
import { useTokenContext } from "../contexts/TokenContextProvider";
import { useImageContext } from "../contexts/ImageContextProvider";
import { Badge } from "../utils/Badge";
import { getBadgesByEmail } from "../repositories/BadgeRepository";

type Props = NativeStackScreenProps<ProfileStackProps, "Profile">;

const ProfileScreen = ({ navigation, route }: Props) => {
  const [user, setUser] = useState<UserOut>({} as UserOut);
  const [followers, setFollowers] = useState<string>("");
  const [following, setFollowing] = useState<string>("");
  const [load, setLoad] = useState<boolean>(false);
  const [isLoadingMore, setIsLoadingMore] = useState<boolean>(false);
  const [selectedPost, setSelectedPost] = useState<PostDomain>(null);
  const { language, darkmode } = useSettingsContext();
  const { email } = useTokenContext();
  const [posts, setPosts] = useState<PostDomain[]>([]);
  const lastDate = useRef<string>(new Date().toISOString().slice(0, 19));
  const { uri } = useImageContext();
  const [badges, setBadges] = useState<Badge[]>([]);

  useEffect(() => {
    fetchUser();
    fetchPosts();
    fetchBadges();
  }, [route.params?.email, uri]);

  useEffect(() => {
    if (load || isLoadingMore) {
      fetchUser();
      fetchPosts();
    }
  }, [load, isLoadingMore]);

  const fetchUser = async () => {
    try {
      let user: UserOut;
      if (route.params?.email) {
        user = await getUser(route.params?.email);
      } else {
        user = await getOwnUser();
      }

      setUser(user);
      setFollowers(convertQuantityToString(user.followers));
      setFollowing(convertQuantityToString(user.following));
    } catch (error) {
      console.error("Error fetching user:", error);
      Alert.alert("Error", error.response.data);
    } finally {
      setLoad(false);
    }
  };

  const fetchPosts = async () => {
    try {
      const response = await getUserPosts(lastDate.current, user.id);
      setPosts(isLoadingMore ? [...posts, ...response] : response);
    } catch (error) {
      console.error("Error fetching posts:", error);
    } finally {
      setLoad(false);
      setIsLoadingMore(false);
    }
  };

  const fetchBadges = async () => {
    try {
      const response = await getBadgesByEmail(user.email);
      console.log(JSON.stringify(response));
      setBadges(response);
    } catch (error) {
      console.error("Error fetching badges:", error);
    } finally {
      setLoad(false);
    }
  };

  const reload = () => {
    lastDate.current = new Date().toISOString().slice(0, 19);
    setLoad(true);
  };

  const loadMore = () => {
    if (isLoadingMore || posts.length === 0) return;
    lastDate.current = posts[posts.length - 1].createdAt;
    setIsLoadingMore(true);
  };

  const handleFollow = async () => {
    try {
      if (user.isFollowing) {
        await unfollowUser(user.email);
      } else {
        await followUser(user.email);
      }
      setUser({
        ...user,
        isFollowing: !user.isFollowing,
        followers: user.isFollowing ? user.followers - 1 : user.followers + 1,
      });

      const updatedFollowers: number = user.isFollowing
        ? Math.max(0, user.followers - 1)
        : user.followers + 1;
      setFollowers(convertQuantityToString(updatedFollowers));
    } catch (error) {
      console.error("Error following user:", error);
      Alert.alert("Error", error.response.data);
    }
  };

  return (
    <View className={`flex-1 ${darkmode ? "bg-[#333333]" : "bg-[#FCFCFC]"}`}>
      {selectedPost && (
        <View className="absolute w-full h-full z-10">
          <TouchableWithoutFeedback onPress={() => setSelectedPost(null)}>
            <View className="flex-1 bg-black opacity-80 w-full h-full absolute"></View>
          </TouchableWithoutFeedback>
          <View className="absolute justify-center h-full">
            <Post post={selectedPost} navigation={navigation} />
          </View>
        </View>
      )}

      {!!route.params?.email && (
        <ProfileNavigation navigation={navigation} message={user.username} />
      )}
      <View className="bg-[#E4D8E9] flex-row border-b-2 border-[#4B0082]">
        {!route.params?.email && (
          <TouchableOpacity
            onPress={() => navigation.navigate("Settings")}
            className="absolute top-5 right-5 z-10"
          >
            <Icon name="settings" size={28} color="black" />
          </TouchableOpacity>
        )}
        <TouchableOpacity
          className="m-5 items-center"
          onPress={() =>
            navigation.navigate("ProfilePictureScreen", { email: user.email })
          }
        >
          <Picture
            image={user?.image}
            size={103}
            style="rounded-full border-2 border-[#4B0082]"
          />
          {/* <View
            style={{ backgroundColor: "rgba(0, 0, 0, 0.7)" }}
            className="-mt-4 w-10 rounded-xl justify-center items-center border-2 border-white"
          >
            <Text className="text-center text-white">
              {Math.floor(Math.random() * 300)}
            </Text>
          </View> */}
        </TouchableOpacity>
        <View className="mt-5">
          <Text className="text-[#333333] text-4xl font-bold">
            {user?.username}
          </Text>
          <FollowCount
            followers={followers}
            following={following}
            email={user.email}
            navigation={navigation}
          />
          {user.email !== email && (
            <TouchableOpacity
              className="fImagelex-1 border-[#E4007C] border-2 rounded-lg mt-4 mb-2"
              onPress={handleFollow}
            >
              <Text className="text-[#4B0082] font-bold text-xl text-center px-6 py-2">
                {user.isFollowing
                  ? translations[language || "en-EN"].screens.Profile.unfollow
                  : translations[language || "en-EN"].screens.Profile.follow}
              </Text>
            </TouchableOpacity>
          )}
        </View>
      </View>
      <FlatList
        style={{ height: 100, width: "100%", flexGrow: 0 }}
        className="border-b-2 border-[#4B0082] bg-white"
        horizontal
        data={badges}
        renderItem={({ item }) => {
          return (
            <Picture image={item.image} size={72} style="rounded-full mt-4 ml-5" />
          );
        }}
      />

      <FlatList
        refreshControl={<RefreshControl refreshing={load} onRefresh={reload} />}
        style={{ width: "100%" }}
        data={posts}
        renderItem={({ item }) => {
          return (
            <ProfilePost
              item={item}
              method={(item: PostDomain) => {
                setSelectedPost(item);
              }}
            />
          );
        }}
        keyExtractor={(item) => item.id}
        numColumns={3}
        onEndReached={loadMore}
        onEndReachedThreshold={0.5}
      />
    </View>
  );
};

export default ProfileScreen;
