import { View, Text } from "react-native";
import React from "react";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { textColor } from "../utils/Utils";

type Props = {
  date: string;
  textStyle?: string;
  containerStyle?: string;
};

const DateFormatString = (props: Props) => {
  const { darkmode } = useSettingsContext();

  return (
    <View className={`flex-row justify-between mt-2 ${props.containerStyle}`}>
      <Text className={`${textColor(darkmode)} ${props.textStyle}`}>
        {props.date?.slice(12, 16)}
      </Text>
      <Text className={`${textColor(darkmode)} ml-2 ${props.textStyle}`}>
        {props.date?.slice(0, 10)}
      </Text>
    </View>
  );
};

export default DateFormatString;
