import { View, Text, TouchableOpacity } from "react-native";
import React from "react";
import { launchCamera, launchImageLibrary } from "react-native-image-picker";
import Icon from "react-native-vector-icons/Ionicons";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { UploadStackProps } from "../navigation/UploadStackNavigation";

type Props = NativeStackScreenProps<UploadStackProps, "Upload">;

const Upload = ({ navigation, route }: Props) => {
  return (
    <View className="justify-center items-center">
      <TouchableOpacity
        className="flex-row justify-center items-center bg-[#F1FEFC] p-5 rounded-lg m-5"
        onPress={() => {
          launchCamera({ mediaType: "photo" }, ({ assets }) => {
            if (assets) {
              navigation.navigate("UploadForm", { image: assets[0]?.uri });
            }
          });
        }}
      >
        <Icon name="camera" size={30} color="#4B0082" />
        <Text className="text-black text-2xl">Take a photo</Text>
      </TouchableOpacity>
      <TouchableOpacity
        className="flex-row justify-center items-center bg-[#F1FEFC] p-5 rounded-lg m-5"
        onPress={() => {
          launchImageLibrary({ mediaType: "photo" }, ({ assets }) => {
            if (assets) {
              navigation.navigate("UploadForm", { image: assets[0]?.uri });
            }
          });
        }}
      >
        <Icon name="image" size={30} color="#4B0082" />
        <Text className="text-black text-2xl">Select from library</Text>
      </TouchableOpacity>
    </View>
  );
};

export default Upload;
