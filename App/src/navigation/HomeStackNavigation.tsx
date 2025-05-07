import React from "react";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import Home from "../screens/Home";
import Search from "../screens/Search";
import ProfileStackNavigation from "./ProfileStackNavigation";

type Props = {};

export type HomeStackProps = {
  Home: undefined;
  Search: undefined;
  ProfileStackNavigation: { screen: string; params: { email: string } };
};

const HomeStackNavigation = (props: Props) => {
  const Stack = createNativeStackNavigator<HomeStackProps>();

  return (
    <Stack.Navigator id={undefined} screenOptions={{ headerShown: false }}>
      <Stack.Screen name="Home" component={Home} />
      <Stack.Screen name="Search" component={Search} />
      <Stack.Screen
        name="ProfileStackNavigation"
        component={ProfileStackNavigation}
      />
    </Stack.Navigator>
  );
};

export default HomeStackNavigation;
