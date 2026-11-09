import React, { useEffect, useMemo, useRef } from "react";
import { Animated, Easing, View } from "react-native";
import { useIsFocused } from "@react-navigation/native";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
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

/*
 * The flame is built from overlapping tongues that share one gradient, so
 * they read as a single silhouette. Each tongue moves on its own wave, which
 * makes the outline change shape the way real fire does. Paths live in a
 * 100×120 box with the base at y≈117.
 */
type Layer = {
  d: string;
  fill: "outer" | "inner" | "core";
  /** Point the tongue grows and sways from, in viewBox units. */
  pivot: [number, number];
};

const LAYERS: Record<string, Layer> = {
  left: {
    d: "M21 30 C17 48 13 64 18 80 C21 89 29 95 42 95 C37 78 35 64 31 54 C27 46 22 40 21 30 Z",
    fill: "outer",
    pivot: [42, 95],
  },
  right: {
    d: "M80 22 C85 42 88 62 83 79 C80 88 71 95 58 95 C63 76 65 62 69 52 C73 42 79 34 80 22 Z",
    fill: "outer",
    pivot: [58, 95],
  },
  main: {
    d: "M51 2 C55 24 70 38 71 60 C72 80 64 98 50 98 C36 98 29 84 31 66 C33 46 48 30 51 2 Z",
    fill: "outer",
    pivot: [50, 100],
  },
  body: {
    d: "M50 40 C66 50 84 64 83 86 C82 106 67 117 50 117 C33 117 18 106 17 86 C16 64 34 50 50 40 Z",
    fill: "outer",
    pivot: [50, 117],
  },
  innerTip: {
    d: "M48 46 C52 60 62 70 64 84 C66 98 58 108 50 108 C42 108 35 100 36 88 C37 76 46 66 48 46 Z",
    fill: "inner",
    pivot: [50, 108],
  },
  innerBody: {
    d: "M50 74 C60 82 68 90 66 102 C64 110 58 113 50 113 C42 113 36 110 34 102 C32 90 40 82 50 74 Z",
    fill: "inner",
    pivot: [50, 113],
  },
  /** White-hot heart at the base. */
  core: {
    d: "M50 82 C53 90 58 96 58 102 C58 108 54 111 50 111 C46 111 42 108 42 102 C42 96 47 90 50 82 Z",
    fill: "core",
    pivot: [50, 111],
  },
};

/** Back to front. */
const ORDER = ["left", "right", "main", "body", "innerTip", "innerBody", "core"] as const;

/** Wave lengths relative to the tier's period; uneven so the loop never visibly repeats. */
const WAVES = [1, 1.31, 1.73, 0.83];

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

const FlameDefs = ({ tier, ids }: { tier: Tier; ids: Record<string, string> }) => (
  <Defs>
    <LinearGradient id={ids.outer} x1="0" y1="0" x2="0" y2="117" gradientUnits="userSpaceOnUse">
      <Stop offset="0" stopColor={tier.outer[0]} />
      <Stop offset="1" stopColor={tier.outer[1]} />
    </LinearGradient>
    <LinearGradient id={ids.inner} x1="0" y1="44" x2="0" y2="113" gradientUnits="userSpaceOnUse">
      <Stop offset="0" stopColor={tier.inner[0]} />
      <Stop offset="1" stopColor={tier.inner[1]} />
    </LinearGradient>
    <LinearGradient id={ids.core} x1="0" y1="82" x2="0" y2="111" gradientUnits="userSpaceOnUse">
      <Stop offset="0" stopColor="#FFFFFF" stopOpacity={0} />
      <Stop offset="0.6" stopColor="#FFFBEB" stopOpacity={0.85} />
      <Stop offset="1" stopColor="#FFFFFF" stopOpacity={0.95} />
    </LinearGradient>
  </Defs>
);

