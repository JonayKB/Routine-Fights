import React, { useEffect, useMemo, useState } from "react";
import {
  ActivityIndicator,
  Image,
  LayoutChangeEvent,
  Modal,
  Pressable,
  StatusBar,
  Text,
  TouchableOpacity,
  View,
} from "react-native";
import { Gesture, GestureDetector, GestureHandlerRootView } from "react-native-gesture-handler";
import Animated, {
  Easing,
  useAnimatedStyle,
  useSharedValue,
  withSpring,
  withTiming,
} from "react-native-reanimated";
import { SafeAreaView } from "react-native-safe-area-context";
import Svg, { Path } from "react-native-svg";
import Icon from "react-native-vector-icons/Ionicons";
import { translations } from "../../translations/translation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { Crop, normalizeRotation } from "../utils/images";

export type CropSource = {
  uri: string;
  /** Size reported by the picker; used if the image can't be measured. */
  width?: number;
  height?: number;
};

type Props = {
  source: CropSource | null;
  /** Aspect ratio of the result, width / height. */
  aspect: number;
  /** Shows a round mask (the result is still a square). */
  circle?: boolean;
  onCancel: () => void;
  onDone: (result: { crop: Crop; width: number; height: number }) => void;
};

const ACCENT = "#C2410C";
const MAX_ZOOM = 5;
const DOUBLE_TAP_ZOOM = 2.5;
const SPRING = { damping: 22, stiffness: 220, mass: 0.8 };
const PADDING = 20;

/**
 * Full-screen photo editor: pinch to zoom, drag to move, double tap to zoom
 * in or out, rotate in 90° steps. The picture always covers the frame. It
 * doesn't produce a new file: it returns the crop as fractions of the photo,
 * which the API applies on upload.
 */
