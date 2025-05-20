import React, { useEffect, useState } from "react";
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
import Picture from "../components/Picture";
import { useTokenContext } from "../contexts/TokenContextProvider";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { borderColor, iconColor } from "../utils/Utils";

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
  const { uri } = useImageContext();
  const { darkmode } = useSettingsContext();
  const baseBg = darkmode ? "#4B294F" : "#E8E2F0";

  useEffect(() => {
    const fetchImageAndEmail = async () => {
      const user: UserOut = await getOwnUserImageAndEmail();
      setImage(user.image);
      setEmail(user.email);
    };
    fetchImageAndEmail();
  }, [uri]);

  return (
    <Tab.Navigator
      id={undefined}
      screenOptions={{
        headerShown: false,
        tabBarActiveBackgroundColor: baseBg,
        tabBarInactiveBackgroundColor: baseBg,
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
            <Icon
              name={focused ? "home" : "home-outline"}
              size={30}
              color={`${iconColor(darkmode)}`}
            />
          ),
        }}
      />
      <Tab.Screen
        name="ActivitiesStackNavigation"
        component={ActivitiesStackNavigation}
        options={{
          tabBarIcon: ({ focused }) => (
            <Icon
              name={focused ? "flame" : "flame-outline"}
              size={30}
              color={`${iconColor(darkmode)}`}
            />
          ),
        }}
      />
      <Tab.Screen
        name="UploadForm"
        component={UploadFormScreen}
        options={{
          tabBarIcon: ({ focused }) => (
            <Icon
              name={focused ? "add-circle" : "add-circle-outline"}
              size={30}
              color={`${iconColor(darkmode)}`}
            />
          ),
        }}
      />
      <Tab.Screen
        name="Events"
        component={EventsScreen}
        options={{
          tabBarIcon: ({ focused }) => (
            <Icon
              name={focused ? "people" : "people-outline"}
              size={30}
              color={`${iconColor(darkmode)}`}
            />
          ),
        }}
      />
      <Tab.Screen
        name="ProfileStackNavigation"
        component={ProfileStackNavigation}
        options={{
          tabBarIcon: ({ focused }) => (
            <Picture
              image={image}
              size={32}
              style={`rounded-full ${
                focused && `border-2 ${borderColor(darkmode)}`
              }`}
            />
          ),
        }}
      />
    </Tab.Navigator>
  );
};

export default MainTabNavigation;
