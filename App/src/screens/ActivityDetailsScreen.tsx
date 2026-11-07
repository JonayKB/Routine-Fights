import {
  View,
  Text,
  TouchableOpacity,
  ScrollView,
  Dimensions,
} from "react-native";
import React, { useEffect, useState } from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ActivitiesStackProps } from "../navigation/ActivitiesStackNavigation";
import { Activity } from "../utils/Activity";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { translations } from "../../translations/translation";
import { subscribeActivity } from "../repositories/ActivityRepository";
import ActivityDetailsBox from "../components/ActivityDetailsBox";
import { bgColor, resetNavigation } from "../utils/Utils";
import Picture from "../components/Picture";
import ProfileNavigation from "../components/ProfileNavigation";
import StreakCalendar from "../components/StreakCalendar";
import { errorMessage } from "../utils/graphql";
import { useToast } from "../contexts/FeedbackContextProvider";

type Props = NativeStackScreenProps<ActivitiesStackProps, "ActivityDetails">;

const ActivityDetailsScreen = ({ navigation, route }: Props) => {
  const { language, darkmode } = useSettingsContext();
  const toast = useToast();
  const [activity, setActivity] = useState<Activity>({} as Activity);

  useEffect(() => {
    setActivity(route.params.activity);
  }, [route.params.activity]);

  const suscribe = async () => {
    try {
      await subscribeActivity(activity.id);
      resetNavigation(navigation, "Streaks");
    } catch (error) {
      toast.error(errorMessage(error));
    }
  }

  const width = Dimensions.get("window").width;

  return (
    <View className={`flex-1 ${bgColor(darkmode)}`}>
      <ProfileNavigation
        navigation={navigation}
        message={route.params.activity?.name}
      />

      <ScrollView contentContainerStyle={{ paddingBottom: 24 }}>
        <Picture
          image={activity.image}
          size={width}
          height={Math.round(width * 0.75)}
          style=""
        />

        <ActivityDetailsBox activity={activity} />

        {route.params.suscribed && !!activity?.id && (
          <StreakCalendar
            activityId={activity.id}
            streak={(route.params.activity as any)?.streak ?? 0}
          />
        )}

        {!route.params.suscribed && (
          <TouchableOpacity
            onPress={suscribe}
            activeOpacity={0.85}
            className="h-12 mx-5 mt-4 rounded-xl bg-[#C2410C] items-center justify-center"
          >
            <Text className="text-white font-semibold text-base">
              {translations[language || "en-EN"].screens.ActivityDetails.add}
            </Text>
          </TouchableOpacity>
        )}
      </ScrollView>
    </View>
  );
};

export default ActivityDetailsScreen;
