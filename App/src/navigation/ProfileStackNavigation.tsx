import React from "react";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import { Post as PostDomain } from "../utils/Post";
import Profile from "../screens/Profile";
import FollowList from "../screens/FollowList";
import Post from "../screens/Post";

type Props = {};

export type ProfileStackProps = {
  Profile: undefined;
  FollowList: { type: "followers" | "following" };
  Post: { post: PostDomain };
};

const ProfileStackNavigation = (props: Props) => {
  const Stack = createNativeStackNavigator<ProfileStackProps>();

  return (
    <Stack.Navigator id={undefined} screenOptions={{ headerShown: false }}>
      <Stack.Screen name="Profile" component={Profile} />
      <Stack.Screen name="FollowList" component={FollowList} />
      <Stack.Screen name="Post" component={Post} />
    </Stack.Navigator>
  );
};

export default ProfileStackNavigation;
