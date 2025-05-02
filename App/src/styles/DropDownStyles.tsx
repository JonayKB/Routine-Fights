import { StyleSheet } from "react-native";

export const styles = StyleSheet.create({
  container: {
    backgroundColor: "white",
    padding: 16,
  },

  dropdown: {
    height: 50,
    borderColor: "#4B0082",
    borderWidth: 2,
    borderRadius: 8,
    paddingHorizontal: 8,
    backgroundColor: "#F8F7FE",
  },

  label: {
    position: "absolute",
    backgroundColor: "black",
    left: 22,
    top: 8,
    zIndex: 999,
    paddingHorizontal: 8,
    fontSize: 14,
    color: "black",
  },

  placeholderStyle: {
    fontSize: 16,
    color: "black",
  },

  selectedTextStyle: {
    fontSize: 16,
    color: "#4B0082",
  },

  inputSearchStyle: {
    height: 40,
    fontSize: 16,
    color: "black",
  },
});
