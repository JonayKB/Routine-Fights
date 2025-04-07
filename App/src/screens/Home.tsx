import { View, FlatList, RefreshControl, TouchableWithoutFeedback } from "react-native";
import React, { useState } from "react";
import { Post as PostDomain } from "../utils/Post";
import Post from "../components/Post";
import Icon from "react-native-vector-icons/Ionicons";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { HomeStackProps } from "../navigation/HomeStackNavigation";

type Props = NativeStackScreenProps<HomeStackProps, "Home">;

const Home = ({navigation}: Props) => {
  const [load, setLoad] = useState<boolean>(false);

  const posts: PostDomain[] = [
    {
      id: "1",
      streak: 100,
      points: 100,
      createdAt: "2021-09-01",
      image: "https://picsum.photos/200/300",
      likes: 100,
    },
    {
      id: "2",
      streak: 200,
      points: 200,
      createdAt: "2021-09-01",
      image: "https://picsum.photos/200/300",
      likes: 200,
    },
    {
      id: "3",
      streak: 300,
      points: 300,
      createdAt: "2021-09-01",
      image: "https://picsum.photos/200/300",
      likes: 300,
    },
  ];

  return (
    <View className="bg-white">
      <TouchableWithoutFeedback className="absolute z-10 right-5 top-5" onPress={() => navigation.navigate("Search")}>
        <Icon name="search" size={35} color="#7C5AF1" />
      </TouchableWithoutFeedback>
      <View className="items-center">
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
          data={posts}
          renderItem={({ item }) => {
            return <Post post={item} />;
          }}
          keyExtractor={(item) => item.id}
        />
      </View>
    </View>
  );
};

export default Home;
