import { translations } from "../../translations/translation";

export const uri: string = "http://routinefights.ddns.net:6342";
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

export const languages = (language: string) => [
  {
    label:
      translations[language || "en-EN"].screens.Settings.languages["english"],
    value: "en-EN",
  },
  {
    label:
      translations[language || "en-EN"].screens.Settings.languages["spanish"],
    value: "es-ES",
  },
  {
    label:
      translations[language || "en-EN"].screens.Settings.languages["french"],
    value: "fr-FR",
  },
  {
    label:
      translations[language || "en-EN"].screens.Settings.languages["german"],
    value: "de-DE",
  },
];

export const borderColor = (darkmode: boolean) =>
  darkmode ? "border-[#B28DFF]" : "border-[#7D3C98]";
export const bgColor = (darkmode: boolean) =>
  darkmode ? "bg-[#1C1C1E]" : "bg-[#FCFCFC]";
export const cardBgColor = (darkmode: boolean) =>
  darkmode ? "bg-[#4B294F]" : "bg-[#E8E2F0]";
export const iconColor = (darkmode: boolean) =>
  darkmode ? "#B28DFF" : "#7D3C98";
export const textColor = (darkmode: boolean) =>
  darkmode ? "text-white" : "text-[#1C1C1E]";
