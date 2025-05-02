import {
  Text,
  View,
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
import { Post as PostDomain } from "../utils/Post";
import Post from "../components/Post";
import Icon from "react-native-vector-icons/Ionicons";
import {
  getUser,
  getOwnUser,
  followUser,
} from "../repositories/UserRepository";
import { convertQuantityToString } from "../utils/Utils";
import ProfilePost from "../components/ProfilePost";
import FollowCount from "../components/FollowCount";
import ProfilePicture from "../components/ProfilePicture";
import ProfileNavigation from "../components/ProfileNavigation";
import { translations } from "../../translations/translation";

type Props = NativeStackScreenProps<ProfileStackProps, "Profile">;

const Profile = ({ navigation, route }: Props) => {
  const [user, setUser] = useState<UserOut>({} as UserOut);
  const [followers, setFollowers] = useState<string>("");
  const [following, setFollowing] = useState<string>("");
  const [load, setLoad] = useState<boolean>(false);
  const [selectedPost, setSelectedPost] = useState<PostDomain>(null);
  const [ownUser, setOwnUser] = useState<boolean>(true);

  useEffect(() => {
    const fetchUser = async () => {
      try {
        let user: UserOut;
        if (route.params?.id) {
          user = await getUser(route.params?.id);
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
  }, [route.params?.id]);

  const posts: PostDomain[] = [
    {
      id: "1",
      streak: 1,
      points: 10,
      createdAt: "2021-09-01",
      image: "https://picsum.photos/200/300",
      likes: 100,
    },
    {
      id: "2",
      streak: 1,
      points: 10,
      createdAt: "2021-09-01",
      image: "https://picsum.photos/200/300",
      likes: 200,
    },
    {
      id: "3",
      streak: 1,
      points: 10,
      createdAt: "2021-09-01",
      image: "https://picsum.photos/200/300",
      likes: 100,
    },
    {
      id: "4",
      streak: 1,
      points: 10,
      createdAt: "2021-09-01",
      image: "https://picsum.photos/200/300",
      likes: 100,
    },
    {
      id: "5",
      streak: 1,
      points: 10,
      createdAt: "2021-09-01",
      image: "https://picsum.photos/200/300",
      likes: 100,
    },
  ];

  return (
    <View className="flex-1">
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

      {!!route.params?.id && (
        <ProfileNavigation navigation={navigation} message={user.username} />
      )}

      <View className="bg-[#E4D8E9] flex-row border-b-2 border-[#4B0082]">
        {!route.params?.id && (
          <TouchableOpacity
            onPress={() => navigation.navigate("Settings")}
            className="absolute top-5 right-5 z-10"
          >
            <Icon name="settings" size={28} color="black" />
          </TouchableOpacity>
        )}
        <View className="m-5 items-center">
          <ProfilePicture image={user.image} size={103} />
          {/* <View
            style={{ backgroundColor: "rgba(0, 0, 0, 0.7)" }}
            className="-mt-4 w-10 rounded-xl justify-center items-center border-2 border-white"
          >
            <Text className="text-center text-white">
              {Math.floor(Math.random() * 300)}
            </Text>
          </View> */}
        </View>
        <View className="mt-5 py-4">
          <Text className="text-black text-4xl font-bold">
            {user?.username}
          </Text>
          <FollowCount
            followers={followers}
            following={following}
            email={user.email}
            navigation={navigation}
          />
          {!!route.params?.id && (
            <TouchableOpacity
              className="border-[#E4007C] border-2 rounded-lg ml-5"
              onPress={() => followUser(user.email)}
            >
              <Text className="text-[#4B0082] font-bold text-xl text-center px-6 py-2">
                {/* TODO: database method for checking if own user follows this user.
                {user.isFollowing
                  ? translations[language || "en-EN"].screens.Profile.unfollow
                  : translations[language || "en-EN"].screens.Profile.follow}
                  */}
              </Text>
            </TouchableOpacity>
          )}
        </View>
      </View>

      <FlatList
        refreshControl={
          <RefreshControl
            refreshing={load}
            onRefresh={() => {
              setLoad(true);
              setTimeout(() => {
                setLoad(false);
              }, 1000);
            }}
          />
        }
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

export default Profile;
