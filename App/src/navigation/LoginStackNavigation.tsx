import React from "react";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import Login from "../screens/Login";
import Register from "../screens/Register";
import Home from "../screens/Home";
import ActivitiesStackNavigation from "./ActivitiesStackNavigation";

type Props = {};

export type LoginStackProps = {
  Login: undefined;
  Register: undefined;
  Home: undefined;
  ActivitiesStackNavigation: undefined;
};

const LoginStackNavigation = (props: Props) => {
  const Stack = createNativeStackNavigator<LoginStackProps>();

  return (
    <Stack.Navigator id={undefined} screenOptions={{ headerShown: false }}>
      <Stack.Screen name="Login" component={Login} />
      <Stack.Screen name="Register" component={Register} />
      <Stack.Screen name="Home" component={Home} />
      <Stack.Screen name="ActivitiesStackNavigation" component={ActivitiesStackNavigation} />
    </Stack.Navigator>
  );
};

export default LoginStackNavigation;
