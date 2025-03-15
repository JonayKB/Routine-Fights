import { View, Image } from "react-native";
import React, { useEffect, useState } from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { UploadStackProps } from "../navigation/UploadStackNavigation";

type Props = NativeStackScreenProps<UploadStackProps, "UploadForm">;

const UploadForm = ({ navigation, route }: Props) => {
  const [image, setImage] = useState<string>("");

  useEffect(() => {
    setImage(route.params.image);
  }, [route.params.image]);

  return (
    <View>
      <Image source={{ uri: image }} style={{ width: 200, height: 200 }} />
    </View>
  );
};

export default UploadForm;
