import { View, Button, TouchableOpacity } from "react-native";
import React, { useEffect, useState } from "react";
import { logout } from "../services/SettingsService";
import { LoginStackProps } from "../navigation/LoginStackNavigation";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { resetNavigation } from "../utils/Utils";
import DropDown from "../components/DropDown";
import { translations } from "../../translations/translation";
import AsyncStorage from "@react-native-async-storage/async-storage";
import ProfileNavigation from "../components/ProfileNavigation";
import { useLanguageContext } from "../contexts/LanguageContextProvider";

type Props = NativeStackScreenProps<LoginStackProps, "Settings">;

const Settings = ({ navigation }: Props) => {
  const { language, setLanguage } = useLanguageContext();

  const changeLanguage = async (language: string) => {
    try {
      await AsyncStorage.setItem("language", language);
      setLanguage(language);
    } catch (error) {
      console.error(error);
    }
  };

  const closeSession = async () => {
    try {
      await logout();
      resetNavigation(navigation, "Login");
    } catch (error) {
      console.error(error);
    }
  };

  const languages = [
    { label: "English", value: "en-EN" },
    { label: "Spanish", value: "es-ES" },
  ];

  return (
    <View>
      <ProfileNavigation
        navigation={navigation}
        message={translations[language || "en-EN"].screens.Settings.settings}
      />
      <TouchableOpacity>
        <DropDown
          data={languages}
          message={translations[language || "en-EN"].screens.Settings.language}
          value={language}
          setValue={changeLanguage}
        />
      </TouchableOpacity>
      <Button title="Logout" onPress={closeSession} />
    </View>
  );
};

export default Settings;
