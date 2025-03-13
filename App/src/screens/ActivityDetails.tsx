import { View, Text, Image } from "react-native";
import React, { useEffect, useState } from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ActivitiesStackProps } from "../navigation/ActivitiesStackNavigation";
import { Activity } from "../utils/Category";

type Props = NativeStackScreenProps<ActivitiesStackProps, "ActivityDetails">;

const ActivityDetails = ({ navigation, route }: Props) => {
  const [activity, setActivity] = useState<Activity>({} as Activity);

  useEffect(() => {
    setActivity(route.params.activity);
  }, [route.params.activity]);

  return (
    <View>
      <Image
        source={{
          uri: activity.image,
        }}
        style={{ width: 440, height: 668 }}
      />
      <Text>{activity.name}</Text>
      <Text>{activity.category}</Text>
      <Text>{activity.description}</Text>
      <Text>{activity.timeRate}</Text>
      <Text>{activity.timesRequired}</Text>
    </View>
  );
};

export default ActivityDetails;
