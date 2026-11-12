import {
  ActivityIndicator,
  KeyboardAvoidingView,
  Platform,
  ScrollView,
  Text,
  TouchableOpacity,
  View,
} from "react-native";
import React, { useEffect, useRef, useState } from "react";
import Icon from "react-native-vector-icons/Ionicons";
import PhoneInput from "react-native-phone-number-input";
import { CountryCode } from "react-native-country-picker-modal/lib/types";
import FormInput from "../components/FormInput";
import Avatar from "../components/Avatar";
import ProfileNavigation from "../components/ProfileNavigation";
import Skeleton from "../components/Skeleton";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { translations } from "../../translations/translation";
import { UserIn, UserOut } from "../utils/User";
import { getOwnUser, updateUser } from "../repositories/UserRepository";
import { useAccounts } from "../contexts/AccountsContextProvider";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ProfileStackProps } from "../navigation/ProfileStackNavigation";
import {
  bgColor,
  borderColor,
  mutedTextColor,
  secondaryTextColor,
} from "../utils/Utils";
import { colors, palette } from "../styles/theme";
import { errorMessage } from "../utils/graphql";
import { useToast } from "../contexts/FeedbackContextProvider";
import { deviceRegion } from "../utils/deviceSettings";
import { splitPhoneNumber } from "../utils/phone";

type Props = NativeStackScreenProps<ProfileStackProps, "ProfileForm">;

const MIN_PASSWORD = 8;
const EMAIL = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/;

/** Titled group of fields. Lives outside the screen so inputs keep focus while typing. */
const Section = ({ title, color, children }: { title: string; color: string; children: React.ReactNode }) => (
  <View className="mt-7">
    <Text className="text-base font-semibold mb-3" style={{ color }}>
      {title}
    </Text>
    {children}
  </View>
);

type Field = "username" | "email" | "nationality" | "phone" | "password" | "confirm";

