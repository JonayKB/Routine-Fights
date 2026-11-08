import { NativeModules, Platform } from "react-native";
import { translations } from "../../translations/translation";

export const DEFAULT_LANGUAGE = "en-EN";

export const isSupportedLanguage = (value: string | null | undefined): value is string =>
  !!value && value in translations;

/** Locale tags the phone reports, most preferred first (e.g. "es-ES", "fr_FR"). */
const deviceLocales = (): string[] => {
  const locales: (string | undefined)[] = [];
  try {
    locales.push(Intl.DateTimeFormat().resolvedOptions().locale);
  } catch {
    // Intl not available on this engine.
  }
  if (Platform.OS === "ios") {
    const settings = NativeModules.SettingsManager?.settings;
    locales.push(...(settings?.AppleLanguages ?? []), settings?.AppleLocale);
  } else {
    locales.push(NativeModules.I18nManager?.localeIdentifier);
  }
  return locales.filter((l): l is string => !!l);
};

/**
 * The phone's language mapped to one the app is translated to ("es-MX" → "es-ES").
 * Falls back to English when the phone uses a language the app doesn't have.
 */
export const deviceLanguage = (): string => {
  const supported = Object.keys(translations);
  for (const locale of deviceLocales()) {
    const code = locale.replace("_", "-").split("-")[0].toLowerCase();
    const match = supported.find((key) => key.split("-")[0] === code);
    if (match) return match;
  }
  return DEFAULT_LANGUAGE;
};

/**
 * Country the phone is set to (ISO code such as "ES"), taken from its locale
 * ("es_ES" → "ES"). Undefined when the locale has no region.
 */
export const deviceRegion = (): string | undefined => {
  for (const locale of deviceLocales()) {
    const region = locale
      .replace(/_/g, "-")
      .split("-")
      .slice(1)
      .find((part) => /^[A-Za-z]{2}$/.test(part));
    if (region) return region.toUpperCase();
  }
  return undefined;
};
