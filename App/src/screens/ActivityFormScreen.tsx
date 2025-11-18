import { View, TouchableOpacity, Text, ScrollView, Alert } from "react-native";
import React, { useEffect, useState } from "react";
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
import { bgColor, cardBgColor } from "../utils/Utils";

type Props = NativeStackScreenProps<ActivitiesStackProps, "ActivityForm">;

const ActivityFormScreen = (props: Props) => {
  const { language, darkmode } = useSettingsContext();
  const { uri, setUri, setWidth, setHeight } = useImageContext();
  const [activity, setActivity] = useState<Activity>({} as Activity);
  const [timeRate, setTimeRate] = useState("daily");

  useEffect(() => {
    setWidth(9);
    setHeight(16);
  }, []);

  const timeRates = [
    {
      label:
        translations[language || "en-US"].screens.ActivityForm.timeRates.daily,
      value: "daily",
    },
    {
      label:
        translations[language || "en-US"].screens.ActivityForm.timeRates.weekly,
      value: "weekly",
    },
    {
      label:
        translations[language || "en-US"].screens.ActivityForm.timeRates
          .monthly,
      value: "monthly",
    },
    {
      label:
        translations[language || "en-US"].screens.ActivityForm.timeRates.yearly,
      value: "yearly",
    },
  ];

  const addActivity = async () => {
    try {
      const imageName = await uploadImage(uri);

      await createActivity(
        activity.name,
        activity.description,
        timeRate,
        activity.timesRequiered,
        imageName
      );

      Alert.alert(
        translations[language || "en-US"].screens.ActivityForm.success
      );
      setUri(null);
      props.navigation.goBack();
    } catch (error) {
      Alert.alert("Error", error.response.data);
    }
  };

  return (
    <View className={`flex-1 ${bgColor(darkmode)} items-center justify-center`}>
      <View className={`rounded-lg w-10/12 ${cardBgColor(darkmode)}`}>
        <ScrollView
          contentContainerStyle={{ paddingVertical: 20, paddingHorizontal: 16 }}
        >
          {uri ? (
            <ChangePicture uri={uri} setUri={setUri} />
          ) : (
            <View className="w-full h-72 mb-6">
              <ImageStackNavigation />
            </View>
          )}

          <FormInput
            label={translations[language || "en-US"].screens.ActivityForm.title}
            name={activity.name}
            setText={(text) => setActivity({ ...activity, name: text })}
            mode="text"
          />

          <FormInput
            label={
              translations[language || "en-US"].screens.ActivityForm.description
            }
            name={activity.description}
            setText={(text) => setActivity({ ...activity, description: text })}
            mode="text"
          />

          <View className="mb-5">
            <DropDown
              data={timeRates}
              value={timeRate}
              setValue={setTimeRate}
              message={
                translations[language || "en-US"].screens.UploadForm.timeRate
              }
            />
          </View>

          <FormInput
            label={
              translations[language || "en-US"].screens.ActivityForm
                .timesRequired
            }
            name={activity.timesRequiered}
            setText={(text) =>
              setActivity({ ...activity, timesRequiered: text })
            }
            mode="numeric"
          />

          <TouchableOpacity
            className="bg-[#F65261] rounded-lg py-3 w-full mt-4"
            onPress={addActivity}
          >
            <Text className="text-white font-bold text-xl text-center">
              {translations[language || "en-US"].screens.ActivityForm.post}
            </Text>
          </TouchableOpacity>
        </ScrollView>
      </View>
    </View>
  );
};

export default ActivityFormScreen;
