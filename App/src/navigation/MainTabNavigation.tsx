import React from "react";
import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import Home from "../screens/Home";
import Events from "../screens/Events";
import ActivitiesStackNavigation from "./ActivitiesStackNavigation";
import Icon from "react-native-vector-icons/Ionicons";
import ProfileStackNavigation from "./ProfileStackNavigation";
import UploadStackNavigation from "./UploadStackNavigation";

type Props = {};

type MainTabProps = {
  Home: undefined;
  ActivitiesStackNavigation: undefined;
  UploadStackNavigation: undefined;
  Events: undefined;
  ProfileStackNavigation: undefined;
};

const MainTabNavigation = (props: Props) => {
  const Tab = createBottomTabNavigator<MainTabProps>();
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
        component={Home}
        options={{
          tabBarIcon: ({ focused }) => (
            <Icon
              name={focused ? "home" : "home-outline"}
              size={30}
              color="#7B5BF2"
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
              color="#7B5BF2"
            />
          ),
        }}
      />
      <Tab.Screen
        name="UploadStackNavigation"
        component={UploadStackNavigation}
        options={{
          tabBarIcon: ({ focused }) => (
            <Icon
              name={focused ? "add-circle" : "add-circle-outline"}
              size={30}
              color="#7B5BF2"
            />
          ),
        }}
      />
      <Tab.Screen
        name="Events"
        component={Events}
        options={{
          tabBarIcon: ({ focused }) => (
            <Icon
              name={focused ? "people" : "people-outline"}
              size={30}
              color="#7B5BF2"
            />
          ),
        }}
      />
      <Tab.Screen
        name="ProfileStackNavigation"
        component={ProfileStackNavigation}
        options={{
          tabBarIcon: ({ focused }) => (
            <Icon
              name={focused ? "person-circle" : "person-circle-outline"}
              size={30}
              color="#7B5BF2"
            />
          ),
        }}
      />
    </Tab.Navigator>
  );
};

export default MainTabNavigation;
