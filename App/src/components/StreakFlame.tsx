import React, { useEffect, useMemo, useRef } from "react";
import { Animated, Easing, View } from "react-native";
import { useIsFocused } from "@react-navigation/native";
import Svg, {
  Defs,
  LinearGradient,
  Path,
  RadialGradient,
  Rect,
  Stop,
} from "react-native-svg";

/**
 * A flame that heats up as a streak grows, the way real fire does:
 * orange → red → crimson → blue → violet plasma → white-hot.
 */

export type TierKey =
  | "unlit"
  | "spark"
  | "blaze"
  | "inferno"
  | "blue"
  | "plasma"
  | "legend";

export type Tier = {
  key: TierKey;
  min: number;
  outer: [string, string];
  inner: [string, string];
  glow: string | null;
  /** Colour used for the number and chip text next to the flame. */
  ink: string;
  embers: number;
  /** Flicker period in ms; hotter fire flickers faster. */
  period: number;
};

export const TIERS: Tier[] = [
  { key: "unlit", min: 0, outer: ["#6B6259", "#3A342E"], inner: ["#9A9187", "#5E554A"], glow: null, ink: "#8A847A", embers: 0, period: 0 },
  { key: "spark", min: 1, outer: ["#FDBA74", "#EA580C"], inner: ["#FB923C", "#FEF3C7"], glow: "#F97316", ink: "#EA580C", embers: 0, period: 1400 },
  { key: "blaze", min: 3, outer: ["#FB923C", "#DC2626"], inner: ["#F97316", "#FDE68A"], glow: "#EF4444", ink: "#DC2626", embers: 2, period: 1200 },
  { key: "inferno", min: 7, outer: ["#FB7185", "#9F1239"], inner: ["#E11D48", "#FDBA74"], glow: "#E11D48", ink: "#E11D48", embers: 3, period: 1000 },
  { key: "blue", min: 14, outer: ["#7DD3FC", "#1D4ED8"], inner: ["#38BDF8", "#F0F9FF"], glow: "#3B82F6", ink: "#2563EB", embers: 4, period: 850 },
  { key: "plasma", min: 30, outer: ["#E9D5FF", "#6D28D9"], inner: ["#C084FC", "#FAF5FF"], glow: "#8B5CF6", ink: "#7C3AED", embers: 5, period: 750 },
  { key: "legend", min: 100, outer: ["#FFFFFF", "#EAB308"], inner: ["#FEF08A", "#FFFFFF"], glow: "#FDE047", ink: "#CA8A04", embers: 6, period: 650 },
];

export const tierFor = (streak: number): Tier => {
  const s = Math.max(0, streak ?? 0);
  return [...TIERS].reverse().find((t) => s >= t.min) ?? TIERS[0];
};

/** Text colour for a tier: the deep tone on light surfaces, the bright tone on dark ones. */
export const inkFor = (tier: Tier, darkmode: boolean) =>
  darkmode ? tier.outer[0] : tier.ink;

export const nextTierFor = (streak: number): Tier | null => {
  const s = Math.max(0, streak ?? 0);
  return TIERS.find((t) => t.min > s) ?? null;
};

const OUTER =
  "M50 4 C55 22 70 32 78 50 C86 68 84 92 70 106 C62 114 56 117 50 117 C44 117 38 114 30 106 C16 92 14 70 24 52 C28 60 33 64 38 66 C34 46 40 24 50 4 Z";
const INNER =
  "M50 52 C53 64 62 72 65 84 C68 98 62 110 50 112 C38 110 32 99 35 87 C37 92 40 95 44 96 C42 82 45 66 50 52 Z";

type Props = {
  streak: number;
  /** Width in dp; height is 1.2× the width. */
  size: number;
  animated?: boolean;
  /** Show the coloured halo behind the flame. */
  glow?: boolean;
};

let gradientSeq = 0;

const Ember = ({
  size,
  color,
  delay,
  period,
  offsetX,
}: {
  size: number;
  color: string;
  delay: number;
  period: number;
  offsetX: number;
}) => {
  const progress = useRef(new Animated.Value(0)).current;

  useEffect(() => {
    const loop = Animated.loop(
      Animated.sequence([
        Animated.delay(delay),
        Animated.timing(progress, {
          toValue: 1,
          duration: period * 1.8,
          easing: Easing.out(Easing.quad),
          useNativeDriver: true,
        }),
        Animated.timing(progress, { toValue: 0, duration: 0, useNativeDriver: true }),
      ])
    );
    loop.start();
    return () => loop.stop();
  }, [delay, period, progress]);

  const dot = Math.max(2, Math.round(size * 0.05));

  return (
    <Animated.View
      pointerEvents="none"
      style={{
        position: "absolute",
        left: size / 2 + offsetX - dot / 2,
        top: size * 0.55,
        width: dot,
        height: dot,
        borderRadius: dot / 2,
        backgroundColor: color,
        opacity: progress.interpolate({
          inputRange: [0, 0.15, 0.7, 1],
          outputRange: [0, 1, 0.6, 0],
        }),
        transform: [
          {
            translateY: progress.interpolate({
              inputRange: [0, 1],
              outputRange: [0, -size * 0.75],
            }),
          },
          {
            translateX: progress.interpolate({
              inputRange: [0, 0.5, 1],
              outputRange: [0, offsetX * 0.4, -offsetX * 0.2],
            }),
          },
        ],
      }}
    />
  );
};

