import React, { useEffect, useMemo, useRef, useState } from "react";
import { Animated, ImageResizeMode, Platform, StyleProp, View, ViewStyle } from "react-native";
import { useTokenContext } from "../contexts/TokenContextProvider";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { imageUrl, renditionsFor } from "../utils/images";

type Props = {
  /** Stored image name, as returned by the API. */
  image: string;
  width: number;
  height: number;
  className?: string;
  style?: StyleProp<ViewStyle>;
  resizeMode?: ImageResizeMode;
  /** Called when the picture can't be loaded at all. */
  onError?: () => void;
};

/** URLs already shown in this session: they come from cache, so skip the placeholder. */
const loaded = new Set<string>();

const BLUR = Platform.OS === "ios" ? 8 : 2;
const FADE_MS = 220;

/**
 * Loads a picture progressively: a ~1 KB blurred thumbnail appears almost
 * instantly, then a medium rendition fades in, then the one that is sharp at
 * this size on this screen. Smaller boxes (avatars, badges) skip straight
 * from the thumbnail to the final rendition.
 */
const ProgressiveImage = ({ image, width, height, className, style, resizeMode = "cover", onError }: Props) => {
  const { token } = useTokenContext();
  const { darkmode } = useSettingsContext();

  const { final, intermediate } = renditionsFor(width, height);
  const urls = useMemo(
    () => ({
      tiny: imageUrl(image, "tiny"),
      intermediate: intermediate ? imageUrl(image, intermediate) : null,
      final: imageUrl(image, final),
    }),
    [image, intermediate, final]
  );
  const cached = loaded.has(urls.final);

  const [finalDone, setFinalDone] = useState(cached);
  const [intermediateDone, setIntermediateDone] = useState(false);
  const midOpacity = useRef(new Animated.Value(0)).current;
  const finalOpacity = useRef(new Animated.Value(cached ? 1 : 0)).current;

  useEffect(() => {
    const isCached = loaded.has(urls.final);
    setFinalDone(isCached);
    setIntermediateDone(false);
    finalOpacity.setValue(isCached ? 1 : 0);
    midOpacity.setValue(0);
  }, [urls, finalOpacity, midOpacity]);

  const fadeIn = (value: Animated.Value, done: () => void) =>
    Animated.timing(value, { toValue: 1, duration: FADE_MS, useNativeDriver: true }).start(done);

  const headers = token ? { Authorization: `Bearer ${token}` } : undefined;
  const source = (url: string) => ({ uri: url, headers, cache: "force-cache" as const });
  const fill = { position: "absolute" as const, top: 0, left: 0, width: "100%" as const, height: "100%" as const };

  return (
    <View
      className={className}
      style={[
        { width, height, overflow: "hidden", backgroundColor: darkmode ? "#262421" : "#EFE9DF" },
        style,
      ]}
    >
      {!finalDone && (
        <Animated.Image
          source={source(urls.tiny)}
          blurRadius={BLUR}
          resizeMode={resizeMode}
          fadeDuration={0}
          style={fill}
        />
      )}
      {!finalDone && urls.intermediate && (
        <Animated.Image
          source={source(urls.intermediate)}
          resizeMode={resizeMode}
          fadeDuration={0}
          style={[fill, { opacity: midOpacity }]}
          onLoad={() => !intermediateDone && fadeIn(midOpacity, () => setIntermediateDone(true))}
        />
      )}
      <Animated.Image
        source={source(urls.final)}
        resizeMode={resizeMode}
        fadeDuration={0}
        style={[fill, { opacity: finalOpacity }]}
        onLoad={() => {
          if (loaded.has(urls.final) && finalDone) return;
          loaded.add(urls.final);
          fadeIn(finalOpacity, () => setFinalDone(true));
        }}
        onError={onError}
      />
    </View>
  );
};

export default ProgressiveImage;
