import { View, TouchableOpacity } from "react-native";
import React, { useRef } from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { CropView } from "react-native-image-crop-tools";
import Icon from "react-native-vector-icons/Ionicons";
import { ImageStackProps } from "../navigation/ImageStackNavigation";
import { useImageContext } from "../contexts/ImageContextProvider";

type Props = NativeStackScreenProps<ImageStackProps, "ImageCrop">;

const ImageCrop = ({ navigation, route }: Props) => {
  const { setUri } = useImageContext();
  const cropViewRef = useRef<CropView>(null);

  return (
    <View className="flex-1 bg-black rounded-t-lg">
      <CropView
        sourceUrl={route.params.uri}
        ref={cropViewRef}
        style={{ flex: 4, margin: 20 }}
        onImageCrop={(res) => {
          setUri(res.uri);
          navigation.goBack();
        }}
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
