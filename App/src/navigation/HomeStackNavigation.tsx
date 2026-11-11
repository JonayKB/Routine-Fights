import React from "react";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import HomeScreen from "../screens/HomeScreen";
import SearchScreen from "../screens/SearchScreen";
import ProfileStackNavigation from "./ProfileStackNavigation";
import CommentsScreen from "../screens/CommentsScreen";
import NotificationsScreen from "../screens/NotificationsScreen";
import ConversationsScreen from "../screens/ConversationsScreen";
import ChatScreen from "../screens/ChatScreen";

type Props = {};

export type HomeStackProps = {
  Home: undefined;
  Search: undefined;
  ProfileStackNavigation: { screen: string; params: { email: string } };
  Comments: { postID: string };
  Notifications: undefined;
  Conversations: undefined;
  /** By chat id when known, otherwise the chat with that user (created on the first message). */
  Chat: { conversationId?: string; email: string; name?: string };
};

const HomeStackNavigation = (props: Props) => {
  const Stack = createNativeStackNavigator<HomeStackProps>();

  return (
    <Stack.Navigator id={undefined} screenOptions={{ headerShown: false }}>
      <Stack.Screen name="Home" component={HomeScreen} />
      <Stack.Screen name="Search" component={SearchScreen} />
      <Stack.Screen name="Comments" component={CommentsScreen} />
      <Stack.Screen name="Notifications" component={NotificationsScreen} />
      <Stack.Screen name="Conversations" component={ConversationsScreen} />
      <Stack.Screen name="Chat" component={ChatScreen} />
      <Stack.Screen
        name="ProfileStackNavigation"
        component={ProfileStackNavigation}
      />
    </Stack.Navigator>
  );
};

export default HomeStackNavigation;
