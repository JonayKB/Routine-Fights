import { View, TouchableOpacity, Text } from "react-native";
import { useToast } from "../contexts/FeedbackContextProvider";
import { haptics } from "../utils/haptics";
import React, { useCallback, useEffect, useState } from "react";
import { useFocusEffect, useNavigation } from "@react-navigation/native";
import Icon from "react-native-vector-icons/Ionicons";
import { useImageContext } from "../contexts/ImageContextProvider";
import { translations } from "../../translations/translation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { getSubscribedActivities } from "../repositories/ActivityRepository";
import { ActivityWithStreak } from "../utils/Activity";
import { uploadPost } from "../repositories/PostRepository";
import { uploadImage } from "../repositories/ImageRepository";
import PhotoPicker from "../components/PhotoPicker";
import PickedPhotoPreview from "../components/PickedPhotoPreview";
import CroppedImage from "../components/CroppedImage";
import {
  borderColor,
  cardBgColor,
  mutedTextColor,
  textColor,
} from "../utils/Utils";
import { fonts } from "../styles/theme";
import { errorMessage } from "../utils/graphql";
import Stepper from "../components/Stepper";
import EmptyState from "../components/EmptyState";
import LevelUpModal from "../components/LevelUpModal";
import StreakFlame, { TIERS, inkFor, tierFor } from "../components/StreakFlame";
import { formatString, remainingOf, streakUnit } from "../components/Streak";

type Props = {};

const TOTAL_STEPS = 3;

/** Streak the server will assign to the next post (see PostService.calculateStreak). */
const predictedStreak = (a: ActivityWithStreak) => {
  const current = a.streak ?? 0;
  const postedThisPeriod = remainingOf(a) < Number(a.timesRequiered ?? 0);
  if (postedThisPeriod) return Math.max(1, current);
  return current + 1;
};

