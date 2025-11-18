import {
  View,
  FlatList,
  RefreshControl,
  TouchableWithoutFeedback,
  TouchableOpacity,
  Text,
  Alert,
} from "react-native";
import React, { useEffect, useRef, useState } from "react";
import { Post as PostDomain } from "../utils/Post";
import Post from "../components/Post";
import Icon from "react-native-vector-icons/Ionicons";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { HomeStackProps } from "../navigation/HomeStackNavigation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import {
  getPostBySuscribedActivities,
  getPosts,
  getPostsFollowing,
} from "../repositories/PostRepository";
import { bgColor, cardBgColor, iconColor } from "../utils/Utils";
import { translations } from "../../translations/translation";

type Props = NativeStackScreenProps<HomeStackProps, "Home">;

const HomeScreen = ({ navigation }: Props) => {
  const [type, setType] = useState<"following" | "home" | "activity">(
    "following"
  );
  const [load, setLoad] = useState<boolean>(false);
  const [isLoadingMore, setIsLoadingMore] = useState<boolean>(false);
  const [posts, setPosts] = useState<PostDomain[]>([]);
  const { darkmode, language } = useSettingsContext();
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
          response = await getPostBySuscribedActivities(lastDate.current);
          break;
      }
      if (isLoadingMore) {
        setPosts([...posts, ...response]);
      } else {
        setPosts(response);
      }
    } catch (error) {
      Alert.alert("Error", error.response.data);
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
    lastDate.current = posts[posts.length - 1].createdAt.slice(0, 19);
    setIsLoadingMore(true);
  };

  return (
    <View className={`flex-1 ${bgColor(darkmode)}`}>
      <TouchableWithoutFeedback
        className="absolute z-10 right-5 top-5"
        onPress={() => navigation.navigate("Search")}
      >
        <Icon name="search" size={40} color={`${iconColor(darkmode)}`} />
      </TouchableWithoutFeedback>
      <View className="flex-row justify-evenly items-center px-4 mt-2">
        {["following", "home", "activity"].map((selectedType) => {
          const isActive = type === selectedType;
          return (
            <TouchableOpacity
              key={selectedType}
              onPress={() =>
                setType(selectedType as "following" | "home" | "activity")
              }
              className={`
              px-4 py-2 rounded-full
              ${isActive ? "bg-[#F65261]" : `${cardBgColor(darkmode)}`}
            `}
            >
              <Text
                className={`
                font-bold text-lg
                ${isActive ? "text-white" : `text-[${iconColor(darkmode)}]`}
              `}
              >
                {
                  translations[language || "en-US"].screens.Home.type[
                    selectedType
                  ]
                }
              </Text>
            </TouchableOpacity>
          );
        })}
      </View>
      <View className="flex-1 pt-6">
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
