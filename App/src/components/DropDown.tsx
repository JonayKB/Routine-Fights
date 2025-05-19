import React, { useState } from "react";
import { Dropdown } from "react-native-element-dropdown";
import { styles } from "../styles/DropDownStyles";
import { useSettingsContext } from "../contexts/SettingsContextProvider";

type Props = {
  message: string;
  data: {
    label: string;
    value: string;
  }[];
  value: string;
  setValue: (value: string) => void;
  onFocus?: () => void;
};

const DropDown = (props: Props) => {
  const [isFocus, setIsFocus] = useState(false);
  const { darkmode } = useSettingsContext();

  return (
    <Dropdown
      style={styles(darkmode).dropdown}
      placeholderStyle={styles(darkmode).placeholderStyle}
      selectedTextStyle={styles(darkmode).selectedTextStyle}
      inputSearchStyle={styles(darkmode).inputSearchStyle}
      itemTextStyle={{ color: "black" }}
      data={props.data}
      search
      mode="modal"
      value={props.value}
      labelField="label"
      valueField="value"
      placeholder={!isFocus ? props.message : "..."}
      searchPlaceholder="Search"
      onChange={(item) => {
        props.setValue(item.value);
      }}
      onFocus={props.onFocus}
    />
  );
};

export default DropDown;
