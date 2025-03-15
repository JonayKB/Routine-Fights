import React from "react";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import Upload from "../screens/Upload";
import UploadForm from "../screens/UploadForm";

type Props = {};

export type UploadStackProps = {
  Upload: undefined;
  UploadForm: { image: string };
};

const UploadStackNavigation = (props: Props) => {
  const Stack = createNativeStackNavigator<UploadStackProps>();

  return (
    <Stack.Navigator id={undefined} screenOptions={{ headerShown: false }}>
      <Stack.Screen name="Upload" component={Upload} />
      <Stack.Screen name="UploadForm" component={UploadForm} />
    </Stack.Navigator>
  );
};

export default UploadStackNavigation;
