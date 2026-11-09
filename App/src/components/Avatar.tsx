import { Text, View } from "react-native";
import React, { useEffect, useState } from "react";
import { hasImage as isStoredImage } from "../utils/images";
import ProgressiveImage from "./ProgressiveImage";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { fonts } from "../styles/theme";

type Props = {
  image?: string | null;
  name?: string | null;
  size: number;
  /** Draw a ring in the given colour, e.g. for the active tab. */
  ring?: string;
};

/** A user picture that falls back to initials when there is no image or it fails to load. */
const Avatar = ({ image, name, size, ring }: Props) => {
  const { darkmode } = useSettingsContext();
  const [failed, setFailed] = useState(false);

  useEffect(() => setFailed(false), [image]);

  const hasImage = isStoredImage(image) && !failed;
  const initials = (name ?? "")
    .trim()
    .split(/[\s._@-]+/)
    .filter(Boolean)
    .slice(0, 2)
    .map((part) => part[0]?.toUpperCase())
    .join("");

  return (
    <View
      style={{
        width: size,
        height: size,
        borderRadius: size / 2,
        borderWidth: ring ? 2 : 1,
        borderColor: ring ?? (darkmode ? "#EFE9DE1A" : "#1F1B161A"),
        backgroundColor: darkmode ? "#262421" : "#EFE9DF",
        overflow: "hidden",
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      {hasImage ? (
        <ProgressiveImage image={image} width={size} height={size} onError={() => setFailed(true)} />
      ) : (
        <Text
          style={{
            fontFamily: fonts.displayMedium,
            fontSize: Math.max(10, Math.round(size * 0.38)),
            color: darkmode ? "#AAA398" : "#5E554A",
          }}
        >
          {initials || "·"}
        </Text>
      )}
    </View>
  );
};

export default Avatar;
