import {
  Text,
  View,
  ScrollView,
  TouchableOpacity,
  FlatList,
  RefreshControl,
  TouchableWithoutFeedback,
  Alert,
} from "react-native";
import React, { useEffect, useState } from "react";
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

type Props = NativeStackScreenProps<ProfileStackProps, "Profile">;

const ProfileScreen = ({ navigation, route }: Props) => {
  const [user, setUser] = useState<UserOut>({} as UserOut);
  const [followers, setFollowers] = useState<string>("");
  const [following, setFollowing] = useState<string>("");
  const [load, setLoad] = useState<boolean>(false);
  const [profileLoad, setProfileLoad] = useState<boolean>(false);
  const [selectedPost, setSelectedPost] = useState<PostDomain>(null);
  const [ownUser, setOwnUser] = useState<boolean>(true);
  const { language, darkmode } = useSettingsContext();
  const [posts, setPosts] = useState<PostDomain[]>([]);

  useEffect(() => {
    const fetchUser = async () => {
      try {
        let user: UserOut;
        if (route.params?.email) {
          user = await getUser(route.params?.email);
        } else {
          user = await getOwnUser();
        }
        let ownUser = await getOwnUser();
        user.id === ownUser.id ? setOwnUser(true) : setOwnUser(false);

        setUser(user);
        setFollowers(convertQuantityToString(user.followers));
        setFollowing(convertQuantityToString(user.following));
      } catch (error) {
        console.error("Error fetching user:", error);
        Alert.alert("Error", error.response.data);
      }
    };

    fetchUser();
  }, [route.params?.email, profileLoad === true]);

  const reload = () => {
    setLoad(true);
    setTimeout(() => {
      setLoad(false);
    }, 1000);
  };

  const profileReload = () => {
    setProfileLoad(true);
    setTimeout(() => {
      setProfileLoad(false);
    }, 1000);
  };

  return (
    <View className={`flex-1 bg-[#${darkmode ? "2C2C2C" : "CCCCCC"}]`}>
      {selectedPost && (
        <View className="absolute w-full h-full z-10">
          <TouchableWithoutFeedback onPress={() => setSelectedPost(null)}>
            <View className="flex-1 bg-black opacity-80 w-full h-full absolute"></View>
          </TouchableWithoutFeedback>
          <View className="absolute justify-center h-full">
            <Post post={selectedPost} />
          </View>
        </View>
      )}

      {!!route.params?.email && (
        <ProfileNavigation navigation={navigation} message={user.username} />
      )}

      <ScrollView
        className="flex-1"
        refreshControl={
          <RefreshControl refreshing={profileLoad} onRefresh={profileReload} />
        }
      >
        <View className="bg-[#E4D8E9] flex-row border-b-2 border-[#4B0082]">
          {!route.params?.email && (
            <TouchableOpacity
              onPress={() => navigation.navigate("Settings")}
              className="absolute top-5 right-5 z-10"
            >
              <Icon name="settings" size={28} color="black" />
            </TouchableOpacity>
          )}
          <View className="m-5 items-center">
            <Picture
              image={user.image}
              size={103}
              style="rounded-full border-2 border-[#4B0082] filter invert"
            />
            {/* <View
            style={{ backgroundColor: "rgba(0, 0, 0, 0.7)" }}
            className="-mt-4 w-10 rounded-xl justify-center items-center border-2 border-white"
          >
            <Text className="text-center text-white">
              {Math.floor(Math.random() * 300)}
            </Text>
          </View> */}
          </View>
          <View className="mt-5">
            <Text className="text-black text-4xl font-bold">
              {user?.username}
            </Text>
            <FollowCount
              followers={followers}
              following={following}
              email={user.email}
              navigation={navigation}
            />
            {!ownUser && (
              <TouchableOpacity
                className="flex-1 border-[#E4007C] border-2 rounded-lg mt-4 mb-2"
                onPress={async () =>
                  user.isFollowing
                    ? await unfollowUser(user.email)
                    : await followUser(user.email)
                }
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
      </ScrollView>

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
      />
    </View>
  );
};

export default ProfileScreen;
