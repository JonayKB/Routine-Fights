import { Image, View } from "react-native";
import React from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { UploadStackProps } from "../navigation/UploadStackNavigation";

type Props = NativeStackScreenProps<UploadStackProps, "UploadForm">;

const UploadForm = ({ navigation, route }: Props) => {
  return (
    <View>
      <Image
        className="justify-center"
        source={{ uri: `file://${route.params.uri}` }}
        style={{ width: 281, height: 500 }}
      />
    </View>
  );
};

export default UploadForm;
