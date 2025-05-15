import { View, Text, Image, TouchableOpacity } from "react-native";
import React, { useEffect, useState } from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ActivitiesStackProps } from "../navigation/ActivitiesStackNavigation";
import { Activity } from "../utils/Activity";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { translations } from "../../translations/translation";
import { subscribeActivity } from "../repositories/ActivityRepository";
import ActivityDetailsBox from "../components/ActivityDetailsBox";
import { resetNavigation } from "../utils/Utils";
import Picture from "../components/Picture";

type Props = NativeStackScreenProps<ActivitiesStackProps, "ActivityDetails">;

const ActivityDetailsScreen = ({ navigation, route }: Props) => {
  const { language, darkmode } = useSettingsContext();
  const [activity, setActivity] = useState<Activity>({} as Activity);

  useEffect(() => {
    setActivity(route.params.activity);
  }, [route.params.activity]);

  return (
    <View
      className={`flex-1 bg-[#${
        darkmode ? "2C2C2C" : "CCCCCC"
      }] justify-center items-center`}
    >
      <View className="flex-1">
        <Picture image={activity.image} size={440} height={550} style="" />
      </View>
      <ActivityDetailsBox activity={activity} />
      {/** TODO: check if is subscribed or not */}
      <TouchableOpacity
        onPress={async () => {
          await subscribeActivity(activity.id);
          resetNavigation(navigation, "Streaks");
        }}
        className="bg-[#E4007C] rounded-lg py-3 m-5 w-11/12 mb-10"
      >
        <Text
          className="text-white font-bold text-2xl text-center"
          style={{ fontFamily: "InriaSans-Regular" }}
        >
          {translations[language || "en-EN"].screens.ActivityDetails.add}
        </Text>
      </TouchableOpacity>
    </View>
  );
};

export default ActivityDetailsScreen;
