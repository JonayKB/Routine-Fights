import React from "react";
import ProgressiveImage from "./ProgressiveImage";

type Props = {
  image: string;
  size: number;
  height?: number;
  style: string;
};

/** A stored picture, loaded progressively (blurred thumbnail → medium → sharp). */
const Picture = ({ image, size, style, height }: Props) => {
  if (!image) return null;
  return <ProgressiveImage image={image} width={size} height={height ?? size} className={style} />;
};

export default Picture;
