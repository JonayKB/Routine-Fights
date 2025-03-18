import {
  View,
  Image,
  TouchableOpacity,
  Text,
  TextInput,
  ScrollView,
} from "react-native";
import React from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ActivitiesStackProps } from "../navigation/ActivitiesStackNavigation";
import { useImageContext } from "../contexts/ImageContextProvider";
import ImageStackNavigation from "../navigation/ImageStackNavigation";

type Props = NativeStackScreenProps<ActivitiesStackProps, "ActivityForm">;

const ActivityForm = (props: Props) => {
  const { uri, setUri } = useImageContext();

  return (
    <View className="flex-1 items-center my-10">
      <ScrollView className="bg-[#E4D8E9] rounded-lg w-10/12">
        {uri ? (
          <View className="items-center">
            <View className="rounded-lg w-full bg-black items-center">
              <Image
                className="justify-center my-5"
                source={{ uri: `file://${uri}` }}
                style={{ width: 140, height: 250 }}
              />
              <TouchableOpacity
                onPress={() => setUri(null)}
                className="border-white border-2 rounded-lg mb-5"
              >
                <Text className="text-white font-bold text-xl text-center px-6 py-2">
                  Change picture
                </Text>
              </TouchableOpacity>
            </View>
          </View>
        ) : (
          <View className="w-full h-3/5">
            <ImageStackNavigation />
          </View>
        )}
        <View className="m-10">
          <TextInput
            placeholder="Title"
            placeholderTextColor="#4B0082"
            className="border-[#4B0082] border-2 rounded-lg bg-[#F8F7FE] text-lg mb-5 pl-3 text-black"
          />
          <TextInput
            placeholder="Description"
            placeholderTextColor="#4B0082"
            className="border-[#4B0082] border-2 rounded-lg bg-[#F8F7FE] text-lg mb-5 pl-3 text-black"
          />
          <TextInput
            placeholder="timeRate"
            placeholderTextColor="#4B0082"
            className="border-[#4B0082] border-2 rounded-lg bg-[#F8F7FE] text-lg mb-5 pl-3 text-black"
          />
          <TextInput
            placeholder="timesRequired"
            placeholderTextColor="#4B0082"
            inputMode="numeric"
            className="border-[#4B0082] border-2 rounded-lg bg-[#F8F7FE] text-lg mb-5 pl-3 text-black"
          />
          <TouchableOpacity className="bg-[#E4007C] rounded-lg py-3 w-full">
            <Text className="text-white font-bold text-xl text-center">
              Post
            </Text>
          </TouchableOpacity>
        </View>
      </ScrollView>
    </View>
  );
};

export default ActivityForm;