const UploadFormScreen = (props: Props) => {
  const { uri, picked, clear, setWidth, setHeight } = useImageContext();
  const [step, setStep] = useState<number>(1);
  const [activities, setActivities] = useState<ActivityWithStreak[]>([]);
  const [selected, setSelected] = useState<ActivityWithStreak | null>(null);
  const [sending, setSending] = useState<boolean>(false);
  const [formError, setFormError] = useState<string | null>(null);
  const toast = useToast();
  const [levelUp, setLevelUp] = useState<{ streak: number; timeRate: string } | null>(null);
  const { language, darkmode } = useSettingsContext();
  const navigation = useNavigation<any>();
  const t = translations[language || "en-EN"].screens;

  useEffect(() => {
    setWidth(4);
    setHeight(5);
  }, []);

  useFocusEffect(
    useCallback(() => {
      fetchActivities();
    }, [])
  );

  const fetchActivities = async () => {
    try {
      setActivities(await getSubscribedActivities());
    } catch (error) {
      toast.error(errorMessage(error));
    }
  };

  const reset = () => {
    clear();
    setSelected(null);
    setStep(1);
  };

  const createPost = async () => {
    if (!selected || !picked) return;
    setSending(true);
    setFormError(null);
    try {
      const before = selected.streak ?? 0;
      const imageName = await uploadImage(picked);
      const post = await uploadPost(selected.id, imageName);
      const after: number = post?.streak ?? predictedStreak(selected);
      const climbed = TIERS.indexOf(tierFor(after)) > TIERS.indexOf(tierFor(before));
      reset();
      fetchActivities();
      if (climbed) {
        setLevelUp({ streak: after, timeRate: selected.timeRate });
      } else {
        // The level-up modal has its own vibration.
        haptics.success();
        toast.success(t.UploadForm.posted);
      }
    } catch (error) {
      haptics.error();
      setFormError(errorMessage(error));
    } finally {
      setSending(false);
    }
  };

  const next = () => {
    if (step < TOTAL_STEPS) setStep(step + 1);
    else createPost();
  };

  const titles = [t.UploadForm.photoTitle, t.UploadForm.activityTitle, t.UploadForm.reviewTitle];
  const canContinue = (step === 1 && !!uri) || (step === 2 && !!selected) || step === 3;

  return (
    <>
      <Stepper
        step={step}
        total={TOTAL_STEPS}
        title={titles[step - 1]}
        error={formError}
        onBack={
          step > 1
            ? () => {
                setFormError(null);
                setStep(step - 1);
              }
            : undefined
        }
        onNext={next}
        nextDisabled={!canContinue}
        nextLabel={step === TOTAL_STEPS ? t.UploadForm.postButton : undefined}
        loading={sending}
      >
        {step === 1 && (
          <View
            style={{ height: 420 }}
            className={`w-full rounded-2xl overflow-hidden border ${
              uri ? "" : "border-dashed"
            } ${borderColor(darkmode)} ${cardBgColor(darkmode)}`}
          >
            {uri ? <PickedPhotoPreview /> : <PhotoPicker />}
          </View>
        )}

        {step === 2 &&
          (activities.length === 0 ? (
            <EmptyState
              visual={<StreakFlame streak={0} size={44} />}
              title={t.UploadForm.noActivities}
              actionLabel={t.Home.emptyAction}
              onAction={() =>
                navigation.navigate("ActivitiesStackNavigation", { screen: "Activities" })
              }
            />
          ) : (
            activities.map((a) => {
              const remaining = remainingOf(a);
              const done = remaining < 1;
              const isSelected = selected?.id === a.id;
              return (
                <TouchableOpacity
                  key={a.id}
                  disabled={done}
                  activeOpacity={0.7}
                  onPress={() => setSelected(a)}
                  style={{ opacity: done ? 0.5 : 1 }}
                  className={`flex-row items-center mb-3 px-4 py-4 rounded-2xl border ${cardBgColor(
                    darkmode
                  )} ${isSelected ? "border-[#C2410C]" : borderColor(darkmode)}`}
                >
                  <StreakFlame streak={a.streak ?? 0} size={28} glow={false} />
                  <View className="flex-1 ml-3">
                    <Text className={`text-base font-medium ${textColor(darkmode)}`}>{a.name}</Text>
                    <Text className={`text-xs mt-0.5 ${mutedTextColor(darkmode)}`}>
                      {done ? t.Streaks.done : formatString(t.Streaks.left, { n: remaining })}
                    </Text>
                  </View>
                  <Icon
                    name={isSelected ? "radio-button-on" : "radio-button-off"}
                    size={20}
                    color={isSelected ? "#C2410C" : darkmode ? "#8A847A" : "#7B7266"}
                  />
                </TouchableOpacity>
              );
            })
          ))}

        {step === 3 && selected && (
          <View>
            <View className={`flex-row rounded-2xl border overflow-hidden ${cardBgColor(darkmode)} ${borderColor(darkmode)}`}>
              {!!picked && <CroppedImage picked={picked} maxWidth={96} maxHeight={120} cover />}
              <View className="flex-1 p-4 justify-center">
                <Text className={`text-base font-medium ${textColor(darkmode)}`}>{selected.name}</Text>
                <Text className={`text-xs mt-1 ${mutedTextColor(darkmode)}`}>
                  {t.ActivityForm.timeRates[selected.timeRate] ?? ""}
                </Text>
              </View>
            </View>

            {(() => {
              const value = predictedStreak(selected);
              const tier = tierFor(value);
              return (
                <View className="items-center mt-8">
                  <StreakFlame streak={value} size={72} animated />
                  <Text
                    style={{ fontFamily: fonts.displayMedium, color: inkFor(tier, darkmode) }}
                    className="text-base mt-4"
                  >
                    {formatString(t.UploadForm.streakAfter, {
                      n: value,
                      unit: streakUnit(t.Streaks, selected.timeRate, value),
                    })}
                  </Text>
                </View>
              );
            })()}
          </View>
        )}
      </Stepper>

      <LevelUpModal
        visible={!!levelUp}
        streak={levelUp?.streak ?? 0}
        timeRate={levelUp?.timeRate ?? "daily"}
        onClose={() => setLevelUp(null)}
      />
    </>
  );
};

export default UploadFormScreen;
