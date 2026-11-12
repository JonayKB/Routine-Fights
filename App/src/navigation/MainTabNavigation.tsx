import React, { useEffect, useState } from "react";
import { View } from "react-native";
import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import EventsScreen from "../screens/EventsScreen";
import ActivitiesStackNavigation from "./ActivitiesStackNavigation";
import Icon from "react-native-vector-icons/Ionicons";
import ProfileStackNavigation from "./ProfileStackNavigation";
import UploadFormScreen from "../screens/UploadFormScreen";
import { useImageContext } from "../contexts/ImageContextProvider";
import { UserOut } from "../utils/User";
import { getOwnUserImageAndEmail } from "../repositories/UserRepository";
import HomeStackNavigation from "./HomeStackNavigation";
import Avatar from "../components/Avatar";
import { palette } from "../styles/theme";
import { useTokenContext } from "../contexts/TokenContextProvider";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { iconColor } from "../utils/Utils";
import { useSafeAreaInsets } from "react-native-safe-area-context";
import InboxContextProvider from "../contexts/InboxContextProvider";
import { useAccounts } from "../contexts/AccountsContextProvider";

type Props = {};

type MainTabProps = {
  Home: undefined;
  ActivitiesStackNavigation: undefined;
  UploadForm: undefined;
  Events: undefined;
  ProfileStackNavigation: undefined;
};

const MainTabNavigation = (props: Props) => {
  const Tab = createBottomTabNavigator<MainTabProps>();
  const [image, setImage] = useState<string>(null);
  const { setEmail } = useTokenContext();
  const { openSwitcher, remember } = useAccounts();
  const { uri } = useImageContext();
  const { darkmode } = useSettingsContext();
  const baseBg = darkmode ? "#1C1A18" : "#FFFFFF";
  const activeColor = darkmode ? palette.accentOnDark : palette.accent;
  const [username, setUsername] = useState<string>("");
  const insets = useSafeAreaInsets();
  const tabIcon = (name: string, focused: boolean) => (
    <Icon
      name={focused ? name : `${name}-outline`}
      size={24}
      color={focused ? activeColor : iconColor(darkmode)}
    />
  );

  useEffect(() => {
    const fetchImageAndEmail = async () => {
      const user: UserOut = await getOwnUserImageAndEmail();
      setImage(user.image);
      setUsername(user.username ?? user.email);
      setEmail(user.email);
      // Keeps the account switcher's name and picture up to date.
      remember(user);
    };
    fetchImageAndEmail();
  }, [uri]);

  return (
    <InboxContextProvider>
    <Tab.Navigator
      id={undefined}
      screenOptions={{
        headerShown: false,
        tabBarActiveBackgroundColor: baseBg,
        tabBarInactiveBackgroundColor: baseBg,
        tabBarStyle: {
          // Room for the home indicator on iPhones (0 on Android).
          height: 64 + insets.bottom,
          paddingBottom: insets.bottom,
          backgroundColor: baseBg,
          borderTopWidth: 0,
          elevation: 0,
        },
        tabBarBackground: () => (
          <View
            style={{
              flex: 1,
              backgroundColor: baseBg,
              borderTopWidth: 1,
              borderTopColor: darkmode ? "#EFE9DE1A" : "#1F1B161A",
            }}
          />
        ),
        tabBarItemStyle: { paddingTop: 8 },
        tabBarShowLabel: false,
        tabBarHideOnKeyboard: true,
        animation: "shift",
      }}
    >
      <Tab.Screen
        name="Home"
        component={HomeStackNavigation}
        options={{
          tabBarIcon: ({ focused }) => (
            tabIcon("home", focused)
          ),
        }}
      />
      <Tab.Screen
        name="ActivitiesStackNavigation"
        component={ActivitiesStackNavigation}
        options={{
          tabBarIcon: ({ focused }) => (
            tabIcon("flame", focused)
          ),
        }}
      />
      <Tab.Screen
        name="UploadForm"
        component={UploadFormScreen}
        options={{
          tabBarIcon: ({ focused }) => (
            tabIcon("add-circle", focused)
          ),
        }}
      />
      <Tab.Screen
        name="Events"
        component={EventsScreen}
        options={{
          tabBarIcon: ({ focused }) => (
            tabIcon("people", focused)
          ),
        }}
      />
      <Tab.Screen
        name="ProfileStackNavigation"
        component={ProfileStackNavigation}
        listeners={({ navigation }) => ({
          // Long press on your picture switches account, like Instagram.
          tabLongPress: () => openSwitcher(),
          tabPress: (e: any) => {
            // Coming from someone else's profile, the tab should open yours.
            e.preventDefault();
            (navigation as any).navigate("ProfileStackNavigation", {
              screen: "Profile",
              params: { email: undefined },
            });
          },
        })}
        options={{
          tabBarIcon: ({ focused }) => (
            <Avatar
              image={image}
              name={username}
              size={28}
              ring={focused ? activeColor : undefined}
            />
          ),
        }}
      />
    </Tab.Navigator>
    </InboxContextProvider>
  );
};

export default MainTabNavigation;
