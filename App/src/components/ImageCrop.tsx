import { View, TouchableOpacity, Dimensions } from "react-native";
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
  const { width, height } = Dimensions.get("window");

  return (
    <View className="flex-1 bg-black">
      <CropView
        sourceUrl={route.params.uri}
        ref={cropViewRef}
        style={{ flex: 4, margin: 20 }}
        onImageCrop={(res) => {
          setUri(res.uri);
          navigation.goBack();
        }}
        keepAspectRatio
        aspectRatio={{ width: width, height: height }}
      />
      <View className="flex-row justify-around items-center p-4">
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <Icon name="close" size={32} color="white" />
        </TouchableOpacity>
        <TouchableOpacity
          onPress={() => cropViewRef.current.saveImage(true, 90)}
        >
          <Icon name="checkmark" size={32} color="#F65261" />
        </TouchableOpacity>
      </View>
    </View>
  );
};

export default ImageCrop;
