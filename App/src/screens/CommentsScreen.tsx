import {
  View,
  FlatList,
  RefreshControl,
  TextInput,
  TouchableOpacity,
} from "react-native";
import React, { useEffect, useState } from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { HomeStackProps } from "../navigation/HomeStackNavigation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { getComments, postComment } from "../repositories/CommentRepository";
import ProfileNavigation from "../components/ProfileNavigation";
import Comment from "../components/Comment";
import { Comment as CommentDomain } from "../utils/Comment";
import { translations } from "../../translations/translation";
import Icon from "react-native-vector-icons/Ionicons";

type Props = NativeStackScreenProps<HomeStackProps, "Comments">;

const CommentsScreen = (props: Props) => {
  const [comments, setComments] = useState<CommentDomain[]>([]);
  const { darkmode, language } = useSettingsContext();
  const [load, setLoad] = useState<boolean>(false);
  const [text, setText] = useState<string>("");

  useEffect(() => {
    fetchComments();
  }, []);

  useEffect(() => {
    if (load) {
      fetchComments();
    }
  }, [load]);

  const fetchComments = async () => {
    try {
      const response = await getComments(props.route.params.postID);
      setComments(response);
    } catch (error) {
      console.error("Error fetching comments:", error);
    } finally {
      setLoad(false);
    }
  };

  const sendMessage = async () => {
    try {
      const response = await postComment(text, props.route.params.postID);
      if (response) {
        setText("");
        setLoad(true);
      }
    } catch (error) {
      console.error("Error sending message:", error);
    }
  };

  return (
    <View className={`flex-1 ${darkmode ? "bg-[#333333]" : "bg-[#FCFCFC]"}`}>
      <ProfileNavigation navigation={props.navigation} />

      <FlatList
        refreshControl={
          <RefreshControl refreshing={load} onRefresh={() => setLoad(true)} />
        }
        style={{ width: "100%" }}
        data={comments}
        renderItem={({ item }) => (
          <Comment
            navigation={props.navigation}
            comment={item}
          />
        )}
        keyExtractor={(item) => item.id}
      />

      <View className="flex-row justify-center items-center w-full px-4 pb-4">
        <TextInput
          value={text}
          placeholder={translations[language || "en-EN"].screens.Home.search}
          placeholderTextColor={darkmode ? "#D8BFFF" : "#4B0082"}
          className={`text-2xl w-10/12 pl-4 py-3 rounded-xl ${
            darkmode
              ? "bg-[#4B294F] text-white border border-[#B28DFF]"
              : "bg-white text-black border-2 border-[#4B0082]"
          }`}
          onChangeText={(text) => setText(text)}
        />
        <TouchableOpacity
          className={`ml-2 p-3 rounded-xl ${
            darkmode ? "bg-[#B28DFF]" : "bg-[#4B0082]"
          }`}
          onPress={sendMessage}
        >
          <Icon name="send" size={30} color="white" />
        </TouchableOpacity>
      </View>
    </View>
  );
};

export default CommentsScreen;
