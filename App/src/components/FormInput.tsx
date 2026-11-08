import { Text, TextInput, TouchableOpacity, View } from "react-native";
import React, { useState } from "react";
import Icon from "react-native-vector-icons/Ionicons";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import {
  borderColor,
  iconColor,
  mutedTextColor,
  raisedBgColor,
  secondaryTextColor,
  textColor,
} from "../utils/Utils";
import { colors } from "../styles/theme";

type Props = {
  setText: (text: string) => void;
  name: string;
  label: string;
  mode: TextInput["props"]["inputMode"];
  /** Hide the value and show an eye toggle. */
  secure?: boolean;
  /** Message shown under the field; also turns its border red. */
  error?: string | null;
  /** Label shown above the field. Without it the label is the placeholder. */
  title?: string;
  /** Help text under the field, hidden while an error is shown. */
  hint?: string;
};

const FormInput = (props: Props) => {
  const { darkmode } = useSettingsContext();
  const [hidden, setHidden] = useState(true);
  const [focused, setFocused] = useState(false);

  return (
    <View className="mb-3">
    {!!props.title && (
      <Text className={`mb-1.5 text-sm font-medium ${secondaryTextColor(darkmode)}`}>{props.title}</Text>
    )}
    <View
      className={`flex-row items-center h-12 px-4 rounded-xl border ${raisedBgColor(
        darkmode
      )} ${
        props.error ? "border-[#E5484D]" : focused ? "border-[#C2410C]" : borderColor(darkmode)
      }`}
    >
      <TextInput
        placeholder={props.title ? undefined : props.label}
        accessibilityLabel={props.title ?? props.label}
        placeholderTextColor={colors(darkmode).textMuted}
        inputMode={props.mode}
        autoCapitalize={props.mode === "email" ? "none" : undefined}
        secureTextEntry={props.secure && hidden}
        onFocus={() => setFocused(true)}
        onBlur={() => setFocused(false)}
        className={`flex-1 text-base py-0 ${textColor(darkmode)}`}
        onChangeText={(text) => props.setText(text)}
        value={props.name}
      />
      {props.secure && (
        <TouchableOpacity onPress={() => setHidden(!hidden)} hitSlop={10}>
          <Icon
            name={hidden ? "eye-outline" : "eye-off-outline"}
            size={18}
            color={iconColor(darkmode)}
          />
        </TouchableOpacity>
      )}
    </View>
    {!props.error && !!props.hint && (
      <Text className={`mt-1.5 ml-1 text-xs ${mutedTextColor(darkmode)}`}>{props.hint}</Text>
    )}
    {!!props.error && (
      <View className="flex-row items-center mt-1.5 ml-1">
        <Icon name="alert-circle" size={14} color="#E5484D" />
        <Text className="ml-1.5 text-xs text-[#E5484D]">{props.error}</Text>
      </View>
    )}
    </View>
  );
};

export default FormInput;
