import { View, Image } from "react-native";
import React from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ActivitiesStackProps } from "../navigation/ActivitiesStackNavigation";
import { useImageContext } from "../contexts/ImageContextProvider";
import ImageStackNavigation from "../navigation/ImageStackNavigation";

type Props = NativeStackScreenProps<ActivitiesStackProps, "ActivityForm">;

const ActivityForm = (props: Props) => {
  const { uri, setUri } = useImageContext();

  return (
    <View className="flex-1">
      {uri ? (
        <Image
          className="justify-center"
          source={{ uri: `file://${uri}` }}
          style={{ width: 281, height: 500 }}
        />
      ) : (
        <ImageStackNavigation />
      )}
    </View>
  );
};

export default ActivityForm;
