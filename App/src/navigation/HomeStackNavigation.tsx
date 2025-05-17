import React from "react";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import HomeScreen from "../screens/HomeScreen";
import SearchScreen from "../screens/SearchScreen";
import ProfileStackNavigation from "./ProfileStackNavigation";
import CommentsScreen from "../screens/CommentsScreen";

type Props = {};

export type HomeStackProps = {
  Home: undefined;
  Search: undefined;
  ProfileStackNavigation: { screen: string; params: { email: string } };
  Comments: { postID: string };
};

const HomeStackNavigation = (props: Props) => {
  const Stack = createNativeStackNavigator<HomeStackProps>();

  return (
    <Stack.Navigator id={undefined} screenOptions={{ headerShown: false }}>
      <Stack.Screen name="Home" component={HomeScreen} />
      <Stack.Screen name="Search" component={SearchScreen} />
      <Stack.Screen name="Comments" component={CommentsScreen} />
      <Stack.Screen
        name="ProfileStackNavigation"
        component={ProfileStackNavigation}
      />
    </Stack.Navigator>
  );
};

export default HomeStackNavigation;
