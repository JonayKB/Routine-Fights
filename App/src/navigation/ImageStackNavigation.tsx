import React from "react";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import Upload from "../components/Upload";
import ImageCrop from "../components/ImageCrop";

type Props = {};

export type ImageStackProps = {
  Upload: undefined;
  ImageCrop: { uri: string };
};

const ImageStackNavigation = (props: Props) => {
  const Stack = createNativeStackNavigator<ImageStackProps>();

  return (
    <Stack.Navigator id={undefined} screenOptions={{ headerShown: false }}>
      <Stack.Screen name="Upload" component={Upload} />
      <Stack.Screen name="ImageCrop" component={ImageCrop} />
    </Stack.Navigator>
  );
};

export default ImageStackNavigation;
