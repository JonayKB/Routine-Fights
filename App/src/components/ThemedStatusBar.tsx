import React from "react";
import { StatusBar } from "react-native";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { colors } from "../styles/theme";

/** Paints the system status bar with the page colour of the current theme. */
const ThemedStatusBar = () => {
  const { darkmode } = useSettingsContext();
  return (
    <StatusBar
      backgroundColor={colors(darkmode).page}
      barStyle={darkmode ? "light-content" : "dark-content"}
    />
  );
};

export default ThemedStatusBar;
