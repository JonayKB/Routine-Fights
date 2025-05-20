import { View, Button, Alert } from "react-native";
import React from "react";
import { logout } from "../repositories/SettingsRepository";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { bgColor, languages, resetNavigation } from "../utils/Utils";
import DropDown from "../components/DropDown";
import { translations } from "../../translations/translation";
import AsyncStorage from "@react-native-async-storage/async-storage";
import ProfileNavigation from "../components/ProfileNavigation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { ProfileStackProps } from "../navigation/ProfileStackNavigation";

type Props = NativeStackScreenProps<ProfileStackProps, "Settings">;

const SettingsScreen = ({ navigation }: Props) => {
  const {
    language,
    setLanguage,
    darkmode,
    setDarkmode,
    lefthand,
    setLefthand,
  } = useSettingsContext();

  const changeLanguage = async (language: string) => {
    try {
      await AsyncStorage.setItem("language", language);
      setLanguage(language);
    } catch (error) {
      Alert.alert("Error", error.response.data);
    }
  };

  const changeMode = async () => {
    try {
      await AsyncStorage.setItem("darkMode", darkmode ? "false" : "true");
      setDarkmode(!darkmode);
    } catch (error) {
      Alert.alert("Error", error.response.data);
    }
  };

  const changeSide = async () => {
    try {
      await AsyncStorage.setItem("lefthand", lefthand ? "false" : "true");
      setLefthand(!lefthand);
    } catch (error) {
      Alert.alert("Error", error.response.data);
    }
  };

  const closeSession = async () => {
    try {
      await logout();
      resetNavigation(navigation, "Login");
    } catch (error) {
      Alert.alert("Error", error.response.data);
    }
  };

  return (
    <View className={`flex-1 ${bgColor(darkmode)}`}>
      <ProfileNavigation
        navigation={navigation}
        message={translations[language || "en-EN"].screens.Settings.settings}
      />
      <View
        className={`p-5 gap-5 m-4 ${
          darkmode ? "bg-[#4B294F]" : "bg-[#E8E2F0]"
        } rounded-2xl`}
      >
        <Button
          title={translations[language || "en-EN"].screens.Settings.editProfile}
          onPress={() => navigation.navigate("ProfileForm")}
          color={darkmode ? "#B28DFF" : "#7D3C98"}
        />
        <View
          className={`rounded-xl px-4 py-2 ${
            darkmode ? "bg-[#3A1D3C]" : "bg-white"
          }`}
        >
          <DropDown
            data={languages(language)}
            message={
              translations[language || "en-EN"].screens.Settings.language
            }
            value={language}
            setValue={changeLanguage}
          />
        </View>
        <Button
          title={
            darkmode
              ? translations[language || "en-EN"].screens.Settings.lightMode
              : translations[language || "en-EN"].screens.Settings.darkMode
          }
          onPress={changeMode}
          color={darkmode ? "#B28DFF" : "#7D3C98"}
        />
        <Button
          title={translations[language || "en-EN"].screens.Settings.leftHand[lefthand ? "left" : "right"]}
          onPress={changeSide}
          color={darkmode ? "#B28DFF" : "#7D3C98"}
        />
        <Button title={translations[language || "en-EN"].screens.Settings.logout} onPress={closeSession} color="#F65261" />
      </View>
    </View>
  );
};

export default SettingsScreen;
