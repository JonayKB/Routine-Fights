import { TextInput } from "react-native";
import React from "react";

type Props = {
  setText: (text: string) => void;
  name: string;
  label: string;
  mode: TextInput["props"]["inputMode"];
};

const FormInput = (props: Props) => {
  return (
    <TextInput
      placeholder={props.label}
      placeholderTextColor="#4B0082"
      inputMode={props.mode}
      className="border-[#4B0082] border-2 rounded-lg bg-[#F8F7FE] text-lg mb-5 pl-3 text-black"
      onChangeText={(text) => props.setText(text)}
      value={props.name}
    />
  );
};

export default FormInput;
