import React from "react";
import { Image, View } from "react-native";
import { PickedImage } from "../utils/images";

type Props = {
  picked: PickedImage;
  /** Box to fit the cropped photo in; it keeps its aspect ratio. */
  maxWidth: number;
  maxHeight?: number;
  /** Fill the whole box, cutting the overflow, instead of fitting inside it. */
  cover?: boolean;
  borderRadius?: number;
};

/**
 * Shows a picked photo exactly as it will look once the API applies the crop,
 * without creating a new file on the device.
 */
const CroppedImage = ({ picked, maxWidth, maxHeight, cover, borderRadius }: Props) => {
  const { crop, aspect } = picked;
  const boxH = maxHeight ?? maxWidth / aspect;

  // Size of the cropped area on screen.
  let w = maxWidth;
  let h = w / aspect;
  if (cover ? h < boxH : h > boxH) {
    h = boxH;
    w = h * aspect;
  }

  // Whole rotated photo at that scale, and its position.
  const dw = w / crop.width;
  const dh = h / crop.height;
  const turned = crop.rotation % 180 !== 0;
  const iw = turned ? dh : dw;
  const ih = turned ? dw : dh;
  // In cover mode the cropped area is centred in (and overflows) the box.
  const offsetX = cover ? (maxWidth - w) / 2 : 0;
  const offsetY = cover ? (boxH - h) / 2 : 0;
  const centerX = offsetX - crop.x * dw + dw / 2;
  const centerY = offsetY - crop.y * dh + dh / 2;

  return (
    <View
      style={{
        width: cover ? maxWidth : w,
        height: cover ? boxH : h,
        overflow: "hidden",
        borderRadius,
        alignSelf: "center",
      }}
    >
      <Image
        source={{ uri: picked.uri }}
        style={{
          position: "absolute",
          width: iw,
          height: ih,
          left: centerX - iw / 2,
          top: centerY - ih / 2,
          transform: [{ rotate: `${crop.rotation}deg` }],
        }}
      />
    </View>
  );
};

export default CroppedImage;
