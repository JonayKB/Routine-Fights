import React, { useEffect, useRef } from "react";
import { Animated, Easing, ViewStyle } from "react-native";
import { useIsFocused } from "@react-navigation/native";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { colors } from "../styles/theme";

type Props = {
  width?: ViewStyle["width"];
  height: number;
  radius?: number;
  style?: ViewStyle;
};

/** A placeholder block that breathes softly while content loads. */
const Skeleton = ({ width = "100%", height, radius = 12, style }: Props) => {
  const { darkmode } = useSettingsContext();
  const focused = useIsFocused();
  const opacity = useRef(new Animated.Value(0.55)).current;

  useEffect(() => {
    if (!focused) return;
    const loop = Animated.loop(
      Animated.sequence([
        Animated.timing(opacity, { toValue: 1, duration: 700, easing: Easing.inOut(Easing.quad), useNativeDriver: true }),
        Animated.timing(opacity, { toValue: 0.55, duration: 700, easing: Easing.inOut(Easing.quad), useNativeDriver: true }),
      ])
    );
    loop.start();
    return () => loop.stop();
  }, [focused, opacity]);

  return (
    <Animated.View
      style={[
        { width, height, borderRadius: radius, backgroundColor: colors(darkmode).raised, opacity },
        style,
      ]}
    />
  );
};

export default Skeleton;
