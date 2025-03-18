import React, { useState } from "react";
import { Dropdown } from "react-native-element-dropdown";
import { styles } from "../styles/DropDownStyles";

type Props = {};

const DropDown = (props: Props) => {
  const [value, setValue] = useState(null);
  const [isFocus, setIsFocus] = useState(false);

  const categories = [
    {
      label: "Category 1",
      value: "1",
    },
    {
      label: "Category 2",
      value: "2",
    },
    {
      label: "Category 3",
      value: "3",
    },
  ];

  return (
    <Dropdown
      style={styles.dropdown}
      placeholderStyle={styles.placeholderStyle}
      selectedTextStyle={styles.selectedTextStyle}
      inputSearchStyle={styles.inputSearchStyle}
      itemTextStyle={{ color: "black" }}
      
      data={categories}
      search
      mode="modal"
      value={value}
      labelField="label"
      valueField="value"
      placeholder={!isFocus ? "Select category" : "..."}
      searchPlaceholder="Search"
      onChange={(item) => {
        setValue(item.value);
      }}
    />
  );
};

export default DropDown;
