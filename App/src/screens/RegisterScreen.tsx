import { View, Text, TextInput, TouchableOpacity, Alert } from "react-native";
import React, { useRef, useState } from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { LoginStackProps } from "../navigation/LoginStackNavigation";
import PhoneInput from "react-native-phone-number-input";
import { UserIn } from "../utils/User";
import { cardBgColor, resetNavigation } from "../utils/Utils";
import { translations } from "../../translations/translation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { register } from "../repositories/RegisterRepository";
import FormInput from "../components/FormInput";

type Props = NativeStackScreenProps<LoginStackProps, "Register">;

const RegisterScreen = ({ navigation, route }: Props) => {
  const [passwordShown, setPasswordShown] = useState<boolean>(false);
  const [userIn, setUserIn] = useState<UserIn>({} as UserIn);
  const phoneInput = useRef<PhoneInput>(null);
  const [confirmPassword, setConfirmPassword] = useState<string>(null);
  const { language, darkmode } = useSettingsContext();

  const validateForm = async () => {
    if (
      !userIn.username ||
      !userIn.email ||
      !userIn.password ||
      !userIn.phoneNumber ||
      !userIn.nationality
    ) {
      return Alert.alert("Missing data");
    }

    if (confirmPassword !== userIn.password) {
      return Alert.alert("Passwords do not match");
    }

    try {
      await register({ ...userIn, image: "null.jpg" });
      Alert.alert(
        translations[language || "en-EN"].screens.Register.success,
        translations[language || "en-EN"].screens.Register.successMessage
      );
    } catch (error) {
      Alert.alert("Error", error.response.data);
    }
  };

  return (
    <View
      className={`flex-1 ${cardBgColor(darkmode)} justify-center items-center`}
    >
      <View
        className={`justify-evenly bg-white rounded-2xl w-96`}
        style={{ height: 700 }}
      >
        <View className="m-10">
          <FormInput
            label={translations[language || "en-EN"].screens.Register.username}
            name={userIn.username}
            setText={(text) => setUserIn({ ...userIn, username: text })}
            mode="text"
          />
          <FormInput
            label={translations[language || "en-EN"].screens.Login.email}
            name={userIn.email}
            setText={(text) => setUserIn({ ...userIn, email: text })}
            mode="email"
          />
          <FormInput
            label={
              translations[language || "en-EN"].screens.ProfileForm.nationality
            }
            name={userIn.nationality}
            setText={(text) => setUserIn({ ...userIn, nationality: text })}
            mode="text"
          />
          <View
            className={`mb-5 rounded-lg border-2 ${
              darkmode
                ? "bg-[#4B294F] border-[#B28DFF]"
                : "bg-[#F8F7FE] border-[#4B0082]"
            }`}
          >
            <PhoneInput
              ref={phoneInput}
              defaultCode="ES"
              layout="first"
              containerStyle={{
                width: "100%",
                height: 50,
                backgroundColor: darkmode ? "#4B294F" : "#F8F7FE",
                borderRadius: 8,
              }}
              textContainerStyle={{
                backgroundColor: darkmode ? "#4B294F" : "#F8F7FE",
                borderRadius: 8,
              }}
              textInputStyle={{
                color: darkmode ? "white" : "black",
                fontSize: 16,
                height: 50,
                paddingVertical: 0,
              }}
              codeTextStyle={{
                color: darkmode ? "white" : "black",
                fontSize: 16,
              }}
              onChangeFormattedText={(text) =>
                setUserIn({ ...userIn, phoneNumber: text })
              }
            />
          </View>

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
            onChangeText={(text) => setUserIn({ ...userIn, password: text })}
          />
          <TextInput
            placeholder={
              translations[language || "en-EN"].screens.Register.confirmPassword
            }
            placeholderTextColor={`${darkmode ? "#E0D3F5" : "#4B0082"}`}
            secureTextEntry={!passwordShown}
            className={`text-lg mb-5 pl-3 rounded-lg border-2 ${
              darkmode
                ? "bg-[#4B294F] text-white border-[#B28DFF]"
                : "bg-[#F8F7FE] text-black border-[#4B0082]"
            }`}
            onChangeText={(text) => setConfirmPassword(text)}
          />
          <TouchableOpacity onPress={() => setPasswordShown(!passwordShown)}>
            <Text
              className="text-[#4B0082] font-bold text-lg"
              style={{ fontFamily: "Lexend-Regular" }}
            >
              {passwordShown ? "Hide" : "Show"}
            </Text>
          </TouchableOpacity>
        </View>
        <View className="mx-8">
          <TouchableOpacity
            onPress={validateForm}
            className="bg-[#F65261] rounded-lg py-3 mb-5"
          >
            <Text className="text-white font-bold text-xl text-center">
              {translations[language || "en-EN"].screens.Login.register}
            </Text>
          </TouchableOpacity>
          <TouchableOpacity
            onPress={() => resetNavigation(navigation, "Login")}
            className="border-[#F65261] border-2 rounded-lg py-1"
          >
            <Text className="text-[#7D3C98] font-bold text-lg text-center">
              {translations[language || "en-EN"].screens.Login.login}
            </Text>
          </TouchableOpacity>
        </View>
      </View>
    </View>
  );
};

export default RegisterScreen;
