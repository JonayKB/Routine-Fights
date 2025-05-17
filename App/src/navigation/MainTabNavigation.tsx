import React, { useEffect, useState } from "react";
import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import EventsScreen from "../screens/EventsScreen";
import ActivitiesStackNavigation from "./ActivitiesStackNavigation";
import Icon from "react-native-vector-icons/Ionicons";
import ProfileStackNavigation from "./ProfileStackNavigation";
import UploadFormScreen from "../screens/UploadFormScreen";
import ImageContextProvider, { useImageContext } from "../contexts/ImageContextProvider";
import { UserOut } from "../utils/User";
import { getOwnUserImageAndEmail } from "../repositories/UserRepository";
import HomeStackNavigation from "./HomeStackNavigation";
import Picture from "../components/Picture";
import { useTokenContext } from "../contexts/TokenContextProvider";

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
          tabBarActiveBackgroundColor: "#EDF2FA",
          tabBarInactiveBackgroundColor: "#EDF2FA",
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
                color="#7D3C98"
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
                color="#7D3C98"
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
                color="#7D3C98"
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
                color="#7D3C98"
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
                  focused && "border-2 border-[#7D3C98]"
                } filter invert`}
              />
            ),
          }}
        />
      </Tab.Navigator>
  );
};

export default MainTabNavigation;
