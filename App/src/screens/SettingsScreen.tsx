import { View, Text, Switch, TouchableOpacity } from "react-native";
import Icon from "react-native-vector-icons/Ionicons";
import React from "react";
import { logout } from "../repositories/SettingsRepository";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import {
  bgColor,
  borderColor,
  cardBgColor,
  iconColor,
  languages,
  resetNavigation,
  textColor,
} from "../utils/Utils";
import { colors, palette } from "../styles/theme";
import DropDown from "../components/DropDown";
import { translations } from "../../translations/translation";
import AsyncStorage from "@react-native-async-storage/async-storage";
import ProfileNavigation from "../components/ProfileNavigation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { ProfileStackProps } from "../navigation/ProfileStackNavigation";
import { errorMessage } from "../utils/graphql";
import { useToast } from "../contexts/FeedbackContextProvider";

type Props = NativeStackScreenProps<ProfileStackProps, "Settings">;

const SettingsScreen = ({ navigation }: Props) => {
  const toast = useToast();
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
      toast.error(errorMessage(error));
    }
  };

  const changeMode = async () => {
    try {
      await AsyncStorage.setItem("darkMode", darkmode ? "false" : "true");
      setDarkmode(!darkmode);
    } catch (error) {
      toast.error(errorMessage(error));
    }
  };

  const changeSide = async () => {
    try {
      await AsyncStorage.setItem("lefthand", lefthand ? "false" : "true");
      setLefthand(!lefthand);
    } catch (error) {
      toast.error(errorMessage(error));
    }
  };

  const closeSession = async () => {
    try {
      await logout();
      resetNavigation(navigation, "Login");
    } catch (error) {
      toast.error(errorMessage(error));
    }
  };

  const t = translations[language || "en-EN"].screens.Settings;
  const c = colors(darkmode);

  const Row = ({
    icon,
    label,
    onPress,
    right,
    last,
    danger,
  }: {
    icon: string;
    label: string;
    onPress?: () => void;
    right?: React.ReactNode;
    last?: boolean;
    danger?: boolean;
  }) => (
    <TouchableOpacity
      disabled={!onPress}
      activeOpacity={0.6}
      onPress={onPress}
      className={`flex-row items-center h-14 px-4 ${
        last ? "" : `border-b ${borderColor(darkmode)}`
      }`}
    >
      <Icon
        name={icon}
        size={18}
        color={danger ? palette.like : iconColor(darkmode)}
      />
      <Text
        className={`flex-1 ml-3 text-base ${
          danger ? "text-[#E5484D]" : textColor(darkmode)
        }`}
      >
        {label}
      </Text>
      {right}
    </TouchableOpacity>
  );

  return (
    <View className={`flex-1 ${bgColor(darkmode)}`}>
      <ProfileNavigation navigation={navigation} message={t.settings} />

      <View
        className={`mx-5 mt-6 rounded-2xl border overflow-hidden ${cardBgColor(
          darkmode
        )} ${borderColor(darkmode)}`}
      >
        <Row
          icon="person-outline"
          label={t.editProfile}
          onPress={() => navigation.navigate("ProfileForm")}
          right={<Icon name="chevron-forward" size={18} color={c.textMuted} />}
        />
        <View className={`flex-row items-center px-4 py-1 border-b ${borderColor(darkmode)}`}>
          <Icon name="language-outline" size={18} color={iconColor(darkmode)} />
          <View className="flex-1 ml-1">
            <DropDown
              data={languages(language)}
              message={t.language}
              value={language}
              setValue={changeLanguage}
              bare
            />
          </View>
        </View>
        <Row
          icon="moon-outline"
          label={t.darkMode}
          right={
            <Switch
              value={darkmode}
              onValueChange={changeMode}
              thumbColor="#FFFFFF"
              trackColor={{ false: c.raised, true: palette.accent }}
            />
          }
        />
        <Row
          icon="hand-left-outline"
          label={t.leftHand[lefthand ? "left" : "right"]}
          onPress={changeSide}
          right={<Icon name="swap-horizontal" size={18} color={c.textMuted} />}
          last
        />
      </View>

      <View
        className={`mx-5 mt-4 rounded-2xl border overflow-hidden ${cardBgColor(
          darkmode
        )} ${borderColor(darkmode)}`}
      >
        <Row icon="log-out-outline" label={t.logout} onPress={closeSession} danger last />
      </View>
    </View>
  );
};

export default SettingsScreen;
