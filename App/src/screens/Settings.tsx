import { View, Button } from "react-native";
import React from "react";
import { logout } from "../services/SettingsService";
import { LoginStackProps } from "../navigation/LoginStackNavigation";
import { NativeStackScreenProps } from "@react-navigation/native-stack";

type Props = NativeStackScreenProps<LoginStackProps, "Settings">;

const Settings = ({ navigation }: Props) => {
  const closeSession = async () => {
    try {
      await logout();
      navigate();
    } catch (error) {
      console.error(error);
    }
  };

  const navigate = () => {
    navigation.navigate("Login");
    navigation.reset({
      index: 0,
      routes: [{ name: "Login" }],
    });
  };

  return (
    <View>
      <Button title="Logout" onPress={closeSession} />
    </View>
  );
};

export default Settings;
