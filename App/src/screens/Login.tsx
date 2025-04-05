import { View, Text, TextInput, TouchableOpacity, Alert } from "react-native";
import React, { useEffect, useState } from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { LoginStackProps } from "../navigation/LoginStackNavigation";
import { login } from "../services/LoginService";
import { resetNavigation } from "../utils/Utils";
import { useTokenContext } from "../contexts/TokenContextProvider";
import RNSecureKeyStore from "react-native-secure-key-store";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { useLanguageContext } from "../contexts/LanguageContextProvider";
import { translations } from "../../translations/translation";

type Props = NativeStackScreenProps<LoginStackProps, "Login">;

const Login = ({ navigation }: Props) => {
  const [passwordShown, setPasswordShown] = useState<boolean>(false);
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const { setToken } = useTokenContext();
  const { language, setLanguage } = useLanguageContext();

  useEffect(() => {
    fetchLanguage();
    fetchToken();
  }, []);

  const fetchToken = async () => {
    const token = await RNSecureKeyStore.get("token");
    if (token) {
      setToken(token);
      resetNavigation(navigation, "MainTabNavigation");
    }
  };

  const fetchLanguage = async () => {
    try {
      const language = await AsyncStorage.getItem("language");
      setLanguage(language);
    } catch (error) {
      console.log(error);
    }
  };

  const log = async () => {
    try {
      const token = await login(email, password);
      setToken(token);
      resetNavigation(navigation, "MainTabNavigation");
    } catch (error) {
      Alert.alert("Error", error.response.data);
    }
  };

  return (
    <View className="flex-1 bg-[#E4DCE9] justify-center items-center">
      <View
        className="justify-evenly bg-white rounded-2xl w-96"
        style={{ height: 400 }}
      >
        <View className="m-10 mb-5">
          <TextInput
            placeholder={translations[language || 'en-EN'].screens.Login.email}
            placeholderTextColor="#4B0082"
            inputMode="email"
            className="border-[#4B0082] border-2 rounded-lg bg-[#F8F7FE] text-2xl mb-5 pl-3 text-black"
            onChangeText={(text) => setEmail(text)}
          />
          <TextInput
            placeholder={translations[language || 'en-EN'].screens.Login.password}
            placeholderTextColor="#4B0082"
            secureTextEntry={!passwordShown}
            className="border-[#4B0082] border-2 rounded-lg bg-[#F8F7FE] text-2xl pl-3 text-black"
            onChangeText={(text) => setPassword(text)}
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
            onPress={log}
            className="bg-[#E4007C] rounded-lg py-3 mb-5"
          >
            <Text
              className="text-white font-bold text-3xl text-center"
              style={{ fontFamily: "InriaSans-Regular" }}
            >
              {translations[language || 'en-EN'].screens.Login.login}
            </Text>
          </TouchableOpacity>
          <TouchableOpacity
            onPress={() => resetNavigation(navigation, "Register")}
            className="border-[#E4007C] border-2 rounded-lg py-1"
          >
            <Text className="text-[#4B0082] font-bold text-2xl text-center">
              {translations[language || 'en-EN'].screens.Login.register}
            </Text>
          </TouchableOpacity>
        </View>
      </View>
    </View>
  );
};

export default Login;
