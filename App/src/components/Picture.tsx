import { Image } from "react-native";
import React from "react";
import { uri } from "../utils/Utils";
import { useTokenContext } from "../contexts/TokenContextProvider";

type Props = {
  image: string;
  size: number;
  height?: number;
  style: string;
};

const Picture = ({ image, size, style, height }: Props) => {
  const { token } = useTokenContext();  
  
  return (
    <Image
      source={{
        uri: uri + "/images/" + image,
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }}
      width={size}
      height={height ?? size}
      className={style}
    />
  );
};

export default Picture;
