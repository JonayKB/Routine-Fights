import {
  View,
  FlatList,
  RefreshControl,
  TouchableWithoutFeedback,
  Button,
} from "react-native";
import React, { useEffect, useRef, useState } from "react";
import { Post as PostDomain } from "../utils/Post";
import Post from "../components/Post";
import Icon from "react-native-vector-icons/Ionicons";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { HomeStackProps } from "../navigation/HomeStackNavigation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { getPosts, getPostsFollowing } from "../repositories/PostRepository";

type Props = NativeStackScreenProps<HomeStackProps, "Home">;

const HomeScreen = ({ navigation }: Props) => {
  const [type, setType] = useState<"following" | "home" | "activity">(
    "following"
  );
  const [load, setLoad] = useState<boolean>(false);
  const [isLoadingMore, setIsLoadingMore] = useState<boolean>(false);
  const [posts, setPosts] = useState<PostDomain[]>([]);
  const { darkmode } = useSettingsContext();
  const lastDate = useRef(new Date().toISOString().slice(0, 19));

  useEffect(() => {
    fetchPosts();
  }, []);

  useEffect(() => {
    if (load || isLoadingMore) {
      fetchPosts();
    }
  }, [load, isLoadingMore]);

  useEffect(() => {
    reload();
  }, [type]);

  const fetchPosts = async () => {
    try {
      let response = [];
      switch (type) {
        case "following":
          response = await getPostsFollowing(lastDate.current);
          break;
        case "home":
          response = await getPosts(lastDate.current);
          break;
        case "activity":
          break;
      }
      setPosts(isLoadingMore ? [...posts, ...response] : response);
    } catch (error) {
      console.error("Error fetching posts:", error);
    } finally {
      setLoad(false);
      setIsLoadingMore(false);
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

  return (
    <View className={`flex-1 ${darkmode ? "bg-[#333333]" : "bg-[#FCFCFC]"}`}>
      <TouchableWithoutFeedback
        className="absolute z-10 left-5 top-5"
        onPress={() => navigation.navigate("Search")}
      >
        <Icon name="search" size={35} color="#7C5AF1" />
      </TouchableWithoutFeedback>
      <View className="flex-row justify-evenly items-center absolute z-10 top-8 w-full">
        <Button title="Following" onPress={() => setType("following")} />
        <Button title="Home" onPress={() => setType("home")} />
        <Button title="activity" onPress={() => setType("activity")} />
      </View>
      <View className="items-center">
        <FlatList
          refreshControl={
            <RefreshControl refreshing={load} onRefresh={reload} />
          }
          data={posts}
          renderItem={({ item }) => {
            return <Post post={item} navigation={navigation} />;
          }}
          keyExtractor={(item) => item.id}
          onEndReached={loadMore}
          onEndReachedThreshold={0.5}
        />
      </View>
    </View>
  );
};

export default HomeScreen;
