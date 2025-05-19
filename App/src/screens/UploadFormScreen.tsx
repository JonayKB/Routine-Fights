import { Image, View, TouchableOpacity, Text, Alert } from "react-native";
import React, { useEffect, useState } from "react";
import { useImageContext } from "../contexts/ImageContextProvider";
import DropDown from "../components/DropDown";
import { translations } from "../../translations/translation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { getSubscribedActivities } from "../repositories/ActivityRepository";
import { ActivityWithStreak, Categories } from "../utils/Activity";
import { uploadPost } from "../repositories/PostRepository";
import { uploadImage } from "../repositories/ImageRepository";
import ImageStackNavigation from "../navigation/ImageStackNavigation";
import { bgColor, cardBgColor, textColor } from "../utils/Utils";

type Props = {};

const UploadFormScreen = (props: Props) => {
  const { uri, setUri, setWidth, setHeight } = useImageContext();
  const [categories, setCategories] = useState<Categories[]>([]);
  const [activityName, setActivityName] = useState<string>(null);
  const [activity, setActivity] = useState<Categories>({} as Categories);
  const { language, darkmode } = useSettingsContext();

  useEffect(() => {
    fetchCategories();
    setWidth(9);
    setHeight(16);
  }, []);

  useEffect(() => {
    setActivity(
      categories.filter((c: Categories) => c.value === activityName)[0]
    );
  }, [activityName]);

  const fetchCategories = async () => {
    try {
      const response = await getSubscribedActivities();
      const categories: Categories[] = response.map(
        (activity: ActivityWithStreak) => ({
          label: activity.name,
          value: activity.id,
          timesRemaining: activity.timesRemaining,
          timesRequiered: activity.timesRequiered,
        })
      );
      setCategories(categories);
    } catch (error) {
      Alert.alert("Error", error.response.data);
    }
  };

  const createPost = async () => {
    try {
      const imageName = await uploadImage(uri);
      const response = await uploadPost(
        categories.filter((c: Categories) => c.value === activityName)[0]
          ?.value,
        imageName
      );
      if (response) {
        Alert.alert("Post created");
        setUri(null);
        setActivityName(null);
      }
    } catch (error) {
      Alert.alert("Error", error.response.data);
    }
  };

  return (
    <View
      className={`flex-1 ${bgColor(darkmode)} justify-center items-center px-4`}
    >
      <View
        className={`w-full ${cardBgColor(
          darkmode
        )} rounded-2xl overflow-hidden`}
      >
        {uri ? (
          <View className="items-center p-6 bg-black">
            <Image
              source={{ uri: `file://${uri}` }}
              style={{ width: 160, height: 280, borderRadius: 12 }}
            />
            <TouchableOpacity
              onPress={() => setUri(null)}
              className="mt-4 border-2 border-white rounded-2xl px-8 py-3"
            >
              <Text className="text-white font-bold text-lg text-center">
                {
                  translations[language || "en-EN"].screens.UploadForm
                    .changePicture
                }
              </Text>
            </TouchableOpacity>
          </View>
        ) : (
          <View className="w-full h-3/5">
            <ImageStackNavigation />
          </View>
        )}

        <View className="px-6 pt-6">
          <DropDown
            data={categories}
            value={activityName}
            setValue={setActivityName}
            message={
              translations[language || "en-EN"].screens.UploadForm
                .selectactivity
            }
            onFocus={fetchCategories}
          />
        </View>

        <TouchableOpacity
          onPress={createPost}
          disabled={!activityName || !uri || activity?.timesRemaining < 1}
          className={`m-6 rounded-2xl py-4 ${
            !activityName || !uri || activity?.timesRemaining < 1
              ? "bg-[#CCCCCC]"
              : "bg-[#F65261]"
          }`}
        >
          <Text className="text-white font-bold text-xl text-center">
            {translations[language || "en-EN"].screens.UploadForm.postButton}
          </Text>
        </TouchableOpacity>

        <Text className={`pb-6 text-center ${textColor(darkmode)}`}>
          {translations[language || "en-EN"].screens.UploadForm.timesRemaining}
          {": "}
          {`${activity?.timesRemaining || 0}`}
        </Text>
      </View>
    </View>
  );
};

export default UploadFormScreen;
