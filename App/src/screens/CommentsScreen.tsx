import {
  View,
  Text,
  FlatList,
  RefreshControl,
  TouchableOpacity,
} from "react-native";
import React, { useEffect, useState } from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { HomeStackProps } from "../navigation/HomeStackNavigation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { getComments } from "../repositories/CommentRepository";
import { Comment } from "../utils/Comment";
import Picture from "../components/Picture";
import ProfileNavigation from '../components/ProfileNavigation';

type Props = NativeStackScreenProps<HomeStackProps, "Comments">;

const CommentsScreen = (props: Props) => {
  const [comments, setComments] = useState<Comment[]>([]);
  const { darkmode } = useSettingsContext();
  const [load, setLoad] = useState<boolean>(false);

  useEffect(() => {
    console.log("CommentsScreen: ", props.route.params.postID);
    fetchComments();
  }, [load === true, props.route.params.postID]);

  const fetchComments = async () => {
    try {
      const response = await getComments(props.route.params.postID);
      setComments(response);
    } catch (error) {
      console.error("Error fetching comments:", error);
    }
  };

  const reload = () => {
    setLoad(true);
    setTimeout(() => {
      setLoad(false);
    }, 1000);
  };

  return (
    <View className={`flex-1 bg-[#${darkmode ? "2C2C2C" : "CCCCCC"}]`}>
      <ProfileNavigation message="" navigation={props.navigation} />
      <FlatList
        refreshControl={<RefreshControl refreshing={load} onRefresh={reload} />}
        style={{ width: "100%" }}
        data={comments}
        renderItem={({ item }) => (
          <View className="p-4 border-b border-gray-300">
            <TouchableOpacity
              onPress={() =>
                props.navigation.navigate("ProfileStackNavigation", {
                  screen: "Profile",
                  params: { email: item.user.email },
                })
              }
            >
              <Picture
                image={item.user.image}
                size={80}
                style="rounded-full border-2 border-[#4B0082]"
              />
            </TouchableOpacity>
            <Text className="text-lg font-bold">{item.user.username}</Text>
            <Text className="text-gray-700">{item.message}</Text>
            <Text className="text-gray-700">{item.createdAt}</Text>
          </View>
        )}
        keyExtractor={(item) => item.id}
      />
      {/* TODO: Add a comment input field and replies */}
    </View>
  );
};

export default CommentsScreen;
