import { View, TouchableOpacity } from "react-native";
import React, { useEffect, useRef, useState } from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { UploadStackProps } from "../navigation/UploadStackNavigation";
import { CropView } from "react-native-image-crop-tools";
import Icon from "react-native-vector-icons/Ionicons";

type Props = NativeStackScreenProps<UploadStackProps, "ImageCrop">;

const ImageCrop = ({ navigation, route }: Props) => {
  const [imageUri, setImageUri] = useState<string | null>(null);
  const cropViewRef = useRef<CropView>(null);

  useEffect(() => {
    if (imageUri) {
      navigation.navigate("UploadForm", { uri: imageUri });
    }
  }, [imageUri]);

  return (
    <View className="flex-1 bg-black">
      <CropView
        sourceUrl={route.params.uri}
        ref={cropViewRef}
        style={{ flex: 4, margin: 20 }}
        onImageCrop={(res) => setImageUri(res.uri)}
        keepAspectRatio
        aspectRatio={{ width: 9, height: 16 }}
      />
      <View className="flex-1 flex-row justify-around items-center">
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <Icon name="close-circle" size={60} color="white" />
        </TouchableOpacity>
        <TouchableOpacity
          onPress={() => cropViewRef.current.saveImage(true, 90)}
        >
          <Icon name="checkmark-circle" size={60} color="white" />
        </TouchableOpacity>
      </View>
    </View>
  );
};

export default ImageCrop;
