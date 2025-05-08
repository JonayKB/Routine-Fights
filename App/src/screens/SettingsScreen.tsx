import { View, Button, TouchableOpacity } from "react-native";
import React from "react";
import { logout } from "../repositories/SettingsRepository";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { languages, resetNavigation } from "../utils/Utils";
import DropDown from "../components/DropDown";
import { translations } from "../../translations/translation";
import AsyncStorage from "@react-native-async-storage/async-storage";
import ProfileNavigation from "../components/ProfileNavigation";
import { useLanguageContext } from "../contexts/SettingsContextProvider";
import { ProfileStackProps } from "../navigation/ProfileStackNavigation";

type Props = NativeStackScreenProps<ProfileStackProps, "Settings">;

const SettingsScreen = ({ navigation }: Props) => {
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

  return (
    <View>
      <ProfileNavigation
        navigation={navigation}
        message={translations[language || "en-EN"].screens.Settings.settings}
      />
      <View className="p-5 gap-5">
        <Button title={translations[language || "en-EN"].screens.Settings.editProfile} onPress={() => navigation.navigate("ProfileForm")}/>
        <TouchableOpacity>
          <DropDown
            data={languages}
            message={
              translations[language || "en-EN"].screens.Settings.language
            }
            value={language}
            setValue={changeLanguage}
          />
        </TouchableOpacity>
        <Button title="Logout" onPress={closeSession} />
      </View>
    </View>
  );
};

export default SettingsScreen;
