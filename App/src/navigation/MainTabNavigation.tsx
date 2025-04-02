import React, { useEffect, useState } from "react";
import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import Home from "../screens/Home";
import Events from "../screens/Events";
import ActivitiesStackNavigation from "./ActivitiesStackNavigation";
import Icon from "react-native-vector-icons/Ionicons";
import ProfileStackNavigation from "./ProfileStackNavigation";
import UploadForm from "../screens/UploadForm";
import ImageContextProvider from "../contexts/ImageContextProvider";
import { Image } from "react-native";
import { uri } from "../utils/Utils";
import { useAppContext } from "../contexts/TokenContextProvider";
import { getOwnUser } from "../services/ProfileService";
import { UserOut } from "../utils/User";

type Props = {};

type MainTabProps = {
  Home: undefined;
  ActivitiesStackNavigation: undefined;
  UploadForm: undefined;
  Events: undefined;
  ProfileStackNavigation: undefined;
};

const MainTabNavigation = (props: Props) => {
  const { token } = useAppContext();
  const Tab = createBottomTabNavigator<MainTabProps>();
  const [image, setImage] = useState<string>(null);

  useEffect(() => {
    // TODO: method for just the image
    const fetchImage = async () => {
      const user: UserOut = await getOwnUser();
      setImage(user.image);
    };
    fetchImage();
  }, []);

  return (
    <ImageContextProvider>
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
          name="UploadForm"
          component={UploadForm}
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
              <Image
                source={{
                  uri: uri + "/images/" + image,
                  headers: { Authorization: `Bearer ${token}` },
                }}
                width={32}
                height={32}
                className={`rounded-full ${
                  focused && "border-2 border-[#7B5BF2]"
                } filter invert`}
              />
            ),
          }}
        />
      </Tab.Navigator>
    </ImageContextProvider>
  );
};

export default MainTabNavigation;
