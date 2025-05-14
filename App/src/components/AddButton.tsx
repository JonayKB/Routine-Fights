import { Text, TouchableOpacity } from "react-native";
import React from "react";

type Props = {
  navigateFunction: () => void;
};

const AddButton = (props: Props) => {
  return (
    <TouchableOpacity
      onPress={props.navigateFunction}
      className="w-11/12 mx-auto mb-4 rounded-xl bg-[#E4007C] justify-center items-center"
    >
      <Text
        className="text-white text-8xl font-bold text-center"
        style={{ fontFamily: "InriaSans-Regular" }}
      >
        +
      </Text>
    </TouchableOpacity>
  );
};

export default AddButton;
