import React, { useState } from "react";
import { Dropdown } from "react-native-element-dropdown";
import { styles } from "../styles/DropDownStyles";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { translations } from "../../translations/translation";

type Props = {
  message: string;
  data: {
    label: string;
    value: string;
  }[];
  value: string;
  setValue: (value: string) => void;
  onFocus?: () => void;
  /** Render without its own border and background, e.g. inside a list row. */
  bare?: boolean;
};

const DropDown = (props: Props) => {
  const [isFocus, setIsFocus] = useState(false);
  const { darkmode, language } = useSettingsContext();
  const st = styles(darkmode, props.bare);

  return (
    <Dropdown
      style={st.dropdown}
      placeholderStyle={st.placeholderStyle}
      selectedTextStyle={st.selectedTextStyle}
      inputSearchStyle={st.inputSearchStyle}
      itemTextStyle={{ color: "#1F1B16" }}
      data={props.data}
      search
      mode="modal"
      value={props.value}
      labelField="label"
      valueField="value"
      placeholder={!isFocus ? props.message : "..."}
      searchPlaceholder={translations[language || "en-EN"].screens.Home.search}
      onChange={(item) => {
        props.setValue(item.value);
      }}
      onFocus={props.onFocus}
    />
  );
};

export default DropDown;
