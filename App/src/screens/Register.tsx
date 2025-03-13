import { View, Text, TextInput, TouchableOpacity } from "react-native";
import React from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { LoginStackProps } from "../navigation/LoginNavigation";
import SelectDropdown from "react-native-select-dropdown";
import Icon from "react-native-vector-icons/MaterialCommunityIcons";

type Props = NativeStackScreenProps<LoginStackProps, "Register">;
const emojisWithIcons = [{ title: "happy", icon: "emoticon-happy-outline" }];

const Register = ({ navigation, route }: Props) => {
  const navigate = (path: "Login" | "Home") => {
    navigation.navigate(path);
    navigation.reset({
      index: 0,
      routes: [{ name: path }],
    });
  };

  return (
    <View className="flex-1 bg-[#E4DCE9] justify-center items-center">
      <View
        className="justify-evenly bg-white rounded-2xl w-96"
        style={{ height: 413 }}
      >
        <View className="m-10">
          <TextInput
            placeholder="Username"
            placeholderTextColor="#4B0082"
            className="border-[#4B0082] border-2 rounded-lg bg-[#F8F7FE] text-2xl mb-5 pl-3"
          />
          <TextInput
            placeholder="Email"
            placeholderTextColor="#4B0082"
            inputMode="email"
            className="border-[#4B0082] border-2 rounded-lg bg-[#F8F7FE] text-2xl mb-5 pl-3"
          />
          <TextInput
            placeholder="Password"
            placeholderTextColor="#4B0082"
            secureTextEntry={true}
            className="border-[#4B0082] border-2 rounded-lg bg-[#F8F7FE] text-2xl pl-3"
          />
          <TextInput
            placeholder="Confirm password"
            placeholderTextColor="#4B0082"
            secureTextEntry={true}
            className="border-[#4B0082] border-2 rounded-lg bg-[#F8F7FE] text-2xl pl-3"
          />
          <SelectDropdown
            data={emojisWithIcons}
            onSelect={(selectedItem, index) => {
              console.log(selectedItem, index);
            }}
            renderButton={(selectedItem, isOpened) => {
              return (
                <View
                  className="border-[#4B0082] border-2 rounded-lg bg-[#F8F7FE] text-2xl pl-3"
                  style={{ height: 50 }}
                >
                  {selectedItem && (
                    <View>
                      <Icon
                        name={selectedItem.icon}
                        size={30}
                        color="#4B0082"
                      />
                      <Text>{selectedItem.title}</Text>
                    </View>
                  )}
                  <Icon
                    name={isOpened ? "chevron-up" : "chevron-down"}
                    size={30}
                    color="#4B0082"
                  />
                </View>
              );
            }}
            renderItem={(item, isSelected) => {
              return (
                <View
                  style={{
                    backgroundColor: isSelected && "#D2D9DF",
                  }}
                >
                  <Icon name={item.icon} size={30} color="#4B0082" />
                  <Text>{item.title}</Text>
                </View>
              );
            }}
          />
          <TextInput
            placeholder="Phone number"
            placeholderTextColor="#4B0082"
            secureTextEntry={true}
            className="border-[#4B0082] border-2 rounded-lg bg-[#F8F7FE] text-2xl pl-3"
          />
        </View>
        <View className="m-10">
          <TouchableOpacity className="bg-[#E4007C] rounded-lg py-3 mb-5">
            <Text className="text-white font-bold text-3xl text-center">
              Register
            </Text>
          </TouchableOpacity>
          <TouchableOpacity
            onPress={() => navigate("Login")}
            className="border-[#E4007C] border-2 rounded-lg py-1"
          >
            <Text className="text-[#4B0082] font-bold text-2xl text-center">
              Login
            </Text>
          </TouchableOpacity>
        </View>
      </View>
    </View>
  );
};

export default Register;
