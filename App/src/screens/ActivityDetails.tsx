import { View, Text, Image, TouchableOpacity } from "react-native";
import React, { useEffect, useState } from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ActivitiesStackProps } from "../navigation/ActivitiesStackNavigation";
import { Activity } from "../utils/Activity";

type Props = NativeStackScreenProps<ActivitiesStackProps, "ActivityDetails">;

const ActivityDetails = ({ navigation, route }: Props) => {
  const [activity, setActivity] = useState<Activity>({} as Activity);

  useEffect(() => {
    setActivity(route.params.activity);
  }, [route.params.activity]);

  const addActivity = () => {};

  return (
    <View className="flex-1 bg-[#E4DCE9] justify-center items-center">
      <View className="flex-1">
        <Image
          source={{
            uri: activity.image,
          }}
          width={440}
          height={550}
        />
      </View>
      <View className="bg-[#E4D8E9] rounded-2xl w-80 h-72 m-12 border-2 border-[#4B0082] justify-evenly items-center">
        <Text className="text-[#4B0082] text-4xl">{activity.name}</Text>
        <Text className="text-[#4B0082] text-lg mb-10">
          {activity.description}
        </Text>
        <Text className="text-[#4B0082] text-xl">
          Num of times: {activity.timesRequired}
        </Text>
        <Text className="text-[#4B0082] text-xl">
          Frequency: {activity.timeRate}
        </Text>
      </View>
      <TouchableOpacity
        onPress={addActivity}
        className="bg-[#E4007C] rounded-lg py-3 m-5 w-11/12 mb-10"
      >
        <Text
          className="text-white font-bold text-2xl text-center"
          style={{ fontFamily: "InriaSans-Regular" }}
        >
          Add
        </Text>
      </TouchableOpacity>
    </View>
  );
};

export default ActivityDetails;
