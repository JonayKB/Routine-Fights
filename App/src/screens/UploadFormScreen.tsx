import { Image, View, TouchableOpacity, Text, Alert } from "react-native";
import React, { useEffect, useState } from "react";
import { useImageContext } from "../contexts/ImageContextProvider";
import ImageStackNavigation from "../navigation/ImageStackNavigation";
import DropDown from "../components/DropDown";
import { translations } from "../../translations/translation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { getSubscribedActivities } from "../repositories/ActivityRepository";
import { ActivityWithStreak } from "../utils/Activity";
import { uploadPost } from "../repositories/PostRepository";
import { uploadImage } from "../repositories/ImageRepository";

type Props = {};

type Categories = {
  label: string;
  value: string;
  timesRemaining: number;
  timesRequiered: string;
};

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
      console.error("Error fetching categories:", error);
    }
  };

  const createPost = async () => {
    try {
      const imageName = await uploadImage(uri);
      console.log(imageName); // Debugging line
      const response = await uploadPost(
        categories.filter((c: Categories) => c.label === activityName)[0]
          ?.value,
        imageName
      );
      if (response) {
        Alert.alert("Post created");
        setUri(null);
        setActivityName(null);
      }
    } catch (error) {
      console.log("Error creating post:", error);
    }
  };

  return (
    <View
      className={`flex-1 bg-[#${
        darkmode ? "2C2C2C" : "CCCCCC"
      }] justify-center items-center`}
    >
      <View className="bg-[#E4D8E9] rounded-lg w-10/12">
        {uri ? (
          <View className="items-center">
            <View className="rounded-lg w-full bg-black items-center">
              <Image
                className="justify-center my-5"
                source={{ uri: `file://${uri}` }}
                style={{ width: 140, height: 250 }}
              />
              <TouchableOpacity
                onPress={() => setUri(null)}
                className="border-white border-2 rounded-lg mb-5"
              >
                <Text className="text-white font-bold text-xl text-center px-6 py-2">
                  {
                    translations[language || "en-EN"].screens.UploadForm
                      .changePicture
                  }
                </Text>
              </TouchableOpacity>
            </View>
          </View>
        ) : (
          <View className="w-full h-3/5">
            <ImageStackNavigation />
          </View>
        )}
        <View className="m-10">
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
          className={`bg-[${
            !activityName || !uri || activity?.timesRemaining < 1
              ? "#CCCCCC"
              : "#E4007C"
          }] rounded-lg py-3 m-5 w-10/12`}
          onPress={createPost}
          disabled={!activityName || !uri || activity?.timesRemaining < 1}
        >
          <Text className="text-white font-bold text-xl text-center">Post</Text>
        </TouchableOpacity>
        <Text className="m-auto text-black">
          Times Remaining:{" "}
          {activity?.timesRemaining === null
            ? activity?.timesRequiered
            : activity?.timesRemaining}
        </Text>
      </View>
    </View>
  );
};

export default UploadFormScreen;
