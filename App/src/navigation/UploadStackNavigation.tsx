import React from "react";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import Upload from "../screens/Upload";
import UploadForm from "../screens/UploadForm";
import ImageCrop from "../screens/ImageCrop";

type Props = {};

export type UploadStackProps = {
  Upload: undefined;
  ImageCrop: { uri: string };
  UploadForm: { uri: string };
};

const UploadStackNavigation = (props: Props) => {
  const Stack = createNativeStackNavigator<UploadStackProps>();

  return (
    <Stack.Navigator id={undefined} screenOptions={{ headerShown: false }}>
      <Stack.Screen name="Upload" component={Upload} />
      <Stack.Screen
        name="ImageCrop"
        component={ImageCrop}
      />
      <Stack.Screen name="UploadForm" component={UploadForm} />
    </Stack.Navigator>
  );
};

export default UploadStackNavigation;
