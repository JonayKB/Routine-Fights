import { StyleSheet } from "react-native";

export const styles = (darkmode: boolean) =>
  StyleSheet.create({
    dropdown: {
      height: 50,
      borderColor: darkmode ? "#B28DFF" : "#7D3C98",
      borderWidth: 2,
      borderRadius: 12,
      paddingHorizontal: 10,
      backgroundColor: darkmode ? "#4B294F" : "#F8F7FE",
    },
    placeholderStyle: {
      fontSize: 16,
      color: darkmode ? "#E0D3F5" : "#4B0082",
    },
    selectedTextStyle: {
      fontSize: 16,
      color: darkmode ? "white" : "black",
    },
    inputSearchStyle: {
      height: 40,
      fontSize: 16,
      color: "black",
    },
  });
