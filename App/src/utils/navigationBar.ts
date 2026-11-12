import { NativeModules, Platform } from "react-native";
import { colors } from "../styles/theme";

/**
 * Paints Android's system navigation bar (gesture/buttons bar) with the
 * app's surface colour so it follows the in-app theme, not only the phone's.
 */
export const setNavigationBarTheme = (darkmode: boolean) => {
  if (Platform.OS !== "android") return;
  NativeModules.NavigationBar?.setColor(colors(darkmode).card, !darkmode);
  // The window shows through during screen transitions.
  NativeModules.NavigationBar?.setWindowBackground?.(colors(darkmode).page);
};
