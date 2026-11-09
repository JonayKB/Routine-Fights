import { PixelRatio } from "react-native";
import { uri } from "./Utils";

/** Renditions the API stores for every picture (longest side, in pixels). */
export type ImageSize = "tiny" | "small" | "medium" | "large";

export const IMAGE_SIZES: { name: Exclude<ImageSize, "tiny">; maxSide: number }[] = [
  { name: "small", maxSide: 320 },
  { name: "medium", maxSide: 720 },
  { name: "large", maxSide: 1440 },
];

/** Same limit the API enforces. */
export const MAX_UPLOAD_BYTES = 10 * 1024 * 1024;
/** Photos are downscaled on the device before uploading. */
export const PICKER_MAX_SIDE = 2560;

export const imageUrl = (name: string, size?: ImageSize) =>
  `${uri}/images/${encodeURIComponent(name)}${size ? `?size=${size}` : ""}`;

export const hasImage = (name?: string | null) => !!name && name !== "null.jpg" && name !== "null";

/**
 * Picks the renditions to load for a box of the given size (in dp): the
 * smallest one that is sharp on this screen, plus the one below it as a
 * quicker intermediate step.
 */
export const renditionsFor = (width: number, height: number) => {
  const needed = PixelRatio.getPixelSizeForLayoutSize(Math.max(width, height));
  let index = IMAGE_SIZES.findIndex((s) => s.maxSide >= needed);
  if (index === -1) index = IMAGE_SIZES.length - 1;
  return {
    final: IMAGE_SIZES[index].name,
    intermediate: index > 0 ? IMAGE_SIZES[index - 1].name : null,
  };
};

/** Crop chosen in the editor, as fractions of the upright, rotated photo. */
export type Crop = {
  x: number;
  y: number;
  width: number;
  height: number;
  /** Clockwise, 0 | 90 | 180 | 270. */
  rotation: number;
};

/** A photo picked on the device, not uploaded yet. */
export type PickedImage = {
  uri: string;
  type: string;
  fileName: string;
  /** Size of the photo as displayed (EXIF orientation applied), before rotation. */
  width: number;
  height: number;
  crop: Crop;
  /** width / height of the cropped result. */
  aspect: number;
};

export const normalizeRotation = (deg: number) => ((Math.round(deg / 90) * 90) % 360 + 360) % 360;
