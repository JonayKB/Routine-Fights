import React from "react";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import Profile from "../screens/Profile";
import FollowList from "../screens/FollowList";

type Props = {};

export type ProfileStackProps = {
  Profile: { id?: string };
  FollowList: { type: "followers" | "following" };
};

const ProfileStackNavigation = (props: Props) => {
  const Stack = createNativeStackNavigator<ProfileStackProps>();

  return (
    <Stack.Navigator id={undefined} screenOptions={{ headerShown: false }}>
      <Stack.Screen name="Profile" component={Profile} />
      <Stack.Screen name="FollowList" component={FollowList} />
    </Stack.Navigator>
  );
};

export default ProfileStackNavigation;
