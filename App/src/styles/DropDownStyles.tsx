import { StyleSheet } from "react-native";

export const styles = StyleSheet.create({
  container: {
    backgroundColor: "white",
    padding: 16,
  },

  dropdown: {
    height: 50,
    borderColor: "gray",
    borderWidth: 0.5,
    borderRadius: 8,
    paddingHorizontal: 8,
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
    color: "black",
  },

  inputSearchStyle: {
    height: 40,
    fontSize: 16,
    color: "black",
  },
});
