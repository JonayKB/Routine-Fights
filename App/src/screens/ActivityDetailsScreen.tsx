import { View, Text, TouchableOpacity, Alert } from "react-native";
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
import ProfileNavigation from "../components/ProfileNavigation";

type Props = NativeStackScreenProps<ActivitiesStackProps, "ActivityDetails">;

const ActivityDetailsScreen = ({ navigation, route }: Props) => {
  const { language, darkmode } = useSettingsContext();
  const [activity, setActivity] = useState<Activity>({} as Activity);

  useEffect(() => {
    setActivity(route.params.activity);
  }, [route.params.activity]);

  const suscribe = async () => {
    try {
      await subscribeActivity(activity.id);
      resetNavigation(navigation, "Streaks");
    } catch (error) {
      Alert.alert("Error", error.response.data);
    }
  }

  return (
    <View className={`flex-1 ${darkmode ? "bg-[#1C1C1E]" : "bg-[#FCFCFC]"}`}>
      <ProfileNavigation
        navigation={navigation}
        message={route.params.activity?.name}
      />

      <View className="items-center justify-center border-b-8 border-[#F65261]">
        <Picture
          image={activity.image}
          size={440}
          height={550}
          style="rounded-3xl"
        />
      </View>

      <View
        className={`flex-1 items-center w-full ${
          !route.params.suscribed ? "-mt-40" : "-mt-24"
        }`}
      >
        <ActivityDetailsBox activity={activity} />

        {!route.params.suscribed && (
          <TouchableOpacity
            onPress={suscribe}
            className="bg-[#F65261] rounded-2xl py-4 px-6 mt-6 w-11/12 shadow-md shadow-black items-center"
          >
            <Text
              className="text-white font-bold text-2xl"
              style={{ fontFamily: "Roboto-Regular" }}
            >
              {translations[language || "en-US"].screens.ActivityDetails.add}
            </Text>
          </TouchableOpacity>
        )}
      </View>
    </View>
  );
};

export default ActivityDetailsScreen;
