import React from "react";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import ProfileScreen from "../screens/ProfileScreen";
import FollowListScreen from "../screens/FollowListScreen";
import SettingsScreen from "../screens/SettingsScreen";
import ProfileFormScreen from "../screens/ProfileFormScreen";
import ProfilePictureScreen from "../screens/ProfilePictureScreen";

type Props = {};

export type ProfileStackProps = {
  Profile: { email?: string };
  FollowList: { email: string; type: "followers" | "following" };
  Settings: undefined;
  ProfileForm: undefined;
  ProfilePictureScreen: { email: string };
};

const ProfileStackNavigation = (props: Props) => {
  const Stack = createNativeStackNavigator<ProfileStackProps>();

  return (
    <Stack.Navigator id={undefined} screenOptions={{ headerShown: false }}>
      <Stack.Screen name="Profile" component={ProfileScreen} />
      <Stack.Screen name="FollowList" component={FollowListScreen} />
      <Stack.Screen name="Settings" component={SettingsScreen} />
      <Stack.Screen name="ProfileForm" component={ProfileFormScreen} />
      <Stack.Screen
        name="ProfilePictureScreen"
        component={ProfilePictureScreen}
      />
    </Stack.Navigator>
  );
};

export default ProfileStackNavigation;
