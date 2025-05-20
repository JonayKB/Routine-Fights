import { Text, TouchableOpacity } from "react-native";
import React from "react";

type Props = {
  navigateFunction: () => void;
};

const AddButton = (props: Props) => {
  return (
    <TouchableOpacity
      onPress={props.navigateFunction}
      className="w-11/12 mx-auto mt-8 py-4 rounded-3xl bg-[#F65261]"
    >
      <Text className="text-8xl font-bold text-white text-center">+</Text>
    </TouchableOpacity>
  );
};

export default AddButton;