const ProfileFormScreen = ({ navigation }: Props) => {
  const toast = useToast();
  const { signOut } = useAccounts();
  const { language, darkmode } = useSettingsContext();
  const t = translations[language || "en-EN"].screens;
  const c = colors(darkmode);
  const phoneInput = useRef<PhoneInput>(null);

  const [original, setOriginal] = useState<UserOut | null>(null);
  const [user, setUser] = useState<UserIn>({} as UserIn);
  const [confirmPassword, setConfirmPassword] = useState("");
  const [errors, setErrors] = useState<Partial<Record<Field, string>>>({});
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    fetchOwnUser();
  }, []);

  const fetchOwnUser = async () => {
    try {
      const response: UserOut = await getOwnUser();
      setOriginal(response);
      setUser({
        username: response.username,
        email: response.email,
        nationality: response.nationality,
        phoneNumber: response.phoneNumber,
        password: "",
        image: response.image,
      });
    } catch (error) {
      toast.error(errorMessage(error));
    }
  };

  const set = (field: keyof UserIn, error: Field) => (value: string) => {
    setUser((u) => ({ ...u, [field]: value }));
    setErrors((e) => ({ ...e, [error]: undefined }));
  };

  const emailChanged = !!original && user.email?.trim() !== original.email;
  const dirty =
    !!original &&
    (user.username?.trim() !== original.username ||
      emailChanged ||
      user.nationality?.trim() !== (original.nationality ?? "") ||
      user.phoneNumber !== original.phoneNumber ||
      !!user.password);

  const validate = (): Partial<Record<Field, string>> => {
    const required = t.Common.required;
    const e: Partial<Record<Field, string>> = {};
    if (!user.username?.trim()) e.username = required;
    if (!user.email?.trim()) e.email = required;
    else if (!EMAIL.test(user.email.trim())) e.email = t.Register.invalidEmail;
    if (!user.nationality?.trim()) e.nationality = required;
    if (!user.phoneNumber) e.phone = required;
    else if (phoneInput.current && !phoneInput.current.isValidNumber(user.phoneNumber)) {
      e.phone = t.Register.invalidPhone;
    }
    if (user.password) {
      if (user.password.length < MIN_PASSWORD) e.password = t.Register.passwordShort;
      if (confirmPassword !== user.password) e.confirm = t.Register.mismatch;
    }
    return e;
  };

  const save = async () => {
    const e = validate();
    setErrors(e);
    if (Object.keys(e).length > 0) return;

    const email = user.email.trim();
    if (emailChanged) {
      const ok = await toast.confirm({
        title: t.ProfileForm.emailChangeTitle,
        message: t.ProfileForm.emailChangeBody.replace("{email}", email),
        confirmLabel: t.ProfileForm.emailChangeConfirm,
        cancelLabel: t.Common.cancel,
      });
      if (!ok) return;
    }

    setSaving(true);
    try {
      await updateUser(
        { ...user, email, username: user.username.trim(), nationality: user.nationality.trim() },
        language
      );
      if (emailChanged) {
        // The session belongs to the old address and the new one must be verified first.
        toast.info(t.ProfileForm.emailChanged);
        await signOut();
        return;
      }
      toast.success(t.ProfileForm.updated);
      navigation.navigate("Profile", {});
    } catch (error: any) {
      const raw: string = error?.message ?? "";
      if (raw.startsWith("Email already exists")) setErrors({ email: errorMessage(error) });
      else if (raw.startsWith("Invalid email")) setErrors({ email: errorMessage(error) });
      else if (raw.startsWith("Invalid phone number")) setErrors({ phone: errorMessage(error) });
      else toast.error(errorMessage(error));
    } finally {
      setSaving(false);
    }
  };

  const phone = splitPhoneNumber(original?.phoneNumber);

  return (
    <View className={`flex-1 ${bgColor(darkmode)}`}>
      <ProfileNavigation navigation={navigation} message={t.Settings.editProfile} />

      {!original ? (
        <View className="px-5 pt-6">
          <View className="items-center mb-8">
            <Skeleton width={88} height={88} radius={44} />
          </View>
          {[0, 1, 2, 3].map((i) => (
            <View key={i} className="mb-4">
              <Skeleton width={120} height={14} />
              <View className="h-2" />
              <Skeleton width="100%" height={48} radius={12} />
            </View>
          ))}
        </View>
      ) : (
        <KeyboardAvoidingView
          className="flex-1"
          behavior={Platform.OS === "ios" ? "padding" : undefined}
        >
          <ScrollView
            contentContainerStyle={{ paddingHorizontal: 20, paddingTop: 24, paddingBottom: 32 }}
            keyboardShouldPersistTaps="handled"
          >
            <View className="items-center">
              <Avatar image={original.image} name={original.username} size={88} />
              <TouchableOpacity
                className="flex-row items-center mt-3 px-3 py-1.5"
                hitSlop={8}
                onPress={() =>
                  navigation.navigate("ProfilePictureScreen", { email: original.email })
                }
              >
                <Icon
                  name="camera-outline"
                  size={16}
                  color={darkmode ? palette.accentOnDark : palette.accent}
                />
                <Text
                  className="ml-1.5 text-sm font-semibold"
                  style={{ color: darkmode ? palette.accentOnDark : palette.accent }}
                >
                  {t.ProfileForm.changePhoto}
                </Text>
              </TouchableOpacity>
            </View>

            <Section title={t.ProfileForm.account} color={c.text}>
              <FormInput
                title={t.ProfileForm.username}
                label={t.ProfileForm.username}
                name={user.username}
                setText={set("username", "username")}
                error={errors.username}
                mode="text"
              />
              <FormInput
                title={t.ProfileForm.email}
                label={t.ProfileForm.email}
                name={user.email}
                setText={set("email", "email")}
                error={errors.email}
                hint={emailChanged ? t.ProfileForm.emailHint : undefined}
                mode="email"
              />
            </Section>

            <Section title={t.ProfileForm.aboutYou} color={c.text}>
              <FormInput
                title={t.ProfileForm.nationality}
                label={t.ProfileForm.nationality}
                name={user.nationality}
                setText={set("nationality", "nationality")}
                error={errors.nationality}
                mode="text"
              />
              <Text className={`mb-1.5 text-sm font-medium ${secondaryTextColor(darkmode)}`}>
                {t.ProfileForm.phoneNumber}
              </Text>
              <View
                className={`rounded-xl border overflow-hidden ${
                  errors.phone ? "border-[#E5484D]" : borderColor(darkmode)
                }`}
              >
                <PhoneInput
                  ref={phoneInput}
                  defaultCode={(phone.country ?? deviceRegion() ?? "ES") as CountryCode}
                  defaultValue={phone.national}
                  layout="first"
                  placeholder={t.ProfileForm.phoneNumber}
                  containerStyle={{ width: "100%", height: 48, backgroundColor: c.raised }}
                  textContainerStyle={{ backgroundColor: c.raised, paddingVertical: 0 }}
                  textInputProps={{ placeholderTextColor: c.textMuted }}
                  textInputStyle={{ color: c.text, fontSize: 16, height: 48, paddingVertical: 0 }}
                  codeTextStyle={{ color: c.text, fontSize: 16 }}
                  onChangeFormattedText={set("phoneNumber", "phone")}
                />
              </View>
              {!!errors.phone && (
                <View className="flex-row items-center mt-1.5 ml-1">
                  <Icon name="alert-circle" size={14} color="#E5484D" />
                  <Text className="ml-1.5 text-xs text-[#E5484D]">{errors.phone}</Text>
                </View>
              )}
            </Section>

            <Section title={t.ProfileForm.passwordSection} color={c.text}>
              <FormInput
                title={t.ProfileForm.newPassword}
                label={t.ProfileForm.newPassword}
                name={user.password}
                setText={set("password", "password")}
                error={errors.password}
                hint={t.ProfileForm.passwordHint}
                mode="text"
                secure
              />
              {!!user.password && (
                <FormInput
                  title={t.ProfileForm.confirmPassword}
                  label={t.ProfileForm.confirmPassword}
                  name={confirmPassword}
                  setText={(text) => {
                    setConfirmPassword(text);
                    setErrors((e) => ({ ...e, confirm: undefined }));
                  }}
                  error={errors.confirm}
                  mode="text"
                  secure
                />
              )}
            </Section>
          </ScrollView>

          <View className={`px-5 pt-3 pb-4 border-t ${borderColor(darkmode)}`}>
            <TouchableOpacity
              accessibilityRole="button"
              accessibilityState={{ disabled: !dirty || saving }}
              disabled={!dirty || saving}
              activeOpacity={0.85}
              onPress={save}
              className="h-12 rounded-xl items-center justify-center"
              style={{ backgroundColor: dirty ? palette.accent : c.raised }}
            >
              {saving ? (
                <ActivityIndicator color="#FFFFFF" />
              ) : (
                <Text
                  className={`text-base font-semibold ${dirty ? "" : mutedTextColor(darkmode)}`}
                  style={dirty ? { color: "#FFFFFF" } : undefined}
                >
                  {t.ProfileForm.save}
                </Text>
              )}
            </TouchableOpacity>
          </View>
        </KeyboardAvoidingView>
      )}
    </View>
  );
};

export default ProfileFormScreen;
