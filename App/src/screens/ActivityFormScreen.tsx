import { View, TouchableOpacity, Text } from "react-native";
import { useToast } from "../contexts/FeedbackContextProvider";
import { haptics } from "../utils/haptics";
import React, { useEffect, useState } from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import Icon from "react-native-vector-icons/Ionicons";
import { ActivitiesStackProps } from "../navigation/ActivitiesStackNavigation";
import { useImageContext } from "../contexts/ImageContextProvider";
import PhotoPicker from "../components/PhotoPicker";
import PickedPhotoPreview from "../components/PickedPhotoPreview";
import { translations } from "../../translations/translation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { Activity } from "../utils/Activity";
import { createActivity } from "../repositories/ActivityRepository";
import FormInput from "../components/FormInput";
import { uploadImage } from "../repositories/ImageRepository";
import {
  borderColor,
  cardBgColor,
  iconColor,
  mutedTextColor,
  raisedBgColor,
  textColor,
} from "../utils/Utils";
import { fonts } from "../styles/theme";
import { errorMessage } from "../utils/graphql";
import Stepper from "../components/Stepper";

type Props = NativeStackScreenProps<ActivitiesStackProps, "ActivityForm">;

const TOTAL_STEPS = 3;
const RATES = ["daily", "weekly", "monthly", "yearly"] as const;

const ActivityFormScreen = (props: Props) => {
  const { language, darkmode } = useSettingsContext();
  const { uri, picked, clear, setWidth, setHeight } = useImageContext();
  const [step, setStep] = useState<number>(1);
  const [activity, setActivity] = useState<Activity>({} as Activity);
  const [timeRate, setTimeRate] = useState<(typeof RATES)[number]>("daily");
  const [times, setTimes] = useState<number>(1);
  const [sending, setSending] = useState<boolean>(false);
  const [errors, setErrors] = useState<{ name?: string; description?: string }>({});
  const [formError, setFormError] = useState<string | null>(null);
  const toast = useToast();
  const t = translations[language || "en-EN"].screens;

  useEffect(() => {
    setWidth(4);
    setHeight(5);
  }, []);

  const addActivity = async () => {
    setSending(true);
    setFormError(null);
    try {
      const imageName = await uploadImage(picked);
      const created = await createActivity(
        activity.name.trim(),
        activity.description.trim(),
        timeRate,
        String(times),
        imageName
      );
      haptics.success();
      toast.success(t.ActivityForm.success);
      clear();
      // The API already signed the creator up: show the new activity as one of theirs.
      props.navigation.replace("ActivityDetails", { activity: created, suscribed: true });
    } catch (error) {
      haptics.error();
      setFormError(errorMessage(error));
    } finally {
      setSending(false);
    }
  };

  const next = () => {
    if (step === 2) {
      const e = {
        name: activity.name?.trim() ? undefined : t.Common.required,
        description: activity.description?.trim() ? undefined : t.Common.required,
      };
      setErrors(e);
      if (e.name || e.description) return;
    }
    if (step < TOTAL_STEPS) setStep(step + 1);
    else addActivity();
  };

  const back = () => (step === 1 ? props.navigation.goBack() : setStep(step - 1));
  const titles = [t.ActivityForm.photoTitle, t.ActivityForm.detailsTitle, t.ActivityForm.rhythmTitle];

  return (
    <Stepper
      step={step}
      total={TOTAL_STEPS}
      title={titles[step - 1]}
      onBack={back}
      error={formError}
      onNext={next}
      nextDisabled={step === 1 && !uri}
      nextLabel={step === TOTAL_STEPS ? t.Activities.create : undefined}
      loading={sending}
    >
      {step === 1 && (
        <View
          style={{ height: 420 }}
          className={`w-full rounded-2xl overflow-hidden border ${uri ? "" : "border-dashed"} ${borderColor(
            darkmode
          )} ${cardBgColor(darkmode)}`}
        >
          {uri ? <PickedPhotoPreview /> : <PhotoPicker />}
        </View>
      )}

      {step === 2 && (
        <View>
          <FormInput
            label={t.ActivityForm.title}
            name={activity.name}
            setText={(text) => {
              setActivity({ ...activity, name: text });
              setErrors({ ...errors, name: undefined });
            }}
            mode="text"
            error={errors.name}
          />
          <FormInput
            label={t.ActivityForm.description}
            name={activity.description}
            setText={(text) => {
              setActivity({ ...activity, description: text });
              setErrors({ ...errors, description: undefined });
            }}
            mode="text"
            error={errors.description}
          />
        </View>
      )}

      {step === 3 && (
        <View>
          <View className="flex-row flex-wrap -mx-1">
            {RATES.map((rate) => {
              const active = rate === timeRate;
              return (
                <TouchableOpacity
                  key={rate}
                  activeOpacity={0.7}
                  onPress={() => setTimeRate(rate)}
                  style={{ width: "50%" }}
                  className="p-1"
                >
                  <View
                    className={`h-12 rounded-xl items-center justify-center border ${
                      active ? "border-[#C2410C] bg-[#C2410C14]" : `${borderColor(darkmode)} ${raisedBgColor(darkmode)}`
                    }`}
                  >
                    <Text className={`text-base ${active ? "font-semibold text-[#C2410C]" : textColor(darkmode)}`}>
                      {t.ActivityForm.timeRates[rate]}
                    </Text>
                  </View>
                </TouchableOpacity>
              );
            })}
          </View>

          <Text className={`text-xs mt-6 mb-2 ${mutedTextColor(darkmode)}`}>{t.ActivityForm.timesHint}</Text>
          <View
            className={`flex-row items-center justify-between h-14 px-2 rounded-xl border ${raisedBgColor(
              darkmode
            )} ${borderColor(darkmode)}`}
          >
            <TouchableOpacity
              hitSlop={8}
              disabled={times <= 1}
              onPress={() => setTimes(Math.max(1, times - 1))}
              className="w-10 h-10 items-center justify-center"
            >
              <Icon name="remove" size={20} color={iconColor(darkmode)} style={{ opacity: times <= 1 ? 0.3 : 1 }} />
            </TouchableOpacity>
            <Text style={{ fontFamily: fonts.display }} className={`text-2xl ${textColor(darkmode)}`}>
              {times}
            </Text>
            <TouchableOpacity
              hitSlop={8}
              disabled={times >= 30}
              onPress={() => setTimes(Math.min(30, times + 1))}
              className="w-10 h-10 items-center justify-center"
            >
              <Icon name="add" size={20} color={iconColor(darkmode)} />
            </TouchableOpacity>
          </View>
        </View>
      )}
    </Stepper>
  );
};

export default ActivityFormScreen;
