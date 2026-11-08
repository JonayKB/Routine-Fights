import { translations } from "../../translations/translation";

// Set to true to point a debug build on the Android emulator at an API running on this machine.
const USE_LOCAL_API = false;
export const uri: string =
  __DEV__ && USE_LOCAL_API ? "http://10.0.2.2:8080" : "https://routinefights.jonaykb.com";
export const neo4jUri: string = uri + "/graphql";
export const limit: number = 10;

export const resetNavigation = (navigation: any, path: string) => {
  navigation.navigate(path);
  navigation.reset({
    index: 0,
    routes: [{ name: path }],
  });
};

export const convertQuantityToString = (quantity: number): string => {
  if (quantity >= 1000000) {
    const num = quantity / 1000000;
    return parseFloat(num.toFixed(2)).toString() + "M";
  } else if (quantity >= 1000) {
    const num = quantity / 1000;
    return parseFloat(num.toFixed(2)).toString() + "K";
  }
  return quantity?.toString();
};

/**
 * Languages the app is translated to. `nativeName` is how speakers of each
 * language write it, so people can find theirs whatever the current language.
 */
export const LANGUAGES = [
  { value: "en-EN", nativeName: "English", key: "english" },
  { value: "es-ES", nativeName: "Español", key: "spanish" },
  { value: "fr-FR", nativeName: "Français", key: "french" },
  { value: "de-DE", nativeName: "Deutsch", key: "german" },
] as const;

export const nativeLanguageName = (value: string) =>
  LANGUAGES.find((l) => l.value === value)?.nativeName ?? value;

export const borderColor = (darkmode: boolean) =>
  darkmode ? "border-[#EFE9DE1A]" : "border-[#1F1B161A]";
export const bgColor = (darkmode: boolean) =>
  darkmode ? "bg-[#161513]" : "bg-[#F7F4EE]";
export const cardBgColor = (darkmode: boolean) =>
  darkmode ? "bg-[#1C1A18]" : "bg-white";
export const raisedBgColor = (darkmode: boolean) =>
  darkmode ? "bg-[#262421]" : "bg-[#EFE9DF]";
export const iconColor = (darkmode: boolean) =>
  darkmode ? "#AAA398" : "#5E554A";
export const textColor = (darkmode: boolean) =>
  darkmode ? "text-[#EFE9DE]" : "text-[#1F1B16]";
export const secondaryTextColor = (darkmode: boolean) =>
  darkmode ? "text-[#AAA398]" : "text-[#5E554A]";
export const mutedTextColor = (darkmode: boolean) =>
  darkmode ? "text-[#8A847A]" : "text-[#7B7266]";
