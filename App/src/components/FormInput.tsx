import { TextInput } from "react-native";
import React from "react";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { borderColor, textColor } from "../utils/Utils";

type Props = {
  setText: (text: string) => void;
  name: string;
  label: string;
  mode: TextInput["props"]["inputMode"];
};

const FormInput = (props: Props) => {
  const { darkmode } = useSettingsContext();

  return (
    <TextInput
      placeholder={props.label}
      placeholderTextColor={darkmode ? "#E0D3F5" : "#4B0082"}
      inputMode={props.mode}
      className={`text-lg mb-5 pl-3 rounded-lg border-2 ${
        darkmode ? "bg-[#4B294F]" : "bg-[#F8F7FE]"
      } ${textColor(darkmode)} ${borderColor(darkmode)}`}
      onChangeText={(text) => props.setText(text)}
      value={props.name}
    />
  );
};

export default FormInput;
