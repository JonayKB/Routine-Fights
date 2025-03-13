import React from "react";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import Login from "../screens/Login";
import Register from "../screens/Register";
import Home from "../screens/Home";

type Props = {};

export type LoginStackProps = {
  Login: undefined;
  Register: undefined;
  Home: undefined;
};

const LoginNavigation = (props: Props) => {
  const Stack = createNativeStackNavigator<LoginStackProps>();

  return (
    <Stack.Navigator id={undefined} screenOptions={{ headerShown: false }}>
      <Stack.Screen name="Login" component={Login} />
      <Stack.Screen name="Register" component={Register} />
      <Stack.Screen name="Home" component={Home} />
    </Stack.Navigator>
  );
};

export default LoginNavigation;
