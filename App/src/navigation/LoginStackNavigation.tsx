import React from "react";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import Login from "../screens/Login";
import Register from "../screens/Register";
import MainTabNavigation from "./MainTabNavigation";

type Props = {};

export type LoginStackProps = {
  Login: undefined;
  Register: undefined;
  MainTabNavigation: undefined;
};

const LoginStackNavigation = (props: Props) => {
  const Stack = createNativeStackNavigator<LoginStackProps>();

  return (
    <Stack.Navigator id={undefined} screenOptions={{ headerShown: false }}>
      <Stack.Screen name="Login" component={Login} />
      <Stack.Screen name="Register" component={Register} />
      <Stack.Screen name="MainTabNavigation" component={MainTabNavigation} />
    </Stack.Navigator>
  );
};

export default LoginStackNavigation;
