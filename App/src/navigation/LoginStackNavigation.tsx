import React from "react";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import LoginScreen from "../screens/LoginScreen";
import SplashScreen from "../screens/SplashScreen";
import RegisterScreen from "../screens/RegisterScreen";
import MainTabNavigation from "./MainTabNavigation";
import TokenContextProvider from "../contexts/TokenContextProvider";
import ImageContextProvider from "../contexts/ImageContextProvider";
import ThemedStatusBar from "../components/ThemedStatusBar";
import FeedbackContextProvider from "../contexts/FeedbackContextProvider";
import SessionGuard from "../components/SessionGuard";
import AccountsContextProvider from "../contexts/AccountsContextProvider";
import SafeTop from "../components/SafeTop";

type Props = {};

export type LoginStackProps = {
  Splash: undefined;
  /** `adding`: signing in one more account while another stays active. */
  Login: { adding?: boolean } | undefined;
  Register: undefined;
  MainTabNavigation: undefined;
};

const LoginStackNavigation = (props: Props) => {
  const Stack = createNativeStackNavigator<LoginStackProps>();

  return (
      <TokenContextProvider>
        <ImageContextProvider>
          <FeedbackContextProvider>
          <AccountsContextProvider>
          <ThemedStatusBar />
          <SessionGuard />
          <SafeTop>
          <Stack.Navigator
            id={undefined}
            initialRouteName="Splash"
            screenOptions={{ headerShown: false }}
          >
            <Stack.Screen
              name="Splash"
              component={SplashScreen}
              options={{ animation: "fade" }}
            />
            <Stack.Screen name="Login" component={LoginScreen} />
            <Stack.Screen name="Register" component={RegisterScreen} />
            <Stack.Screen
              name="MainTabNavigation"
              component={MainTabNavigation}
            />
          </Stack.Navigator>
          </SafeTop>
          </AccountsContextProvider>
          </FeedbackContextProvider>
        </ImageContextProvider>
      </TokenContextProvider>
  );
};

export default LoginStackNavigation;
