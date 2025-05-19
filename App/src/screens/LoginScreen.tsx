import { View, Text, TextInput, TouchableOpacity, Alert } from "react-native";
import React, { useEffect, useState } from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { LoginStackProps } from "../navigation/LoginStackNavigation";
import { login } from "../repositories/LoginRepository";
import { cardBgColor, resetNavigation } from "../utils/Utils";
import RNSecureKeyStore from "react-native-secure-key-store";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { translations } from "../../translations/translation";
import style from "../styles/Styles.json";
import { useTokenContext } from "../contexts/TokenContextProvider";
import FormInput from "../components/FormInput";

type Props = NativeStackScreenProps<LoginStackProps, "Login">;

const LoginScreen = ({ navigation }: Props) => {
  const [passwordShown, setPasswordShown] = useState<boolean>(false);
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const { setToken } = useTokenContext();
  const { language, setLanguage, darkmode, setDarkmode } = useSettingsContext();

  useEffect(() => {
    fetchMode();
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

  const fetchMode = async () => {
    try {
      const darkMode = await AsyncStorage.getItem("darkMode");
      setDarkmode(darkMode === "true");
    } catch (error) {
      console.log(error);
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
    <View
      className={`flex-1 ${cardBgColor(darkmode)} justify-center items-center`}
    >
      <View
        className={`justify-evenly ${
          darkmode ? "bg-[#E8E2F0]" : "bg-white"
        } rounded-2xl w-96`}
        style={{ height: 400 }}
      >
        <View className="m-10 mb-5">
          <FormInput
            label={translations[language || "en-EN"].screens.Login.email}
            name={email}
            setText={(text) => setEmail(text)}
            mode="email"
          />
          <TextInput
            placeholder={
              translations[language || "en-EN"].screens.Login.password
            }
            placeholderTextColor={`${darkmode ? "#E0D3F5" : "#4B0082"}`}
            secureTextEntry={!passwordShown}
            className={`text-lg mb-5 pl-3 rounded-lg border-2 ${
              darkmode
                ? "bg-[#4B294F] text-white border-[#B28DFF]"
                : "bg-[#F8F7FE] text-black border-[#4B0082]"
            }`}
            onChangeText={(text) => setPassword(text)}
          />
          <TouchableOpacity onPress={() => setPasswordShown(!passwordShown)}>
            <Text
              className="text-[#4B0082] font-bold text-lg mt-3"
              style={{ fontFamily: "Lexend-Regular" }}
            >
              {passwordShown ? "Hide" : "Show"}
            </Text>
          </TouchableOpacity>
        </View>
        <View className="m-10 mt-5">
          <TouchableOpacity
            onPress={log}
            className="bg-[#F65261] rounded-lg py-3 mb-5"
          >
            <Text
              className={style.utils.button}
              style={{ fontFamily: "Lexend-Regular" }}
            >
              {translations[language || "en-EN"].screens.Login.login}
            </Text>
          </TouchableOpacity>
          <TouchableOpacity
            onPress={() => resetNavigation(navigation, "Register")}
            className="border-[#F65261] border-2 rounded-lg py-1"
          >
            <Text className="text-[#7D3C98] font-bold text-2xl text-center">
              {translations[language || "en-EN"].screens.Login.register}
            </Text>
          </TouchableOpacity>
        </View>
      </View>
    </View>
  );
};

export default LoginScreen;
