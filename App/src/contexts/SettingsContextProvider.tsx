import { setErrorLanguage } from "../utils/graphql";
import React, {
  useEffect,
  createContext,
  Dispatch,
  SetStateAction,
  useCallback,
  useContext,
  useMemo,
  useState,
} from "react";
import { useColorScheme } from "react-native";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { deviceLanguage, isSupportedLanguage } from "../utils/deviceSettings";

/** "system" follows the phone's light/dark mode. */
export type ThemeMode = "system" | "light" | "dark";

/** "system" follows the phone's language; otherwise a key of translations. */
export type LanguageMode = "system" | string;

export interface AppContextType {
  /** Language actually in use, after resolving "system". */
  language: string;
  languageMode: LanguageMode;
  /** Changes the language and remembers the choice. */
  setLanguageMode: (mode: LanguageMode) => Promise<void>;
  /** Theme actually in use, after resolving "system". */
  darkmode: boolean;
  themeMode: ThemeMode;
  /** Changes the theme and remembers the choice. */
  setThemeMode: (mode: ThemeMode) => Promise<void>;
  lefthand: boolean;
  setLefthand: Dispatch<SetStateAction<boolean>>;
}

export const AppContext = createContext<AppContextType>({} as AppContextType);

/**
 * App settings. Language and theme follow the phone (its language if the app
 * has it, its light/dark mode live) unless the user picks one in Settings.
 */
const SettingsContextProvider = (props: any) => {
  const systemScheme = useColorScheme();
  const [languageMode, setLanguageModeState] = useState<LanguageMode>("system");
  const [themeMode, setThemeModeState] = useState<ThemeMode>("system");
  const [lefthand, setLefthand] = useState<boolean>(false);

  const systemLanguage = useMemo(deviceLanguage, []);
  const language = languageMode === "system" ? systemLanguage : languageMode;
  const darkmode = themeMode === "system" ? systemScheme === "dark" : themeMode === "dark";

  // Stored under "language"; no value means follow the phone.
  const setLanguageMode = useCallback(async (mode: LanguageMode) => {
    setLanguageModeState(mode);
    if (mode === "system") {
      await AsyncStorage.removeItem("language");
    } else {
      await AsyncStorage.setItem("language", mode);
    }
  }, []);

  // Stored under "darkMode" ("true"/"false"); no value means follow the phone.
  const setThemeMode = useCallback(async (mode: ThemeMode) => {
    setThemeModeState(mode);
    if (mode === "system") {
      await AsyncStorage.removeItem("darkMode");
    } else {
      await AsyncStorage.setItem("darkMode", mode === "dark" ? "true" : "false");
    }
  }, []);

  useEffect(() => {
    const loadSaved = async () => {
      try {
        const [savedLanguage, savedDarkmode, savedLefthand] = await Promise.all([
          AsyncStorage.getItem("language"),
          AsyncStorage.getItem("darkMode"),
          AsyncStorage.getItem("lefthand"),
        ]);
        if (isSupportedLanguage(savedLanguage)) setLanguageModeState(savedLanguage);
        if (savedDarkmode !== null) setThemeModeState(savedDarkmode === "true" ? "dark" : "light");
        setLefthand(savedLefthand === "true");
      } catch {
        // Keep the phone's settings if storage can't be read.
      }
    };
    loadSaved();
  }, []);

  useEffect(() => setErrorLanguage(language), [language]);

  const contextValues: AppContextType = {
    language: language,
    languageMode: languageMode,
    setLanguageMode: setLanguageMode,
    darkmode: darkmode,
    themeMode: themeMode,
    setThemeMode: setThemeMode,
    lefthand: lefthand,
    setLefthand: setLefthand,
  };

  return (
    <AppContext.Provider value={contextValues}>
      {props.children}
    </AppContext.Provider>
  );
};

export const useSettingsContext = () => {
  return useContext(AppContext);
};

export default SettingsContextProvider;
