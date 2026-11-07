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
import EmptyState from "../components/EmptyState";
import Comment from "../components/Comment";
import { Comment as CommentDomain } from "../utils/Comment";
import { translations } from "../../translations/translation";
import Icon from "react-native-vector-icons/Ionicons";
import {
  bgColor,
  borderColor,
  iconColor,
  raisedBgColor,
  textColor,
} from "../utils/Utils";
import { errorMessage } from "../utils/graphql";
import { useToast } from "../contexts/FeedbackContextProvider";

type Props = NativeStackScreenProps<HomeStackProps, "Comments">;

const CommentsScreen = (props: Props) => {
  const [comments, setComments] = useState<CommentDomain[]>([]);
  const { darkmode, language } = useSettingsContext();
  const toast = useToast();
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
      toast.error(errorMessage(error));
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
      toast.error(errorMessage(error));
    }
  };

  return (
    <View className={`flex-1 ${bgColor(darkmode)}`}>
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
        ListEmptyComponent={
          !load ? (
            <EmptyState
              icon="chatbubbles-outline"
              title={translations[language || "en-EN"].screens.Comments.empty}
              hint={translations[language || "en-EN"].screens.Comments.emptyHint}
            />
          ) : null
        }
      />

      <View
        className={`flex-row items-center px-4 py-3 border-t ${borderColor(
          darkmode
        )} ${bgColor(darkmode)}`}
      >
        <TextInput
          value={text}
          placeholder={translations[language || "en-EN"].screens.Comments.placeholder}
          placeholderTextColor={darkmode ? "#8A847A" : "#7B7266"}
          className={`flex-1 h-11 text-base px-4 py-0 rounded-xl border ${raisedBgColor(
            darkmode
          )} ${borderColor(darkmode)} ${textColor(darkmode)}`}
          onChangeText={(text) => setText(text)}
        />
        <TouchableOpacity
          activeOpacity={0.8}
          disabled={!text?.trim()}
          className={`ml-2 w-11 h-11 items-center justify-center rounded-xl ${
            text?.trim() ? "bg-[#C2410C]" : raisedBgColor(darkmode)
          }`}
          onPress={sendMessage}
        >
          <Icon
            name="arrow-up"
            size={20}
            color={text?.trim() ? "#FFFFFF" : iconColor(darkmode)}
          />
        </TouchableOpacity>
      </View>
    </View>
  );
};

export default CommentsScreen;
