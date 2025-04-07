import React from "react";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import Profile from "../screens/Profile";
import Home from "../screens/Home";
import Search from "../screens/Search";

type Props = {};

export type HomeStackProps = {
  Home: undefined;
  Search: undefined;
  Profile: { email: string };
};

const HomeStackNavigation = (props: Props) => {
  const Stack = createNativeStackNavigator<HomeStackProps>();

  return (
    <Stack.Navigator id={undefined} screenOptions={{ headerShown: false }}>
      <Stack.Screen name="Home" component={Home} />
      <Stack.Screen name="Search" component={Search} />
      <Stack.Screen name="Profile" component={Profile} />
    </Stack.Navigator>
  );
};

export default HomeStackNavigation;
