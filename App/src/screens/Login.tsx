import { View, Text, TextInput, TouchableOpacity, Alert } from "react-native";
import React, { useState } from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { LoginStackProps } from "../navigation/LoginNavigation";
import { UserIn } from "../utils/User";

type Props = NativeStackScreenProps<LoginStackProps, "Login">;

const Login = ({ navigation }: Props) => {
  const [userIn, setUserIn] = useState<UserIn>({} as UserIn);
  const [passwordShown, setPasswordShown] = useState<boolean>(false);

  const login = () => {
    Alert.alert("Login", JSON.stringify(userIn));
  };

  const navigate = (path: "Register" | "Home") => {
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
        style={{ height: 400 }}
      >
        <View className="m-10 mb-5">
          <TextInput
            placeholder="Email"
            placeholderTextColor="#4B0082"
            inputMode="email"
            className="border-[#4B0082] border-2 rounded-lg bg-[#F8F7FE] text-2xl mb-5 pl-3 text-black"
            onChangeText={(text) => setUserIn({ ...userIn, email: text })}
          />
          <TextInput
            placeholder="Password"
            placeholderTextColor="#4B0082"
            secureTextEntry={!passwordShown}
            className="border-[#4B0082] border-2 rounded-lg bg-[#F8F7FE] text-2xl pl-3 text-black"
            onChangeText={(text) => setUserIn({ ...userIn, password: text })}
          />
          <TouchableOpacity onPress={() => setPasswordShown(!passwordShown)}>
            <Text
              className="text-[#4B0082] font-bold text-lg mt-3"
              style={{ fontFamily: "InriaSans-Regular" }}
            >
              {passwordShown ? "Hide" : "Show"}
            </Text>
          </TouchableOpacity>
        </View>
        <View className="m-10 mt-5">
          <TouchableOpacity
            onPress={login}
            className="bg-[#E4007C] rounded-lg py-3 mb-5"
          >
            <Text
              className="text-white font-bold text-3xl text-center"
              style={{ fontFamily: "InriaSans-Regular" }}
            >
              Login
            </Text>
          </TouchableOpacity>
          <TouchableOpacity
            onPress={() => navigate("Register")}
            className="border-[#E4007C] border-2 rounded-lg py-1"
          >
            <Text className="text-[#4B0082] font-bold text-2xl text-center">
              Register
            </Text>
          </TouchableOpacity>
        </View>
      </View>
    </View>
  );
};

export default Login;
