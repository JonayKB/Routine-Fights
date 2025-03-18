import { View, TouchableOpacity, Text } from "react-native";
import React from "react";
import { launchCamera, launchImageLibrary } from "react-native-image-picker";
import Icon from "react-native-vector-icons/Ionicons";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ImageStackProps } from "../navigation/ImageStackNavigation";

type Props = NativeStackScreenProps<ImageStackProps, "Upload">;

const Upload = ({ navigation }: Props) => {
  return (
    <View className="flex-1 bg-[#E4D8E9] justify-center items-center rounded-t-lg">
      <TouchableOpacity
        className="flex-row justify-center items-center bg-white p-5 rounded-lg m-5"
        onPress={() => {
          launchCamera({ mediaType: "photo" }, ({ assets }) => {
            if (assets) {
              navigation.navigate("ImageCrop", { uri: assets[0]?.uri });
            }
          });
        }}
      >
        <Icon name="camera" size={50} color="#4B0082" />
        <Text className="text-black font-bold text-xl ml-3">
          Take from camera
        </Text>
      </TouchableOpacity>

      <TouchableOpacity
        className="flex-row justify-center items-center bg-white p-5 rounded-lg m-5 w-8/12"
        onPress={() => {
          launchImageLibrary({ mediaType: "photo" }, ({ assets }) => {
            if (assets) {
              navigation.navigate("ImageCrop", { uri: assets[0]?.uri });
            }
          });
        }}
      >
        <Icon name="image" size={50} color="#4B0082" />
        <Text className="text-black font-bold text-xl ml-3">
          Select from library
        </Text>
      </TouchableOpacity>
    </View>
  );
};

export default Upload;
