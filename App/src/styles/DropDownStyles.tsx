import { StyleSheet } from "react-native";
import { useSettingsContext } from "../contexts/SettingsContextProvider";

const { darkmode } = useSettingsContext();

export const styles = StyleSheet.create({
  dropdown: {
    height: 50,
    borderColor: "#4B0082",
    borderWidth: 2,
    borderRadius: 12,
    paddingHorizontal: 10,
    backgroundColor: darkmode ? "#4B294F" : "#F8F7FE",
  },
  placeholderStyle: {
    fontSize: 16,
    color: darkmode ? "#BFAFD8" : "black",
  },
  selectedTextStyle: {
    fontSize: 16,
    color: "#4B0082",
  },
  inputSearchStyle: {
    height: 40,
    fontSize: 16,
    color: darkmode ? "white" : "black",
  },
});
