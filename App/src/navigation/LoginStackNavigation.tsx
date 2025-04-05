import React from "react";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import Login from "../screens/Login";
import Register from "../screens/Register";
import MainTabNavigation from "./MainTabNavigation";
import Settings from "../screens/Settings";
import TokenContextProvider from "../contexts/TokenContextProvider";
import LanguageContextProvider from "../contexts/LanguageContextProvider";

type Props = {};

export type LoginStackProps = {
  Login: undefined;
  Register: undefined;
  MainTabNavigation: undefined;
  Settings: undefined;
};

const LoginStackNavigation = (props: Props) => {
  const Stack = createNativeStackNavigator<LoginStackProps>();

  return (
    <LanguageContextProvider>
      <TokenContextProvider>
        <Stack.Navigator id={undefined} screenOptions={{ headerShown: false }}>
          <Stack.Screen name="Login" component={Login} />
          <Stack.Screen name="Register" component={Register} />
          <Stack.Screen
            name="MainTabNavigation"
            component={MainTabNavigation}
          />
          <Stack.Screen name="Settings" component={Settings} />
        </Stack.Navigator>
      </TokenContextProvider>
    </LanguageContextProvider>
  );
};

export default LoginStackNavigation;
