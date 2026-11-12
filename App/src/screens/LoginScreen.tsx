import { View, Text, TouchableOpacity } from "react-native";
import Icon from "react-native-vector-icons/Ionicons";
import { useToast } from "../contexts/FeedbackContextProvider";
import React, { useState } from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { LoginStackProps } from "../navigation/LoginStackNavigation";
import { login } from "../repositories/LoginRepository";
import {
  bgColor,
  resetNavigation,
  secondaryTextColor,
  textColor,
} from "../utils/Utils";
import StreakFlame from "../components/StreakFlame";
import { fonts } from "../styles/theme";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { translations } from "../../translations/translation";
import { useTokenContext } from "../contexts/TokenContextProvider";
import FormInput from "../components/FormInput";
import { errorMessage } from "../utils/graphql";
import { sessionStarted } from "../utils/session";
import { useAccounts } from "../contexts/AccountsContextProvider";
import { getOwnUserImageAndEmail } from "../repositories/UserRepository";

type Props = NativeStackScreenProps<LoginStackProps, "Login">;

const LoginScreen = ({ navigation, route }: Props) => {
  const adding = !!route.params?.adding;
  const { remember } = useAccounts();
  const toast = useToast();
  const [errors, setErrors] = useState<{ email?: string | null; password?: string | null }>({});
  const [formError, setFormError] = useState<string | null>(null);
  const [sending, setSending] = useState<boolean>(false);
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const { setToken, setEmail: setSessionEmail } = useTokenContext();
  const { language, darkmode } = useSettingsContext();

  const log = async () => {
    const required = translations[language || "en-EN"].screens.Common.required;
    const fieldErrors = {
      email: email.trim() ? null : required,
      password: password ? null : required,
    };
    setErrors(fieldErrors);
    setFormError(null);
    if (fieldErrors.email || fieldErrors.password) return;
    setSending(true);
    try {
      const token = await login(email.trim(), password);
      sessionStarted();
      setToken(token);
      const me = await getOwnUserImageAndEmail().catch(() => null);
      await remember(me ?? { email: email.trim() });
      setSessionEmail(me?.email ?? email.trim());
      resetNavigation(navigation, "MainTabNavigation");
    } catch (error) {
      setFormError(errorMessage(error));
    } finally {
      setSending(false);
    }
  };

  return (
    <View className={`flex-1 justify-center px-6 ${bgColor(darkmode)}`}>
      {adding && navigation.canGoBack() && (
        <TouchableOpacity
          onPress={() => navigation.goBack()}
          hitSlop={10}
          accessibilityRole="button"
          accessibilityLabel={translations[language || "en-EN"].screens.Common.back}
          className="absolute top-4 left-4 w-10 h-10 items-center justify-center"
        >
          <Icon name="arrow-back" size={22} color={darkmode ? "#EFE9DE" : "#1F1B16"} />
        </TouchableOpacity>
      )}
      <View className="items-center mb-10">
        <StreakFlame streak={3} size={56} animated />
        <Text
          style={{ fontFamily: fonts.display }}
          className={`text-3xl mt-4 ${textColor(darkmode)}`}
        >
          Routine Fights
        </Text>
      </View>

      <FormInput
        label={translations[language || "en-EN"].screens.Login.email}
        name={email}
        setText={(text) => {
          setEmail(text);
          setErrors({ ...errors, email: null });
          setFormError(null);
        }}
        mode="email"
        error={errors.email}
      />
      <FormInput
        label={translations[language || "en-EN"].screens.Login.password}
        name={password}
        setText={(text) => {
          setPassword(text);
          setErrors({ ...errors, password: null });
          setFormError(null);
        }}
        mode="text"
        secure
        error={errors.password}
      />

      {!!formError && (
        <View className="flex-row items-center mt-1 mb-1 px-3 py-2.5 rounded-xl bg-[#E5484D1A]">
          <Icon name="alert-circle" size={16} color="#E5484D" />
          <Text className="flex-1 ml-2 text-sm text-[#E5484D]">{formError}</Text>
        </View>
      )}
      <TouchableOpacity
        disabled={sending}
        onPress={log}
        activeOpacity={0.85}
        className="h-12 mt-3 rounded-xl bg-[#C2410C] items-center justify-center"
      >
        <Text className="text-base font-semibold text-white">
          {translations[language || "en-EN"].screens.Login.login}
        </Text>
      </TouchableOpacity>
      <TouchableOpacity
        onPress={() => (adding ? navigation.navigate("Register") : resetNavigation(navigation, "Register"))}
        activeOpacity={0.6}
        className="h-12 mt-2 items-center justify-center"
      >
        <Text className={`text-sm font-medium ${secondaryTextColor(darkmode)}`}>
          {translations[language || "en-EN"].screens.Login.register}
        </Text>
      </TouchableOpacity>
    </View>
  );
};

export default LoginScreen;
