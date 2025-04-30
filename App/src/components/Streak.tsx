import { View, Text } from "react-native";
import React from "react";

type Props = {
    name: string;
    description: string;
    streak: number;
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
      </View>
    </View>
  );
};

export default Streak;
