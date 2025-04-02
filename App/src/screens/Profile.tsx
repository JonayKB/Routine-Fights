import {
  Image,
  Text,
  View,
  TouchableOpacity,
  FlatList,
  RefreshControl,
  TouchableWithoutFeedback,
  Alert,
} from "react-native";
import React, { useEffect, useState } from "react";
import { UserOut } from '../utils/User';
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ProfileStackProps } from "../navigation/ProfileStackNavigation";
import { Post as PostDomain } from "../utils/Post";
import Post from "../components/Post";
import Icon from "react-native-vector-icons/Ionicons";
import { getUser, getOwnUser } from "../services/ProfileService";
import { convertQuantityToString, uri } from "../utils/Utils";
import { getImage } from "../services/ImageService";

type Props = NativeStackScreenProps<ProfileStackProps, "Profile">;

const Profile = ({ navigation, route }: Props) => {
  const [user, setUser] = useState<UserOut>({} as UserOut);
  const [followers, setFollowers] = useState<string>("");
  const [following, setFollowing] = useState<string>("");
  const [load, setLoad] = useState<boolean>(false);
  const [selectedPost, setSelectedPost] = useState<PostDomain>(null);
  const [image, setImage] = useState<string>(null);

  useEffect(() => {
    const fetchUser = async () => {
      let user: UserOut = null;
      try {
        if (route.params?.id) {
          user = await getUser(route.params?.id);
        } else {
          user = await getOwnUser();
        }

        setUser(user);
        setFollowers(convertQuantityToString(user.followers));
        setFollowing(convertQuantityToString(user.following));
      } catch (error) {
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

      <View className="bg-[#E4D8E9] flex-row border-b-2 border-[#4B0082]">
        <View className="m-5 items-center">
          <Image
            source={{ uri: image }}
            width={103}
            height={103}
            className="rounded-full border-2 border-[#4B0082] filter invert"
          />
          <View
            style={{ backgroundColor: "rgba(0, 0, 0, 0.7)" }}
            className="-mt-4 w-10 rounded-xl justify-center items-center border-2 border-white"
          >
            <Text className="text-center text-white">
              {Math.floor(Math.random() * 300)}
            </Text>
          </View>
        </View>
        <View className="mt-5">
          <Text className="text-black text-4xl font-bold mb-5">
            {user.username}
          </Text>
          <TouchableOpacity onPress={() => navigation.navigate("Settings")}>
            <Icon name="settings" size={30} />
          </TouchableOpacity>
          <View className="flex-1 flex-col">
            <TouchableOpacity
              onPress={() =>
                navigation.navigate("FollowList", { email: user.email, type: "followers" })
              }
            >
              <Text className="text-black text-lg">Followers: {followers}</Text>
            </TouchableOpacity>
            <TouchableOpacity
              onPress={() =>
                navigation.navigate("FollowList", { email: user.email, type: "following" })
              }
            >
              <Text className="text-black text-lg">Following: {following}</Text>
            </TouchableOpacity>
          </View>
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
            <TouchableOpacity
              onPress={() => setSelectedPost(item)}
              className="m-2 rounded-xl shadow-md shadow-black"
            >
              <Image
                className="rounded-xl"
                source={{ uri: item.image }}
                width={117}
                height={210}
              />
              <View className="z-index-10 -mt-8 bg-[#00000070] rounded-xl">
                <Text className="text-white text-xl font-bold text-center">
                  Walking
                </Text>
              </View>
            </TouchableOpacity>
          );
        }}
        keyExtractor={(item) => item.id}
        numColumns={3}
      />
    </View>
  );
};

export default Profile;
