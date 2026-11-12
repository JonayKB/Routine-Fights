import { View, Text, TouchableOpacity } from "react-native";
import Icon from "react-native-vector-icons/Ionicons";
import React, { useState } from "react";
import { useAccounts } from "../contexts/AccountsContextProvider";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import {
  bgColor,
  borderColor,
  cardBgColor,
  iconColor,
  LANGUAGES,
  nativeLanguageName,
  textColor,
} from "../utils/Utils";
import { colors, palette } from "../styles/theme";
import OptionSheet, { SheetOption } from "../components/OptionSheet";
import { translations } from "../../translations/translation";
import AsyncStorage from "@react-native-async-storage/async-storage";
import ProfileNavigation from "../components/ProfileNavigation";
import { ThemeMode, useSettingsContext } from "../contexts/SettingsContextProvider";
import { ProfileStackProps } from "../navigation/ProfileStackNavigation";
import { errorMessage } from "../utils/graphql";
import { deviceLanguage } from "../utils/deviceSettings";
import { useToast } from "../contexts/FeedbackContextProvider";

type Props = NativeStackScreenProps<ProfileStackProps, "Settings">;

const THEME_OPTIONS: { mode: ThemeMode; icon: string }[] = [
  { mode: "system", icon: "phone-portrait-outline" },
  { mode: "light", icon: "sunny-outline" },
  { mode: "dark", icon: "moon-outline" },
];

const SettingsScreen = ({ navigation }: Props) => {
  const toast = useToast();
  const { signOut, openSwitcher } = useAccounts();
  const {
    language,
    languageMode,
    setLanguageMode,
    darkmode,
    themeMode,
    setThemeMode,
    lefthand,
    setLefthand,
  } = useSettingsContext();

  const [languageSheet, setLanguageSheet] = useState(false);

  const changeLanguage = async (mode: string) => {
    try {
      await setLanguageMode(mode);
    } catch (error) {
      toast.error(errorMessage(error));
    }
  };

  const changeMode = async (mode: ThemeMode) => {
    try {
      await setThemeMode(mode);
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
      await signOut();
    } catch (error) {
      toast.error(errorMessage(error));
    }
  };

  const t = translations[language || "en-EN"].screens.Settings;

  const languageOptions: SheetOption[] = [
    {
      value: "system",
      label: t.systemLanguage,
      description: nativeLanguageName(deviceLanguage()),
      icon: "phone-portrait-outline",
    },
    ...LANGUAGES.map((l) => ({
      value: l.value,
      label: l.nativeName,
      // The name in the current language helps when the native one is unfamiliar.
      description: l.value === language ? undefined : t.languages[l.key],
    })),
  ];
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
        <Row
          icon="language-outline"
          label={t.language}
          onPress={() => setLanguageSheet(true)}
          right={
            <View className="flex-row items-center">
              <Text className="text-base mr-1" style={{ color: c.textSecondary }}>
                {languageMode === "system" ? t.themes.system : nativeLanguageName(language)}
              </Text>
              <Icon name="chevron-forward" size={18} color={c.textMuted} />
            </View>
          }
        />
        <View className={`px-4 py-3 border-b ${borderColor(darkmode)}`}>
          <View className="flex-row items-center mb-3">
            <Icon name="contrast-outline" size={18} color={iconColor(darkmode)} />
            <Text className={`flex-1 ml-3 text-base ${textColor(darkmode)}`}>{t.theme}</Text>
          </View>
          <View
            accessibilityRole="radiogroup"
            className="flex-row rounded-xl p-1"
            style={{ backgroundColor: c.raised }}
          >
            {THEME_OPTIONS.map(({ mode, icon }) => {
              const selected = themeMode === mode;
              return (
                <TouchableOpacity
                  key={mode}
                  accessibilityRole="radio"
                  accessibilityState={{ selected }}
                  activeOpacity={0.7}
                  onPress={() => changeMode(mode)}
                  className="flex-1 flex-row items-center justify-center h-9 rounded-lg"
                  style={selected ? { backgroundColor: palette.accent } : undefined}
                >
                  <Icon name={icon} size={15} color={selected ? "#FFFFFF" : c.textSecondary} />
                  <Text
                    className="ml-1.5 text-sm font-medium"
                    style={{ color: selected ? "#FFFFFF" : c.textSecondary }}
                  >
                    {t.themes[mode]}
                  </Text>
                </TouchableOpacity>
              );
            })}
          </View>
        </View>
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
        <Row
          icon="people-outline"
          label={translations[language || "en-EN"].screens.Accounts.switch}
          onPress={openSwitcher}
          right={<Icon name="chevron-forward" size={18} color={c.textMuted} />}
        />
        <Row icon="log-out-outline" label={t.logout} onPress={closeSession} danger last />
      </View>

      <OptionSheet
        visible={languageSheet}
        title={t.language}
        options={languageOptions}
        selected={languageMode}
        onSelect={changeLanguage}
        onClose={() => setLanguageSheet(false)}
      />
    </View>
  );
};

export default SettingsScreen;
