import { View, Text } from "react-native";
import React from "react";

type Props = {
  date: string;
  textStyle?: string;
  containerStyle?: string;
};

const DateFormatString = (props: Props) => {
  return (
    <View className={`flex-row justify-between mt-2 ${props.containerStyle}`}>
      <Text className={`text-black ${props.textStyle}`}>
        {props.date?.slice(12, 16)}
      </Text>
      <Text className={`text-black ml-2 ${props.textStyle}`}>
        {props.date?.slice(0, 10)}
      </Text>
    </View>
  );
};

export default DateFormatString;
