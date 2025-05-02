import { Text, TouchableOpacity } from "react-native";
import React from "react";
import style from "../styles/Styles.json";

type Props = {
  navigateFunction: () => void;
};

const AddButton = (props: Props) => {
  return (
    <TouchableOpacity
      onPress={props.navigateFunction}
      className={style.screens.Activities.addButton}
    >
      <Text
        className={style.screens.Activities.addButtonText}
        style={{ fontFamily: "InriaSans-Regular" }}
      >
        +
      </Text>
    </TouchableOpacity>
  );
};

export default AddButton;