const StreakFlame = ({ streak, size, animated = false, glow = true }: Props) => {
  const tier = tierFor(streak);
  const ids = useMemo(() => {
    gradientSeq += 1;
    return { o: `fo${gradientSeq}`, i: `fi${gradientSeq}`, g: `fg${gradientSeq}` };
  }, []);
  const flicker = useRef(new Animated.Value(0)).current;
  // Off-screen flames stop animating to save battery.
  const focused = useIsFocused();
  const live = animated && focused && tier.period > 0;

  useEffect(() => {
    if (!live) return;
    const half = tier.period / 2;
    const loop = Animated.loop(
      Animated.sequence([
        Animated.timing(flicker, { toValue: 1, duration: half, easing: Easing.inOut(Easing.sin), useNativeDriver: true }),
        Animated.timing(flicker, { toValue: 0, duration: half, easing: Easing.inOut(Easing.sin), useNativeDriver: true }),
      ])
    );
    loop.start();
    return () => loop.stop();
  }, [live, tier.period, flicker]);

  const height = size * 1.2;
  // Scale from the base of the flame, not its centre.
  const outerStyle: any = live
    ? {
        transform: [
          { translateY: flicker.interpolate({ inputRange: [0, 1], outputRange: [0, -height * 0.025] }) },
          { scaleY: flicker.interpolate({ inputRange: [0, 1], outputRange: [1, 1.05] }) },
          { scaleX: flicker.interpolate({ inputRange: [0, 1], outputRange: [1, 0.97] }) },
        ],
      }
    : undefined;
  const innerStyle: any = live
    ? {
        transform: [
          { translateY: flicker.interpolate({ inputRange: [0, 1], outputRange: [0, -height * 0.05] }) },
          { scaleY: flicker.interpolate({ inputRange: [0, 1], outputRange: [1.04, 0.94] }) },
        ],
      }
    : undefined;
  const glowStyle: any = live
    ? { opacity: flicker.interpolate({ inputRange: [0, 1], outputRange: [0.75, 1] }) }
    : undefined;

  const showGlow = glow && tier.glow;

  return (
    <View style={{ width: size, height }} pointerEvents="none">
      {showGlow && (
        <Animated.View style={[{ position: "absolute", left: -size * 0.35, top: -size * 0.2, width: size * 1.7, height: height * 1.4 }, glowStyle]}>
          <Svg width="100%" height="100%" viewBox="0 0 100 100">
            <Defs>
              <RadialGradient id={ids.g} cx="50" cy="60" r="50" gradientUnits="userSpaceOnUse">
                <Stop offset="0" stopColor={tier.glow} stopOpacity={0.45} />
                <Stop offset="1" stopColor={tier.glow} stopOpacity={0} />
              </RadialGradient>
            </Defs>
            <Rect x="0" y="0" width="100" height="100" fill={`url(#${ids.g})`} />
          </Svg>
        </Animated.View>
      )}

      <Animated.View style={[{ position: "absolute", width: size, height }, outerStyle]}>
        <Svg width={size} height={height} viewBox="0 0 100 120">
          <Defs>
            <LinearGradient id={ids.o} x1="0" y1="0" x2="0" y2="1">
              <Stop offset="0" stopColor={tier.outer[0]} />
              <Stop offset="1" stopColor={tier.outer[1]} />
            </LinearGradient>
          </Defs>
          <Path d={OUTER} fill={`url(#${ids.o})`} />
        </Svg>
      </Animated.View>

      <Animated.View style={[{ position: "absolute", width: size, height }, innerStyle]}>
        <Svg width={size} height={height} viewBox="0 0 100 120">
          <Defs>
            <LinearGradient id={ids.i} x1="0" y1="0" x2="0" y2="1">
              <Stop offset="0" stopColor={tier.inner[0]} />
              <Stop offset="1" stopColor={tier.inner[1]} />
            </LinearGradient>
          </Defs>
          <Path d={INNER} fill={`url(#${ids.i})`} />
        </Svg>
      </Animated.View>

      {live &&
        Array.from({ length: tier.embers }).map((_, i) => (
          <Ember
            key={i}
            size={size}
            color={tier.outer[0]}
            period={tier.period}
            delay={(i * tier.period * 1.8) / Math.max(1, tier.embers)}
            offsetX={((i % 2 === 0 ? 1 : -1) * size * (0.08 + (i % 3) * 0.07))}
          />
        ))}
    </View>
  );
};

export default StreakFlame;
