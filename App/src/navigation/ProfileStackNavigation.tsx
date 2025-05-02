import React from "react";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import Profile from "../screens/Profile";
import FollowList from "../screens/FollowList";
import Settings from "../screens/Settings";
import ProfileForm from "../screens/ProfileForm";

type Props = {};

export type ProfileStackProps = {
  Profile: { id?: string };
  FollowList: { email: string; type: "followers" | "following" };
  Settings: undefined;
  ProfileForm: undefined;
};

const ProfileStackNavigation = (props: Props) => {
  const Stack = createNativeStackNavigator<ProfileStackProps>();

  return (
    <Stack.Navigator id={undefined} screenOptions={{ headerShown: false }}>
      <Stack.Screen name="Profile" component={Profile} />
      <Stack.Screen name="FollowList" component={FollowList} />
      <Stack.Screen name="Settings" component={Settings} />
      <Stack.Screen name="ProfileForm" component={ProfileForm} />
    </Stack.Navigator>
  );
};

export default ProfileStackNavigation;
