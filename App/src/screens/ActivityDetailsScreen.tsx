import { View, Text, TouchableOpacity } from "react-native";
import React, { useEffect, useState } from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ActivitiesStackProps } from "../navigation/ActivitiesStackNavigation";
import { Activity } from "../utils/Activity";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { translations } from "../../translations/translation";
import { subscribeActivity } from "../repositories/ActivityRepository";
import ActivityDetailsBox from "../components/ActivityDetailsBox";
import { bgColor, cardBgColor, resetNavigation } from "../utils/Utils";
import Picture from "../components/Picture";
import ProfileNavigation from "../components/ProfileNavigation";

type Props = NativeStackScreenProps<ActivitiesStackProps, "ActivityDetails">;

const ActivityDetailsScreen = ({ navigation, route }: Props) => {
  const { language, darkmode } = useSettingsContext();
  const [activity, setActivity] = useState<Activity>({} as Activity);

  useEffect(() => {
    setActivity(route.params.activity);
  }, [route.params.activity]);

  return (
    <View className={`flex-1 ${cardBgColor(darkmode)}`}>
      <ProfileNavigation
        navigation={navigation}
        message={route.params.activity?.name}
      />
      <View className="flex-1">
        <Picture image={activity.image} size={440} height={550} style="" />
      </View>
      <View className="flex-1 justify-center items-center">
        <ActivityDetailsBox activity={activity} />
        {!route.params.suscribed && (
          <TouchableOpacity
            onPress={async () => {
              await subscribeActivity(activity.id);
              resetNavigation(navigation, "Streaks");
            }}
            className="bg-[#E4007C] rounded-lg py-3 m-5 w-11/12 -mt-10"
          >
            <Text
              className="text-white font-bold text-2xl text-center"
              style={{ fontFamily: "Roboto-Regular" }}
            >
              {translations[language || "en-EN"].screens.ActivityDetails.add}
            </Text>
          </TouchableOpacity>
        )}
      </View>
    </View>
  );
};

export default ActivityDetailsScreen;
