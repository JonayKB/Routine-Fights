import { View, Text, TouchableOpacity } from "react-native";
import React from "react";
import Icon from "react-native-vector-icons/Ionicons";

type Props = {
  name: string;
  description: string;
  streak: number;
  timesRemaining: number;
  unsubscribeFunction: () => void;
};

const Streak = (props: Props) => {
  return (
    <View className="flex-row items-center bg-[#E4D8E9] w-11/12 h-36 rounded-3xl mt-5 mx-auto">
      <View className="w-20 h-20 bg-blue-400 rounded-full m-5 items-center justify-center">
        <Text className="text-3xl text-black">{props.streak}</Text>
      </View>
      <View>
        <Text className="text-black text-3xl">{props.name}</Text>
        <Text className="text-black text-xl">{props.description}</Text>
        <Text className="text-black text-2xl mt-4">Times remaining: {props.timesRemaining}</Text>
      </View>
      <TouchableOpacity
        onPress={props.unsubscribeFunction}
        className="ml-auto mr-5"
      >
        <Icon name="trash" size={30} color="black" />
      </TouchableOpacity>
    </View>
  );
};

export default Streak;
