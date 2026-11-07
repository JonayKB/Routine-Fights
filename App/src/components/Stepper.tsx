import React from "react";
import {
  ActivityIndicator,
  KeyboardAvoidingView,
  Platform,
  ScrollView,
  Text,
  TouchableOpacity,
  View,
} from "react-native";
import Icon from "react-native-vector-icons/Ionicons";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { translations } from "../../translations/translation";
import {
  bgColor,
  borderColor,
  mutedTextColor,
  raisedBgColor,
  secondaryTextColor,
  textColor,
} from "../utils/Utils";
import { fonts } from "../styles/theme";
import { formatString } from "./Streak";

type Props = {
  step: number;
  total: number;
  title: string;
  subtitle?: string;
  children: React.ReactNode;
  onBack?: () => void;
  onNext: () => void;
  nextLabel?: string;
  nextDisabled?: boolean;
  loading?: boolean;
  /** Extra top padding, e.g. when there is no header above. */
  topPadding?: number;
  /** Error for the whole step, shown right above the buttons. */
  error?: string | null;
};

/** A multi-step form frame: progress, title, content and Back / Next. */
const Stepper = ({
  step,
  total,
  title,
  subtitle,
  children,
  onBack,
  onNext,
  nextLabel,
  nextDisabled,
  loading,
  topPadding = 32,
  error,
}: Props) => {
  const { darkmode, language } = useSettingsContext();
  const t = translations[language || "en-EN"].screens.Common;
  const disabled = nextDisabled || loading;

  return (
    <KeyboardAvoidingView
      className={`flex-1 ${bgColor(darkmode)}`}
      behavior={Platform.OS === "ios" ? "padding" : undefined}
    >
      <ScrollView
        keyboardShouldPersistTaps="handled"
        contentContainerStyle={{ flexGrow: 1, paddingHorizontal: 20, paddingTop: topPadding, paddingBottom: 16 }}
      >
        <View className="flex-row">
          {Array.from({ length: total }).map((_, i) => (
            <View
              key={i}
              className={`flex-1 h-1 rounded-full ${i < total - 1 ? "mr-1.5" : ""} ${
                i < step ? "bg-[#C2410C]" : raisedBgColor(darkmode)
              }`}
            />
          ))}
        </View>
        <Text className={`text-xs mt-3 ${mutedTextColor(darkmode)}`}>
          {formatString(t.step, { n: step, total })}
        </Text>
        <Text style={{ fontFamily: fonts.display }} className={`text-3xl mt-2 ${textColor(darkmode)}`}>
          {title}
        </Text>
        {!!subtitle && (
          <Text className={`text-sm mt-1 ${secondaryTextColor(darkmode)}`}>{subtitle}</Text>
        )}
        <View className="mt-6 flex-1">{children}</View>
      </ScrollView>

      {!!error && (
        <View className="flex-row items-center mx-5 mb-2 px-3 py-2.5 rounded-xl bg-[#E5484D1A]">
          <Icon name="alert-circle" size={16} color="#E5484D" />
          <Text className="flex-1 ml-2 text-sm text-[#E5484D]">{error}</Text>
        </View>
      )}
      <View className={`flex-row px-5 py-3 border-t ${borderColor(darkmode)}`}>
        {onBack && (
          <TouchableOpacity
            activeOpacity={0.7}
            onPress={onBack}
            className={`h-12 px-5 mr-3 rounded-xl items-center justify-center border ${raisedBgColor(
              darkmode
            )} ${borderColor(darkmode)}`}
          >
            <Text className={`text-base font-medium ${textColor(darkmode)}`}>{t.back}</Text>
          </TouchableOpacity>
        )}
        <TouchableOpacity
          activeOpacity={0.85}
          disabled={disabled}
          onPress={onNext}
          className={`flex-1 h-12 rounded-xl items-center justify-center ${
            disabled ? raisedBgColor(darkmode) : "bg-[#C2410C]"
          }`}
        >
          {loading ? (
            <ActivityIndicator color="#FFFFFF" />
          ) : (
            <Text
              className={`text-base font-semibold ${disabled ? mutedTextColor(darkmode) : "text-white"}`}
            >
              {nextLabel ?? t.next}
            </Text>
          )}
        </TouchableOpacity>
      </View>
    </KeyboardAvoidingView>
  );
};

export default Stepper;
