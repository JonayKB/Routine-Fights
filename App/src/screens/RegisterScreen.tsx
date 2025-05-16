import { View, Text, TextInput, TouchableOpacity, Alert } from "react-native";
import React, { useRef, useState } from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { LoginStackProps } from "../navigation/LoginStackNavigation";
import PhoneInput from "react-native-phone-number-input";
import { UserIn } from "../utils/User";
import { resetNavigation } from "../utils/Utils";
import { translations } from "../../translations/translation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { register } from "../repositories/RegisterRepository";

type Props = NativeStackScreenProps<LoginStackProps, "Register">;

const RegisterScreen = ({ navigation, route }: Props) => {
  const [passwordShown, setPasswordShown] = useState<boolean>(false);
  const [userIn, setuserIn] = useState<UserIn>({} as UserIn);
  const phoneInput = useRef<PhoneInput>(null);
  const [confirmPassword, setConfirmPassword] = useState<string>(null);
  const { language, darkmode } = useSettingsContext();

  const validateForm = async () => {
    if (
      !userIn.username ||
      !userIn.email ||
      !userIn.password ||
      !userIn.phoneNumber
    ) {
      return Alert.alert("Missing data");
    }

    if (confirmPassword !== userIn.password) {
      return Alert.alert("Passwords do not match");
    }
    
    try {
      await register({...userIn, image: "null.jpg"});
    } catch (error) {
      return Alert.alert("Error", error.response.data);
    }
  };

  return (
    <View className={`flex-1 bg-[#${darkmode ? "2C2C2C" : "CCCCCC"}] justify-center items-center`}>
      <View
        className="justify-evenly bg-white rounded-2xl w-96"
        style={{ height: 600 }}
      >
        <View className="m-10">
          <TextInput
            placeholder={
              translations[language || "en-EN"].screens.Register.username
            }
            placeholderTextColor="#4B0082"
            className="border-[#4B0082] border-2 rounded-lg bg-[#F8F7FE] text-lg mb-5 pl-3 text-black"
            onChangeText={(text) => setuserIn({ ...userIn, username: text })}
          />
          <TextInput
            placeholder={translations[language || "en-EN"].screens.Login.email}
            placeholderTextColor="#4B0082"
            inputMode="email"
            className="border-[#4B0082] border-2 rounded-lg bg-[#F8F7FE] text-lg mb-5 pl-3 text-black"
            onChangeText={(text) => setuserIn({ ...userIn, email: text })}
          />
          <TextInput
            placeholder={
              translations[language || "en-EN"].screens.Login.password
            }
            placeholderTextColor="#4B0082"
            secureTextEntry={!passwordShown}
            className="border-[#4B0082] border-2 rounded-lg bg-[#F8F7FE] text-lg mb-5 pl-3 text-black"
            onChangeText={(text) => setuserIn({ ...userIn, password: text })}
          />
          <TextInput
            placeholder={
              translations[language || "en-EN"].screens.Register.confirmPassword
            }
            placeholderTextColor="#4B0082"
            secureTextEntry={!passwordShown}
            className="border-[#4B0082] border-2 rounded-lg bg-[#F8F7FE] text-lg mb-2 pl-3 text-black"
            onChangeText={(text) => setConfirmPassword(text)}
          />
          <TouchableOpacity onPress={() => setPasswordShown(!passwordShown)}>
            <Text
              className="text-[#4B0082] font-bold text-lg mb-5"
              style={{ fontFamily: "InriaSans-Regular" }}
            >
              {passwordShown ? "Hide" : "Show"}
            </Text>
          </TouchableOpacity>
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
              {translations[language || "en-EN"].screens.Login.register}
            </Text>
          </TouchableOpacity>
          <TouchableOpacity
            onPress={() => resetNavigation(navigation, "Login")}
            className="border-[#E4007C] border-2 rounded-lg py-1"
          >
            <Text className="text-[#4B0082] font-bold text-lg text-center">
              {translations[language || "en-EN"].screens.Login.login}
            </Text>
          </TouchableOpacity>
        </View>
      </View>
    </View>
  );
};

export default RegisterScreen;
