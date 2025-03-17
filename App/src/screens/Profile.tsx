import {
  Image,
  Text,
  View,
  TouchableOpacity,
  FlatList,
  RefreshControl,
  Touchable,
} from "react-native";
import React, { useEffect, useState } from "react";
import { UserOut } from "../utils/User";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ProfileStackProps } from "../navigation/ProfileStackNavigation";
import { Post as PostDomain } from "../utils/Post";
import Post from "./Post";

type Props = NativeStackScreenProps<ProfileStackProps, "Profile">;

const Profile = ({ navigation, route }: Props) => {
  const [followers, setFollowers] = useState<string>("");
  const [following, setFollowing] = useState<string>("");
  const [load, setLoad] = useState<boolean>(false);
  const [selectedPost, setSelectedPost] = useState<PostDomain>(null);

  const user: UserOut = {
    id: "1",
    username: "user",
    email: "email@gmail.com",
    nationality: "Spain",
    phoneNumber: "123456789",
    image: "https://picsum.photos/200/300",
    createdAt: "2021-09-01",
    followers: 3855555,
    following: 900000,
  };

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

  useEffect(() => {
    setFollowers(convertQuantityToString(user.followers));
    setFollowing(convertQuantityToString(user.following));
  }, []);

  const convertQuantityToString = (quantity: number) => {
    if (quantity.toString().length > 5) {
      if (quantity > 999999) {
        return numberToString(quantity).slice(0, 5) + "M";
      }
      return numberToString(quantity).slice(0, 5) + "K";
    } else {
      return numberToString(quantity);
    }
  };

  const numberToString = (number: number) => {
    return new Intl.NumberFormat("en-EN").format(number);
  };

  return (
    <View className="flex-1">
      {selectedPost && (
        <View className="absolute w-full h-full z-10">
          <TouchableOpacity
            onPress={() => setSelectedPost(null)}
            className="bg-black opacity-80 w-full h-full absolute"
          />
          <View className="absolute justify-center h-full">
            <Post post={selectedPost} />
          </View>
        </View>
      )}

      <View className="bg-[#F1FEFC] flex-row border-b-2 border-[#4B0082]">
        <View className="m-5 items-center">
          <Image
            source={{ uri: user.image }}
            width={103}
            height={103}
            className="rounded-full border-2 border-[#4B0082] filter invert"
          />
          <View
            style={{ backgroundColor: "rgba(0, 0, 0, 0.7)" }}
            className="-mt-4 w-10 rounded-xl justify-center items-center border-2 border-white"
          >
            <Text className="text-center text-white">239</Text>
          </View>
        </View>
        <View className="mt-5">
          <Text className="text-black text-4xl font-bold mb-5">
            {user.username}
          </Text>
          <View className="flex-1 flex-col">
            <TouchableOpacity
              onPress={() =>
                navigation.navigate("FollowList", { type: "followers" })
              }
            >
              <Text className="text-black text-lg">Followers: {followers}</Text>
            </TouchableOpacity>
            <TouchableOpacity
              onPress={() =>
                navigation.navigate("FollowList", { type: "following" })
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
