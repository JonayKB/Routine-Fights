import React from "react";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import LoginScreen from "../screens/LoginScreen";
import RegisterScreen from "../screens/RegisterScreen";
import MainTabNavigation from "./MainTabNavigation";
import TokenContextProvider from "../contexts/TokenContextProvider";
import SettingsContextProvider from "../contexts/SettingsContextProvider";
import ImageContextProvider from "../contexts/ImageContextProvider";

type Props = {};

export type LoginStackProps = {
  Login: undefined;
  Register: undefined;
  MainTabNavigation: undefined;
};

const LoginStackNavigation = (props: Props) => {
  const Stack = createNativeStackNavigator<LoginStackProps>();

  return (
    <SettingsContextProvider>
      <TokenContextProvider>
        <ImageContextProvider>
          <Stack.Navigator
            id={undefined}
            screenOptions={{ headerShown: false }}
          >
            <Stack.Screen name="Login" component={LoginScreen} />
            <Stack.Screen name="Register" component={RegisterScreen} />
            <Stack.Screen
              name="MainTabNavigation"
              component={MainTabNavigation}
            />
          </Stack.Navigator>
        </ImageContextProvider>
      </TokenContextProvider>
    </SettingsContextProvider>
  );
};

export default LoginStackNavigation;
