import React from "react";
import { View } from "react-native";
import { useSafeAreaInsets } from "react-native-safe-area-context";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { colors } from "../styles/theme";

/**
 * Keeps every screen below the status bar, the notch and the Dynamic Island.
 * On Android the status bar is opaque, so the inset there is 0.
 */
const SafeTop = ({ children }: { children: React.ReactNode }) => {
  const insets = useSafeAreaInsets();
  const { darkmode } = useSettingsContext();
  return (
    <View style={{ flex: 1, paddingTop: insets.top, backgroundColor: colors(darkmode).page }}>{children}</View>
  );
};

export default SafeTop;
