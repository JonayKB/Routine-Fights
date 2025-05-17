import { View, Text, TouchableOpacity } from "react-native";
import React from "react";
import Icon from "react-native-vector-icons/Ionicons";

type Props = {
  message: string;
  navigation: any;
};

const ProfileNavigation = (props: Props) => {
  return (
    <View className="flex-row bg-[#F1FEFC] border-b-2 border-[#4B0082] p-5">
      <TouchableOpacity onPress={() => props.navigation.goBack()}>
        <Icon name="arrow-back" size={30} color="#4B0082" />
      </TouchableOpacity>
      <Text className="text-[#4B0082] font-bold text-2xl ml-5">{props.message}</Text>
    </View>
  );
};

export default ProfileNavigation;
