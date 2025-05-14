import {
  View,
  FlatList,
  RefreshControl,
  TouchableWithoutFeedback,
  Button,
} from "react-native";
import React, { useState } from "react";
import { Post as PostDomain } from "../utils/Post";
import Post from "../components/Post";
import Icon from "react-native-vector-icons/Ionicons";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { HomeStackProps } from "../navigation/HomeStackNavigation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";

type Props = NativeStackScreenProps<HomeStackProps, "Home">;

const HomeScreen = ({ navigation }: Props) => {
  const [load, setLoad] = useState<boolean>(false);
  const [posts, setPosts] = useState<PostDomain[]>([]);
  const { darkmode } = useSettingsContext();

  const reload = () => {
    setLoad(true);
    setTimeout(() => {
      setLoad(false);
    }, 1000);
  };

  return (
    <View className={`flex-1 bg-[#${darkmode ? "2C2C2C" : "CCCCCC"}]`}>
      <TouchableWithoutFeedback
        className="absolute z-10 right-5 top-5"
        onPress={() => navigation.navigate("Search")}
      >
        <Icon name="search" size={35} color="#7C5AF1" />
      </TouchableWithoutFeedback>
      <View className="flex-row justify-evenly items-center absolute z-10 top-8 w-full">
        <Button title="Following" />
        <Button title="Home" />
        <Button title="Category" />
      </View>
      <View className="items-center">
        <FlatList
          refreshControl={
            <RefreshControl refreshing={load} onRefresh={reload} />
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

export default HomeScreen;