const PhotoCropper = ({ source, aspect, circle, onCancel, onDone }: Props) => {
  const { language } = useSettingsContext();
  const t = translations[language || "en-EN"].screens.ImageEditor;

  const [natural, setNatural] = useState<{ w: number; h: number } | null>(null);
  const [failed, setFailed] = useState(false);
  const [stage, setStage] = useState<{ w: number; h: number } | null>(null);
  const [rotation, setRotation] = useState(0);

  // Gesture state, all on the UI thread.
  const scale = useSharedValue(1);
  const tx = useSharedValue(0);
  const ty = useSharedValue(0);
  const rot = useSharedValue(0);
  const coverScale = useSharedValue(1);
  const startScale = useSharedValue(1);
  const startTx = useSharedValue(0);
  const startTy = useSharedValue(0);
  const startFx = useSharedValue(0);
  const startFy = useSharedValue(0);
  const grid = useSharedValue(0);

  // Measure the picture as it will be displayed (EXIF orientation applied).
  useEffect(() => {
    setNatural(null);
    setFailed(false);
    setRotation(0);
    rot.value = 0;
    scale.value = 1;
    tx.value = 0;
    ty.value = 0;
    if (!source) return;
    Image.getSize(
      source.uri,
      (w, h) => setNatural({ w, h }),
      () =>
        source.width && source.height
          ? setNatural({ w: source.width, h: source.height })
          : setFailed(true)
    );
  }, [source?.uri]);

  const frame = useMemo(() => {
    if (!stage) return null;
    const maxW = stage.w - PADDING * 2;
    const maxH = stage.h - PADDING * 2;
    let w = maxW;
    let h = w / aspect;
    if (h > maxH) {
      h = maxH;
      w = h * aspect;
    }
    return { w, h };
  }, [stage, aspect]);

  // The image is laid out at the scale that covers the frame unrotated; the
  // extra factor for rotated orientations is applied through `coverScale`.
  const layoutScale = useMemo(() => {
    if (!natural || !frame) return 1;
    return Math.max(frame.w / natural.w, frame.h / natural.h);
  }, [natural, frame]);

  const coverFor = (deg: number) => {
    if (!natural || !frame) return 1;
    const turned = normalizeRotation(deg) % 180 !== 0;
    const rw = turned ? natural.h : natural.w;
    const rh = turned ? natural.w : natural.h;
    return Math.max(frame.w / rw, frame.h / rh) / layoutScale;
  };

  useEffect(() => {
    coverScale.value = coverFor(rotation);
  }, [natural, frame, layoutScale]);

  const imgW = (natural?.w ?? 0) * layoutScale;
  const imgH = (natural?.h ?? 0) * layoutScale;
  const fw = frame?.w ?? 0;
  const fh = frame?.h ?? 0;

  /** Size of the rotated picture on screen for a given zoom. */
  const visualSize = (s: number, c: number, deg: number) => {
    "worklet";
    const turned = Math.abs(Math.round(deg / 90)) % 2 === 1;
    const w = (turned ? imgH : imgW) * c * s;
    const h = (turned ? imgW : imgH) * c * s;
    return { w, h };
  };

  const clampOffset = (value: number, size: number, frameSize: number) => {
    "worklet";
    const max = Math.max(0, (size - frameSize) / 2);
    return Math.min(max, Math.max(-max, value));
  };

  /** Springs zoom and position back inside the allowed range. */
  const settle = () => {
    "worklet";
    const s = Math.min(MAX_ZOOM, Math.max(1, scale.value));
    const { w, h } = visualSize(s, coverScale.value, rot.value);
    scale.value = withSpring(s, SPRING);
    tx.value = withSpring(clampOffset(tx.value, w, fw), SPRING);
    ty.value = withSpring(clampOffset(ty.value, h, fh), SPRING);
  };

  /** Lets the picture go a little past its limits while dragging. */
  const rubber = (value: number, size: number, frameSize: number) => {
    "worklet";
    const max = Math.max(0, (size - frameSize) / 2);
    if (value > max) return max + (value - max) * 0.35;
    if (value < -max) return -max + (value + max) * 0.35;
    return value;
  };

  const pan = Gesture.Pan()
    .maxPointers(1)
    .onStart(() => {
      startTx.value = tx.value;
      startTy.value = ty.value;
      grid.value = withTiming(1, { duration: 150 });
    })
    .onUpdate((e) => {
      const { w, h } = visualSize(scale.value, coverScale.value, rot.value);
      tx.value = rubber(startTx.value + e.translationX, w, fw);
      ty.value = rubber(startTy.value + e.translationY, h, fh);
    })
    .onFinalize(() => {
      grid.value = withTiming(0, { duration: 250 });
      settle();
    });

  const pinch = Gesture.Pinch()
    .onStart((e) => {
      startScale.value = scale.value;
      startTx.value = tx.value;
      startTy.value = ty.value;
      // Focal point relative to the centre of the stage.
      startFx.value = e.focalX - (stage?.w ?? 0) / 2;
      startFy.value = e.focalY - (stage?.h ?? 0) / 2;
      grid.value = withTiming(1, { duration: 150 });
    })
    .onUpdate((e) => {
      let s = startScale.value * e.scale;
      if (s < 1) s = 1 - (1 - s) * 0.4;
      if (s > MAX_ZOOM) s = MAX_ZOOM + (s - MAX_ZOOM) * 0.3;
      const k = s / startScale.value;
      const fx = e.focalX - (stage?.w ?? 0) / 2;
      const fy = e.focalY - (stage?.h ?? 0) / 2;
      // Keep the point under the fingers in place while it follows them.
      scale.value = s;
      tx.value = fx - (startFx.value - startTx.value) * k;
      ty.value = fy - (startFy.value - startTy.value) * k;
    })
    .onFinalize(() => {
      grid.value = withTiming(0, { duration: 250 });
      settle();
    });

  const doubleTap = Gesture.Tap()
    .numberOfTaps(2)
    .maxDuration(250)
    .onEnd((e) => {
      if (scale.value > 1.05) {
        scale.value = withSpring(1, SPRING);
        tx.value = withSpring(0, SPRING);
        ty.value = withSpring(0, SPRING);
        return;
      }
      const s = DOUBLE_TAP_ZOOM;
      const k = s / scale.value;
      const fx = e.x - (stage?.w ?? 0) / 2;
      const fy = e.y - (stage?.h ?? 0) / 2;
      const { w, h } = visualSize(s, coverScale.value, rot.value);
      scale.value = withSpring(s, SPRING);
      tx.value = withSpring(clampOffset(fx - (fx - tx.value) * k, w, fw), SPRING);
      ty.value = withSpring(clampOffset(fy - (fy - ty.value) * k, h, fh), SPRING);
    });

  const gesture = Gesture.Race(doubleTap, Gesture.Simultaneous(pan, pinch));

  const imageStyle = useAnimatedStyle(() => ({
    transform: [
      { translateX: tx.value },
      { translateY: ty.value },
      { scale: scale.value * coverScale.value },
      { rotate: `${rot.value}deg` },
    ] as const,
  }));

  const gridStyle = useAnimatedStyle(() => ({ opacity: grid.value }));

  const rotate = () => {
    const next = rotation - 90;
    setRotation(next);
    const timing = { duration: 280, easing: Easing.out(Easing.cubic) };
    rot.value = withTiming(next, timing);
    coverScale.value = withTiming(coverFor(next), timing);
    scale.value = withTiming(1, timing);
    tx.value = withTiming(0, timing);
    ty.value = withTiming(0, timing);
  };

  const reset = () => {
    const target = Math.round(rotation / 360) * 360;
    setRotation(target);
    rot.value = withSpring(target, SPRING);
    coverScale.value = withSpring(coverFor(0), SPRING);
    scale.value = withSpring(1, SPRING);
    tx.value = withSpring(0, SPRING);
    ty.value = withSpring(0, SPRING);
  };

  const confirm = () => {
    if (!natural || !frame) return;
    const deg = normalizeRotation(rotation);
    const { w, h } = visualSize(scale.value, coverScale.value, deg);
    const x = clampOffset(tx.value, w, fw);
    const y = clampOffset(ty.value, h, fh);
    const clamp01 = (v: number) => Math.min(1, Math.max(0, v));
    const crop: Crop = {
      x: clamp01((w / 2 - fw / 2 - x) / w),
      y: clamp01((h / 2 - fh / 2 - y) / h),
      width: clamp01(fw / w),
      height: clamp01(fh / h),
      rotation: deg,
    };
    onDone({ crop, width: natural.w, height: natural.h });
  };

  const onStageLayout = (e: LayoutChangeEvent) => {
    const { width, height } = e.nativeEvent.layout;
    setStage({ w: width, h: height });
  };

  const ready = !!natural && !!frame && !!stage;
  const left = stage ? (stage.w - fw) / 2 : 0;
  const top = stage ? (stage.h - fh) / 2 : 0;

  // Dimmed area around the frame, with a rectangular or round hole.
  const maskPath = useMemo(() => {
    if (!stage || !frame) return "";
    const outer = `M0 0H${stage.w}V${stage.h}H0Z`;
    if (circle) {
      const r = Math.min(fw, fh) / 2;
      const cx = stage.w / 2;
      const cy = stage.h / 2;
      return `${outer}M${cx - r} ${cy}a${r} ${r} 0 1 0 ${r * 2} 0a${r} ${r} 0 1 0 ${-r * 2} 0Z`;
    }
    return `${outer}M${left} ${top}h${fw}v${fh}h${-fw}Z`;
  }, [stage, frame, circle, left, top, fw, fh]);

  const Corner = ({ style }: { style: object }) => (
    <View pointerEvents="none" style={[{ position: "absolute", width: 22, height: 22, borderColor: "#FFF" }, style]} />
  );

  return (
    <Modal
      visible={!!source}
      animationType="slide"
      statusBarTranslucent
      presentationStyle="fullScreen"
      onRequestClose={onCancel}
    >
      <StatusBar barStyle="light-content" backgroundColor="#0B0A09" />
      <GestureHandlerRootView style={{ flex: 1, backgroundColor: "#0B0A09" }}>
        {/* A modal is its own window: the native view measures the notch and
            the home indicator there, where the app's inset context can't. */}
        <SafeAreaView edges={["top", "bottom", "left", "right"]} style={{ flex: 1 }}>
        {/* Top bar */}
        <View className="flex-row items-center justify-between px-4 pt-2 pb-3">
          <Pressable
            onPress={onCancel}
            hitSlop={10}
            accessibilityRole="button"
            accessibilityLabel={t.cancel}
            className="w-10 h-10 rounded-full items-center justify-center bg-[#FFFFFF14]"
          >
            <Icon name="close" size={22} color="#EFE9DE" />
          </Pressable>
          <Text className="text-base font-semibold text-[#EFE9DE]">{t.title}</Text>
          <TouchableOpacity
            onPress={confirm}
            disabled={!ready}
            activeOpacity={0.8}
            accessibilityRole="button"
            style={{
              height: 40,
              paddingHorizontal: 16,
              borderRadius: 20,
              flexDirection: "row",
              alignItems: "center",
              backgroundColor: ACCENT,
              opacity: ready ? 1 : 0.4,
            }}
          >
            <Icon name="checkmark" size={18} color="#FFF" />
            <Text className="ml-1 text-sm font-semibold text-white">{t.done}</Text>
          </TouchableOpacity>
        </View>

        {/* Stage */}
        <GestureDetector gesture={gesture}>
          <View style={{ flex: 1, overflow: "hidden" }} onLayout={onStageLayout} collapsable={false}>
            {ready && source && (
              <Animated.Image
                source={{ uri: source.uri }}
                fadeDuration={150}
                style={[
                  {
                    position: "absolute",
                    width: imgW,
                    height: imgH,
                    left: (stage!.w - imgW) / 2,
                    top: (stage!.h - imgH) / 2,
                  },
                  imageStyle,
                ]}
              />
            )}

            {ready && (
              <View pointerEvents="none" style={{ position: "absolute", top: 0, left: 0, right: 0, bottom: 0 }}>
                <Svg width={stage!.w} height={stage!.h}>
                  <Path d={maskPath} fill="rgba(11,10,9,0.72)" fillRule="evenodd" />
                </Svg>

                {/* Frame */}
                <View
                  style={{
                    position: "absolute",
                    left,
                    top,
                    width: fw,
                    height: fh,
                    borderWidth: 1,
                    borderColor: "rgba(255,255,255,0.55)",
                    borderRadius: circle ? fw / 2 : 2,
                  }}
                >
                  {/* Rule-of-thirds grid while interacting */}
                  <Animated.View style={[{ position: "absolute", top: 0, left: 0, right: 0, bottom: 0 }, gridStyle]}>
                    {[1, 2].map((i) => (
                      <React.Fragment key={i}>
                        <View
                          style={{
                            position: "absolute",
                            left: (fw / 3) * i,
                            top: 0,
                            bottom: 0,
                            width: 1,
                            backgroundColor: "rgba(255,255,255,0.35)",
                          }}
                        />
                        <View
                          style={{
                            position: "absolute",
                            top: (fh / 3) * i,
                            left: 0,
                            right: 0,
                            height: 1,
                            backgroundColor: "rgba(255,255,255,0.35)",
                          }}
                        />
                      </React.Fragment>
                    ))}
                  </Animated.View>
                </View>

                {!circle && (
                  <>
                    <Corner style={{ left: left - 2, top: top - 2, borderLeftWidth: 3, borderTopWidth: 3 }} />
                    <Corner style={{ left: left + fw - 20, top: top - 2, borderRightWidth: 3, borderTopWidth: 3 }} />
                    <Corner style={{ left: left - 2, top: top + fh - 20, borderLeftWidth: 3, borderBottomWidth: 3 }} />
                    <Corner
                      style={{ left: left + fw - 20, top: top + fh - 20, borderRightWidth: 3, borderBottomWidth: 3 }}
                    />
                  </>
                )}
              </View>
            )}

            {!ready && (
              <View className="flex-1 items-center justify-center">
                {failed ? (
                  <Text className="text-sm text-[#AAA398]">{t.loadError}</Text>
                ) : (
                  <ActivityIndicator color="#EFE9DE" />
                )}
              </View>
            )}
          </View>
        </GestureDetector>

        {/* Tools */}
        <View className="pt-3 pb-4 px-6">
          <Text className="text-xs text-center text-[#8A847A] mb-4">{t.hint}</Text>
          <View className="flex-row justify-center">
            <Tool icon="refresh-outline" label={t.rotate} onPress={rotate} flip disabled={!ready} />
            <View style={{ width: 28 }} />
            <Tool icon="scan-outline" label={t.reset} onPress={reset} disabled={!ready} />
          </View>
        </View>
        </SafeAreaView>
      </GestureHandlerRootView>
    </Modal>
  );
};

const Tool = ({
  icon,
  label,
  onPress,
  flip,
  disabled,
}: {
  icon: string;
  label: string;
  onPress: () => void;
  flip?: boolean;
  disabled?: boolean;
}) => (
  <TouchableOpacity
    onPress={onPress}
    disabled={disabled}
    activeOpacity={0.6}
    accessibilityRole="button"
    accessibilityLabel={label}
    style={{ alignItems: "center", opacity: disabled ? 0.4 : 1 }}
  >
    <View className="w-12 h-12 rounded-full items-center justify-center bg-[#FFFFFF14]">
      <Icon name={icon} size={22} color="#EFE9DE" style={flip ? { transform: [{ scaleX: -1 }] } : undefined} />
    </View>
    <Text className="mt-1.5 text-[11px] text-[#AAA398]">{label}</Text>
  </TouchableOpacity>
);

export default PhotoCropper;