const StreakFlame = ({ streak, size, animated = false, glow = true }: Props) => {
  const tier = tierFor(streak);
  const ids = useMemo(() => {
    gradientSeq += 1;
    return {
      outer: `fo${gradientSeq}`,
      inner: `fi${gradientSeq}`,
      core: `fc${gradientSeq}`,
      glow: `fg${gradientSeq}`,
    };
  }, []);
  const waves = useRef(WAVES.map(() => new Animated.Value(0))).current;
  // Off-screen flames stop animating to save battery.
  const focused = useIsFocused();
  const live = animated && focused && tier.period > 0;

  useEffect(() => {
    if (!live) return;
    const loops = waves.map((value, i) => {
      const half = (tier.period * WAVES[i]) / 2;
      return Animated.loop(
        Animated.sequence([
          Animated.timing(value, { toValue: 1, duration: half, easing: Easing.inOut(Easing.sin), useNativeDriver: true }),
          Animated.timing(value, { toValue: 0, duration: half, easing: Easing.inOut(Easing.sin), useNativeDriver: true }),
        ])
      );
    });
    loops.forEach((l) => l.start());
    return () => loops.forEach((l) => l.stop());
  }, [live, tier.period, waves]);

  const height = size * 1.2;
  const showCore = !!tier.glow;
  const layers = ORDER.filter((key) => key !== "core" || showCore);

  /** A wave going from `from` to `to` and back. */
  const w = (i: number, from: number, to: number) =>
    waves[i].interpolate({ inputRange: [0, 1], outputRange: [from, to] });
  const deg = (i: number, from: number, to: number) =>
    waves[i].interpolate({ inputRange: [0, 1], outputRange: [`${from}deg`, `${to}deg`] });

  // RN transforms pivot on the view's centre: move to the tongue's base,
  // transform, move back.
  const around = ([px, py]: [number, number], transforms: any[]) => {
    const dx = ((px - 50) / 100) * size;
    const dy = ((py - 60) / 120) * height;
    return [{ translateX: dx }, { translateY: dy }, ...transforms, { translateY: -dy }, { translateX: -dx }];
  };

  const motion: Record<string, any> = {
    left: { transform: around(LAYERS.left.pivot, [{ rotate: deg(1, -4, 4) }, { scaleY: w(2, 0.68, 1.04) }]) },
    right: { transform: around(LAYERS.right.pivot, [{ rotate: deg(2, -4, 4) }, { scaleY: w(3, 0.68, 1.04) }]) },
    main: {
      transform: around(LAYERS.main.pivot, [
        { rotate: deg(0, -3, 3) },
        { scaleY: w(1, 0.85, 1.03) },
        { scaleX: w(1, 1.03, 0.97) },
      ]),
    },
    body: { transform: around(LAYERS.body.pivot, [{ rotate: deg(0, -1, 1) }, { scaleX: w(1, 0.98, 1.02) }]) },
    innerTip: { transform: around(LAYERS.innerTip.pivot, [{ rotate: deg(3, 4, -4) }, { scaleY: w(0, 0.78, 1.02) }]) },
    innerBody: undefined,
    core: {
      opacity: w(1, 0.95, 0.7),
      transform: around(LAYERS.core.pivot, [{ rotate: deg(3, 2, -2) }, { scaleY: w(1, 0.92, 1.08) }]),
    },
  };
  const glowStyle: any = live
    ? { opacity: w(0, 0.7, 1), transform: [{ scale: w(0, 0.96, 1.04) }] }
    : undefined;

  const showGlow = glow && tier.glow;
  // On the light page a red halo reads as a pink smudge: there it uses the
  // flame's warm top colour and stays faint; on dark it can glow fully.
  const { darkmode } = useSettingsContext();
  const halo = darkmode
    ? { color: tier.glow, stops: [0.42, 0.2, 0.06] }
    : { color: tier.outer[0], stops: [0.3, 0.12, 0.03] };
  // Halo centred on the body of the flame and fully inside its box, so it fades
  // out on every side instead of ending in a straight edge.
  const glowSize = size * 1.9;
  const fillFor = (layer: Layer) => `url(#${ids[layer.fill]})`;

  return (
    <View style={{ width: size, height }} pointerEvents="none">
      {showGlow && (
        <Animated.View
          style={[
            {
              position: "absolute",
              left: (size - glowSize) / 2,
              top: height * 0.62 - glowSize / 2,
              width: glowSize,
              height: glowSize,
            },
            glowStyle,
          ]}
        >
          <Svg width="100%" height="100%" viewBox="0 0 100 100">
            <Defs>
              <RadialGradient id={ids.glow} cx="50" cy="50" r="50" gradientUnits="userSpaceOnUse">
                <Stop offset="0" stopColor={halo.color} stopOpacity={halo.stops[0]} />
                <Stop offset="0.35" stopColor={halo.color} stopOpacity={halo.stops[1]} />
                <Stop offset="0.7" stopColor={halo.color} stopOpacity={halo.stops[2]} />
                <Stop offset="1" stopColor={halo.color} stopOpacity={0} />
              </RadialGradient>
            </Defs>
            <Rect x="0" y="0" width="100" height="100" fill={`url(#${ids.glow})`} />
          </Svg>
        </Animated.View>
      )}

      {live ? (
        // One view per tongue so each can move on the native thread.
        layers.map((key) => (
          <Animated.View key={key} style={[{ position: "absolute", width: size, height }, motion[key]]}>
            <Svg width={size} height={height} viewBox="0 0 100 120">
              <FlameDefs tier={tier} ids={ids} />
              <Path d={LAYERS[key].d} fill={fillFor(LAYERS[key])} />
            </Svg>
          </Animated.View>
        ))
      ) : (
        // Still flames (lists, badges) draw everything in a single SVG.
        <Svg width={size} height={height} viewBox="0 0 100 120">
          <FlameDefs tier={tier} ids={ids} />
          {layers.map((key) => (
            <Path key={key} d={LAYERS[key].d} fill={fillFor(LAYERS[key])} />
          ))}
        </Svg>
      )}

      {live &&
        Array.from({ length: tier.embers }).map((_, i) => (
          <Ember
            key={i}
            size={size}
            color={tier.inner[0]}
            period={tier.period}
            delay={(i * tier.period * 1.8) / Math.max(1, tier.embers)}
            offsetX={((i % 2 === 0 ? 1 : -1) * size * (0.08 + (i % 3) * 0.07))}
          />
        ))}
    </View>
  );
};

export default StreakFlame;
