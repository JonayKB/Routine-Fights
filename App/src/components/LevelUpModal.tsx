import React, { useEffect, useRef } from "react";
import { Animated, Easing, Modal, Text, TouchableOpacity, Vibration, View } from "react-native";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { translations } from "../../translations/translation";
import StreakFlame, { tierFor } from "./StreakFlame";
import { formatString, streakUnit } from "./Streak";
import { fonts } from "../styles/theme";

type Props = {
  visible: boolean;
  streak: number;
  timeRate: string;
  onClose: () => void;
};

/** Celebrates reaching a hotter flame tier right after posting. */
const LevelUpModal = ({ visible, streak, timeRate, onClose }: Props) => {
  const { language } = useSettingsContext();
  const t = translations[language || "en-EN"].screens.Streaks;
  const tier = tierFor(streak);
  const enter = useRef(new Animated.Value(0)).current;

  useEffect(() => {
    if (!visible) return;
    enter.setValue(0);
    Vibration.vibrate([0, 30, 60, 40]);
    Animated.timing(enter, {
      toValue: 1,
      duration: 450,
      easing: Easing.out(Easing.back(1.4)),
      useNativeDriver: true,
    }).start();
  }, [visible, enter]);

  return (
    <Modal visible={visible} transparent animationType="fade" onRequestClose={onClose}>
      <View className="flex-1 items-center justify-center bg-[#000000B3] px-8">
        <Animated.View
          style={{
            opacity: enter,
            transform: [{ scale: enter.interpolate({ inputRange: [0, 1], outputRange: [0.85, 1] }) }],
          }}
          className="w-full items-center rounded-3xl bg-[#12110F] border border-[#EFE9DE1A] px-6 pt-10 pb-6"
        >
          <StreakFlame streak={streak} size={120} animated />
          <Text className="text-sm text-[#8A847A] mt-6">{t.levelUp}</Text>
          <Text style={{ fontFamily: fonts.display, color: tier.outer[0] }} className="text-3xl mt-1">
            {t.tiers[tier.key]}
          </Text>
          <Text className="text-base text-[#AAA398] mt-2">
            {formatString(t.levelUpBody, { n: streak, unit: streakUnit(t, timeRate, streak) })}
          </Text>
          <TouchableOpacity
            activeOpacity={0.85}
            onPress={onClose}
            className="w-full h-12 mt-8 rounded-xl bg-[#EFE9DE] items-center justify-center"
          >
            <Text className="text-base font-semibold text-[#12110F]">{t.keepGoing}</Text>
          </TouchableOpacity>
        </Animated.View>
      </View>
    </Modal>
  );
};

export default LevelUpModal;
