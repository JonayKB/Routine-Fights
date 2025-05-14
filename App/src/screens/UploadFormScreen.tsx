import { Image, View, TouchableOpacity, Text, Alert } from "react-native";
import React, { useEffect, useRef, useState } from "react";
import { useImageContext } from "../contexts/ImageContextProvider";
import ImageStackNavigation from "../navigation/ImageStackNavigation";
import DropDown from "../components/DropDown";
import { translations } from "../../translations/translation";
import { useLanguageContext } from "../contexts/SettingsContextProvider";
import { getSubscribedActivities } from "../repositories/ActivityRepository";
import { Activity } from "../utils/Activity";
import { uploadPost } from "../repositories/PostRepository";
import { uploadImage } from "../repositories/ImageRepository";

type Props = {};

type Categories = {
  label: string;
  value: string;
};

const UploadFormScreen = (props: Props) => {
  const { uri, setUri } = useImageContext();
  const [categories, setCategories] = useState<Categories[]>([]);
  const [category, setCategory] = useState<string>(null);
  const { language } = useLanguageContext();

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await getSubscribedActivities();
        const categories: Categories[] = response.map((category: Activity) => ({
          label: category.name,
          value: category.id,
        }));
        setCategories(categories);
      } catch (error) {
        console.error("Error fetching categories:", error);
      }
    };
    fetchCategories();
  }, []);

  const createPost = async () => {
    try {
      const imageName = await uploadImage(uri);
      const response = await uploadPost(
        categories.filter((c: Categories) => c.label === category)[0].value,
        imageName
      );
      if (response) {
        Alert.alert("Post created");
        setUri(null);
        setCategory(null);
      }
    } catch (error) {
      console.log("Error creating post:", error);
    }
  };

  return (
    <View className="flex-1 justify-center items-center">
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
            value={category}
            setValue={setCategory}
            message={
              translations[language || "en-EN"].screens.UploadForm
                .selectCategory
            }
          />
        </View>
        <TouchableOpacity
          className="bg-[#E4007C] rounded-lg py-3 m-5 w-10/12"
          onPress={createPost}
        >
          <Text className="text-white font-bold text-xl text-center">Post</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
};

export default UploadFormScreen;
