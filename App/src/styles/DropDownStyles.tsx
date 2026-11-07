import { StyleSheet } from "react-native";
import { colors } from "./theme";

export const styles = (darkmode: boolean, bare = false) => {
  const c = colors(darkmode);
  return StyleSheet.create({
    dropdown: bare
      ? { height: 52, paddingHorizontal: 12 }
      : {
          height: 48,
          borderColor: c.hairline,
          borderWidth: 1,
          borderRadius: 12,
          paddingHorizontal: 16,
          backgroundColor: c.raised,
        },
    placeholderStyle: {
      fontSize: 16,
      color: c.textMuted,
    },
    selectedTextStyle: {
      fontSize: 16,
      color: c.text,
    },
    inputSearchStyle: {
      height: 40,
      fontSize: 16,
      color: "#1F1B16",
      borderRadius: 8,
    },
  });
};
