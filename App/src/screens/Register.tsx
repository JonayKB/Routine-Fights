import { View, Text, TextInput, TouchableOpacity, Alert } from "react-native";
import React, { useRef, useState } from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { LoginStackProps } from "../navigation/LoginNavigation";
import PhoneInput from "react-native-phone-number-input";
import { UserIn } from "../utils/User";
import axios from "axios";

type Props = NativeStackScreenProps<LoginStackProps, "Register">;

const Register = ({ navigation, route }: Props) => {
  const uri = "http://64.226.71.234:8080/auth/register";
  const [userIn, setuserIn] = useState<UserIn>({} as UserIn);
  const phoneInput = useRef<PhoneInput>(null);
  const [password, setPassword] = useState<string>(null);

  const navigate = (path: "Login" | "Home") => {
    navigation.navigate(path);
    navigation.reset({
      index: 0,
      routes: [{ name: path }],
    });
  };

  async function validateForm() {
    if (
      !userIn.username ||
      !userIn.email ||
      !userIn.password ||
      !userIn.phoneNumber
    ) {
      return Alert.alert("Missing data");
    }

    if (password !== userIn.password) {
      return Alert.alert("Passwords do not match");
    }

    // If any unique field is found on another user in the database, return an error
    await register();
  }

  const register = async () => {
    try {
      const { status } = await axios.post(uri, userIn);

      if (status === 200) {
        navigate("Home");
      }
    } catch (error) {
      console.log("Error", error);
    }
  };

  return (
    <View className="flex-1 bg-[#E4DCE9] justify-center items-center">
      <View
        className="justify-evenly bg-white rounded-2xl w-96"
        style={{ height: 600 }}
      >
        <View className="m-10">
          <TextInput
            placeholder="Username"
            placeholderTextColor="#4B0082"
            className="border-[#4B0082] border-2 rounded-lg bg-[#F8F7FE] text-lg mb-5 pl-3 text-black"
            onChangeText={(text) => setuserIn({ ...userIn, username: text })}
          />
          <TextInput
            placeholder="Email"
            placeholderTextColor="#4B0082"
            inputMode="email"
            className="border-[#4B0082] border-2 rounded-lg bg-[#F8F7FE] text-lg mb-5 pl-3 text-black"
            onChangeText={(text) => setuserIn({ ...userIn, email: text })}
          />
          <TextInput
            placeholder="Password"
            placeholderTextColor="#4B0082"
            secureTextEntry={true}
            className="border-[#4B0082] border-2 rounded-lg bg-[#F8F7FE] text-lg mb-5 pl-3 text-black"
            onChangeText={(text) => setuserIn({ ...userIn, password: text })}
          />
          <TextInput
            placeholder="Confirm password"
            placeholderTextColor="#4B0082"
            secureTextEntry={true}
            className="border-[#4B0082] border-2 rounded-lg bg-[#F8F7FE] text-lg mb-5 pl-3 text-black"
            onChangeText={(text) => setPassword(text)}
          />
          <View className="border-[#4B0082] border-2 rounded-lg bg-[#F8F7FE] text-lg mb-5 pl-3">
            <PhoneInput
              ref={phoneInput}
              defaultCode="ES"
              layout="first"
              containerStyle={{
                width: "100%",
                height: 60,
                marginLeft: -8,
                backgroundColor: "#F8F7FE",
              }}
              textInputStyle={{
                height: 60,
                fontSize: 16,
                backgroundColor: "#F8F7FE",
              }}
              onChangeFormattedText={(text) => {
                setuserIn({ ...userIn, phoneNumber: text });
              }}
            />
          </View>
        </View>
        <View className="m-10">
          <TouchableOpacity
            onPress={validateForm}
            className="bg-[#E4007C] rounded-lg py-3 mb-5"
          >
            <Text className="text-white font-bold text-xl text-center">
              Register
            </Text>
          </TouchableOpacity>
          <TouchableOpacity
            onPress={() => navigate("Login")}
            className="border-[#E4007C] border-2 rounded-lg py-1"
          >
            <Text className="text-[#4B0082] font-bold text-lg text-center">
              Login
            </Text>
          </TouchableOpacity>
        </View>
      </View>
    </View>
  );
};

export default Register;
