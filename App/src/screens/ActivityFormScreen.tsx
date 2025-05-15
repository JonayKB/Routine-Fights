import { View, TouchableOpacity, Text, ScrollView, Alert } from "react-native";
import React, { useState } from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ActivitiesStackProps } from "../navigation/ActivitiesStackNavigation";
import { useImageContext } from "../contexts/ImageContextProvider";
import ImageStackNavigation from "../navigation/ImageStackNavigation";
import { translations } from "../../translations/translation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { Activity } from "../utils/Activity";
import { createActivity } from "../repositories/ActivityRepository";
import DropDown from "../components/DropDown";
import ChangePicture from "../components/ChangePicture";
import FormInput from "../components/FormInput";
import { uploadImage } from "../repositories/ImageRepository";

type Props = NativeStackScreenProps<ActivitiesStackProps, "ActivityForm">;

const ActivityFormScreen = (props: Props) => {
  const { language, darkmode } = useSettingsContext();
  const { uri, setUri } = useImageContext();
  const [activity, setActivity] = useState<Activity>({} as Activity);
  const [timeRate, setTimeRate] = useState("daily");

  const timeRates = [
    {
      label:
        translations[language || "en-EN"].screens.ActivityForm.timeRates.daily,
      value: "daily",
    },
    {
      label:
        translations[language || "en-EN"].screens.ActivityForm.timeRates.weekly,
      value: "weekly",
    },
    {
      label:
        translations[language || "en-EN"].screens.ActivityForm.timeRates
          .monthly,
      value: "monthly",
    },
    {
      label:
        translations[language || "en-EN"].screens.ActivityForm.timeRates.yearly,
      value: "yearly",
    },
  ];

  const addActivity = async () => {
    try {
      const imageName = await uploadImage(uri);
      console.log(imageName); // Debugging line

      const id = await createActivity(
        activity.name,
        activity.description,
        timeRate,
        activity.timesRequiered,
        imageName
      );

      if (id) {
        Alert.alert(
          translations[language || "en-EN"].screens.ActivityForm.success
        );
        setUri(null);
        props.navigation.goBack();
      } else {
        throw new Error("Failed to create activity");
      }
    } catch (error) {
      console.error(error);
      Alert.alert(translations[language || "en-EN"].screens.ActivityForm.error);
    }
  };

  return (
    <View
      className={`flex-1 bg-[#${
        darkmode ? "2C2C2C" : "CCCCCC"
      }] items-center m-10`}
    >
      <ScrollView className="bg-[#E4D8E9] rounded-lg w-10/12">
        {uri ? (
          <ChangePicture uri={uri} setUri={setUri} />
        ) : (
          <View className="w-full h-3/5">
            <ImageStackNavigation />
          </View>
        )}
        <View className="m-10">
          <FormInput
            name={activity.name}
            label={translations[language || "en-EN"].screens.ActivityForm.title}
            mode="text"
            setText={(text) => setActivity({ ...activity, name: text })}
          />
          <FormInput
            name={activity.description}
            label={
              translations[language || "en-EN"].screens.ActivityForm.description
            }
            mode="text"
            setText={(text) => setActivity({ ...activity, description: text })}
          />
          <View className="mb-5">
            <DropDown
              data={timeRates}
              value={timeRate}
              setValue={setTimeRate}
              message={
                translations[language || "en-EN"].screens.UploadForm.timeRate
              }
            />
          </View>
          <FormInput
            name={activity.timesRequiered}
            label={
              translations[language || "en-EN"].screens.ActivityForm
                .timesRequired
            }
            mode="numeric"
            setText={(text) =>
              setActivity({ ...activity, timesRequiered: text })
            }
          />
          <TouchableOpacity
            className="bg-[#E4007C] rounded-lg py-3 w-full"
            onPress={addActivity}
          >
            <Text className="text-white font-bold text-xl text-center">
              {translations[language || "en-EN"].screens.ActivityForm.post}
            </Text>
          </TouchableOpacity>
        </View>
      </ScrollView>
    </View>
  );
};

export default ActivityFormScreen;
