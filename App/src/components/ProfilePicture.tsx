import { Image } from "react-native";
import React from "react";
import { uri } from "../utils/Utils";
import { useTokenContext } from "../contexts/TokenContextProvider";

type Props = {
  image: string;
  size: number;
};

const ProfilePicture = ({ image, size }: Props) => {
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
      height={size}
      className="rounded-full border-2 border-[#4B0082] filter invert"
    />
  );
};

export default ProfilePicture;
