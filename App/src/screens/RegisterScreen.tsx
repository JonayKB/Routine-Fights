import { View, Text, TouchableOpacity } from "react-native";
import Icon from "react-native-vector-icons/Ionicons";
import React, { useRef, useState } from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { LoginStackProps } from "../navigation/LoginStackNavigation";
import PhoneInput from "react-native-phone-number-input";
import { UserIn } from "../utils/User";
import {
  bgColor,
  borderColor,
  cardBgColor,
  mutedTextColor,
  resetNavigation,
  textColor,
} from "../utils/Utils";
import { colors, fonts } from "../styles/theme";
import { translations } from "../../translations/translation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { register } from "../repositories/RegisterRepository";
import FormInput from "../components/FormInput";
import Stepper from "../components/Stepper";
import { errorMessage } from "../utils/graphql";

type Props = NativeStackScreenProps<LoginStackProps, "Register">;

const TOTAL_STEPS = 3;
const MIN_PASSWORD = 8;
const EMAIL = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/;

const RegisterScreen = ({ navigation }: Props) => {
  const [step, setStep] = useState<number>(1);
  const [userIn, setUserIn] = useState<UserIn>({} as UserIn);
  const [confirmPassword, setConfirmPassword] = useState<string>("");
  const [sending, setSending] = useState<boolean>(false);
  const phoneInput = useRef<PhoneInput>(null);
  const { language, darkmode } = useSettingsContext();
  const t = translations[language || "en-EN"].screens;
  const c = colors(darkmode);

  type Field = "email" | "password" | "confirm" | "username" | "nationality" | "phone";
  const [errors, setErrors] = useState<Partial<Record<Field, string>>>({});
  const [formError, setFormError] = useState<string | null>(null);
  const [done, setDone] = useState<boolean>(false);

  const clear = (field: Field) => {
    setErrors((e) => ({ ...e, [field]: undefined }));
    setFormError(null);
  };

  // Field-level errors for the current step; empty when the step is valid.
  const validate = (): Partial<Record<Field, string>> => {
    const required = t.Common.required;
    const e: Partial<Record<Field, string>> = {};
    if (step === 1) {
      if (!userIn.email?.trim()) e.email = required;
      else if (!EMAIL.test(userIn.email.trim())) e.email = t.Register.invalidEmail;
      if (!userIn.password) e.password = required;
      else if (userIn.password.length < MIN_PASSWORD) e.password = t.Register.passwordShort;
      if (!confirmPassword) e.confirm = required;
      else if (userIn.password && confirmPassword !== userIn.password) e.confirm = t.Register.mismatch;
    }
    if (step === 2) {
      if (!userIn.username?.trim()) e.username = required;
      if (!userIn.nationality?.trim()) e.nationality = required;
      if (!userIn.phoneNumber) e.phone = required;
      else if (phoneInput.current && !phoneInput.current.isValidNumber(userIn.phoneNumber)) {
        e.phone = t.Register.invalidPhone;
      }
    }
    return e;
  };

  const next = async () => {
    const e = validate();
    setErrors(e);
    if (Object.keys(e).length > 0) return;
    if (step < TOTAL_STEPS) {
      setStep(step + 1);
      return;
    }
    setSending(true);
    setFormError(null);
    try {
      await register({
        ...userIn,
        email: userIn.email.trim(),
        username: userIn.username.trim(),
        image: "null.jpg",
      });
      setDone(true);
    } catch (error) {
      setFormError(errorMessage(error));
    } finally {
      setSending(false);
    }
  };

  const back = () => (step === 1 ? resetNavigation(navigation, "Login") : setStep(step - 1));

  const titles = [t.Register.accountTitle, t.Register.profileTitle, t.Register.reviewTitle];

  const Row = ({ label, value, last }: { label: string; value: string; last?: boolean }) => (
    <View className={`flex-row items-center px-4 py-3.5 ${last ? "" : `border-b ${borderColor(darkmode)}`}`}>
      <Text className={`w-40 mr-3 text-sm ${mutedTextColor(darkmode)}`}>{label}</Text>
      <Text numberOfLines={1} className={`flex-1 text-sm ${textColor(darkmode)}`}>
        {value}
      </Text>
    </View>
  );

  if (done) {
    return (
      <View className={`flex-1 justify-center px-6 ${bgColor(darkmode)}`}>
        <View className="w-14 h-14 rounded-2xl items-center justify-center bg-[#16A34A1F]">
          <Icon name="mail-unread-outline" size={26} color="#16A34A" />
        </View>
        <Text style={{ fontFamily: fonts.display }} className={`text-3xl mt-6 ${textColor(darkmode)}`}>
          {t.Register.success}
        </Text>
        <Text className={`text-base mt-2 ${mutedTextColor(darkmode)}`}>{t.Register.successMessage}</Text>
        <Text className={`text-base mt-1 font-medium ${textColor(darkmode)}`}>{userIn.email}</Text>
        <TouchableOpacity
          activeOpacity={0.85}
          onPress={() => resetNavigation(navigation, "Login")}
          className="h-12 mt-8 rounded-xl bg-[#C2410C] items-center justify-center"
        >
          <Text className="text-base font-semibold text-white">{t.Login.login}</Text>
        </TouchableOpacity>
      </View>
    );
  }

  return (
    <Stepper
      error={formError}
      step={step}
      total={TOTAL_STEPS}
      title={titles[step - 1]}
      onBack={back}
      onNext={next}
      nextLabel={step === TOTAL_STEPS ? t.Login.register : undefined}
      loading={sending}
      topPadding={48}
    >
      {step === 1 && (
        <View>
          <FormInput
            label={t.Login.email}
            name={userIn.email}
            setText={(text) => {
              setUserIn({ ...userIn, email: text });
              clear("email");
            }}
            error={errors.email}
            mode="email"
          />
          <FormInput
            label={t.Login.password}
            name={userIn.password}
            setText={(text) => {
              setUserIn({ ...userIn, password: text });
              clear("password");
            }}
            error={errors.password}
            mode="text"
            secure
          />
          <FormInput
            label={t.Register.confirmPassword}
            name={confirmPassword}
            setText={(text) => {
              setConfirmPassword(text);
              clear("confirm");
            }}
            error={errors.confirm}
            mode="text"
            secure
          />
          <Text
            className={`text-xs mt-1 ${
              userIn.password && userIn.password.length >= MIN_PASSWORD
                ? "text-[#16A34A]"
                : mutedTextColor(darkmode)
            }`}
          >
            {t.Register.passwordHint}
          </Text>
        </View>
      )}

      {step === 2 && (
        <View>
          <FormInput
            label={t.Register.username}
            name={userIn.username}
            setText={(text) => {
              setUserIn({ ...userIn, username: text });
              clear("username");
            }}
            error={errors.username}
            mode="text"
          />
          <FormInput
            label={t.ProfileForm.nationality}
            name={userIn.nationality}
            setText={(text) => {
              setUserIn({ ...userIn, nationality: text });
              clear("nationality");
            }}
            error={errors.nationality}
            mode="text"
          />
          <View
            className={`rounded-xl border overflow-hidden ${
              errors.phone ? "border-[#E5484D]" : borderColor(darkmode)
            }`}
          >
            <PhoneInput
              ref={phoneInput}
              defaultCode="ES"
              layout="first"
              defaultValue={userIn.phoneNumber?.replace(/^\+\d{1,3}/, "")}
              placeholder={t.ProfileForm.phoneNumber}
              containerStyle={{ width: "100%", height: 48, backgroundColor: c.raised }}
              textContainerStyle={{ backgroundColor: c.raised, paddingVertical: 0 }}
              textInputProps={{ placeholderTextColor: c.textMuted }}
              textInputStyle={{ color: c.text, fontSize: 16, height: 48, paddingVertical: 0 }}
              codeTextStyle={{ color: c.text, fontSize: 16 }}
              onChangeFormattedText={(text) => {
                setUserIn({ ...userIn, phoneNumber: text });
                clear("phone");
              }}
            />
          </View>
          {!!errors.phone && (
            <View className="flex-row items-center mt-1.5 ml-1">
              <Icon name="alert-circle" size={14} color="#E5484D" />
              <Text className="ml-1.5 text-xs text-[#E5484D]">{errors.phone}</Text>
            </View>
          )}
        </View>
      )}

      {step === 3 && (
        <View>
          <View className={`rounded-2xl border overflow-hidden ${cardBgColor(darkmode)} ${borderColor(darkmode)}`}>
            <Row label={t.Login.email} value={userIn.email} />
            <Row label={t.Register.username} value={userIn.username} />
            <Row label={t.ProfileForm.nationality} value={userIn.nationality} />
            <Row label={t.ProfileForm.phoneNumber} value={userIn.phoneNumber} last />
          </View>
        </View>
      )}
    </Stepper>
  );
};

export default RegisterScreen;
