import {
  View,
  TouchableOpacity,
  Text,
  TextInput,
  ScrollView,
  Alert,
} from "react-native";
import React, { useState } from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ActivitiesStackProps } from "../navigation/ActivitiesStackNavigation";
import { useImageContext } from "../contexts/ImageContextProvider";
import ImageStackNavigation from "../navigation/ImageStackNavigation";
import { translations } from "../../translations/translation";
import { useLanguageContext } from "../contexts/SettingsContextProvider";
import { Activity } from "../utils/Activity";
import { createActivity } from "../repositories/ActivityRepository";
import DropDown from "../components/DropDown";
import ChangePicture from "../components/ChangePicture";
import FormInput from "../components/FormInput";

type Props = NativeStackScreenProps<ActivitiesStackProps, "ActivityForm">;

const ActivityForm = (props: Props) => {
  const { language } = useLanguageContext();
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
        translations[language || "en-EN"].screens.ActivityForm.timeRates.monthly,
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
      const id = await createActivity(
        activity.name,
        activity.description,
        activity.timeRate,
        activity.timesRequiered,
        ""
      );

      if (id) {
        Alert.alert(
          translations[language || "en-EN"].screens.ActivityForm.success
        );
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
    <View className="flex-1 items-center my-10">
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
            setText={(text) => setActivity({ ...activity, name: text })}
          />
          <TextInput
            placeholder={
              translations[language || "en-EN"].screens.ActivityForm.description
            }
            placeholderTextColor="#4B0082"
            className="border-[#4B0082] border-2 rounded-lg bg-[#F8F7FE] text-lg mb-5 pl-3 text-black"
            onChangeText={(text) =>
              setActivity({ ...activity, description: text })
            }
            value={activity.description}
          />
          <View className="m-10">
            <DropDown
              data={timeRates}
              value={timeRate}
              setValue={setTimeRate}
              message={
                translations[language || "en-EN"].screens.UploadForm.timeRate
              }
            />
          </View>
          <TextInput
            placeholder={
              translations[language || "en-EN"].screens.ActivityForm
                .timesRequired
            }
            placeholderTextColor="#4B0082"
            inputMode="numeric"
            className="border-[#4B0082] border-2 rounded-lg bg-[#F8F7FE] text-lg mb-5 pl-3 text-black"
            onChangeText={(text) =>
              setActivity({ ...activity, timesRequiered: text })
            }
            value={activity.timesRequiered}
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

export default ActivityForm;
