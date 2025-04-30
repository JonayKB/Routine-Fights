import {
  View,
  FlatList,
  TouchableOpacity,
  TextInput,
  Text,
  Image,
  RefreshControl,
} from "react-native";
import React, { useEffect, useRef, useState } from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ActivitiesStackProps } from "../navigation/ActivitiesStackNavigation";
import { Activity } from "../utils/Activity";
import { useLanguageContext } from "../contexts/SettingsContextProvider";
import { translations } from "../../translations/translation";
import { getActivitiesNotSubscribed } from "../repositories/ActivityRepository";

type Props = NativeStackScreenProps<ActivitiesStackProps, "Activities">;

const Activities = ({ navigation }: Props) => {
  const { language } = useLanguageContext();
  const [load, setLoad] = useState<boolean>(false);
  const [activities, setActivities] = useState<Activity[]>([]);
  const pageNum = useRef(1);

  useEffect(() => {
    pageNum.current = 1;
    const fetchActivities = async () => {
      try {
        const response = await getActivitiesNotSubscribed(pageNum.current, "");
        setActivities(response);
      } catch (error) {
        console.error("Error fetching activities:", error);
      }
    };
    fetchActivities();
  }, [load === true]);

  const loadMore = async () => {
    pageNum.current += 1;
    try {
      const response = await getActivitiesNotSubscribed(pageNum.current, "");
      setActivities([...activities, response]);
    } catch (error) {
      console.error("Error fetching activities:", error);
    }
  };

  return (
    <View className="flex-1 bg-[#E4D8E9]">
      <View className="justify-center items-center">
        <TextInput
          placeholder={
            translations[language || "en-EN"].screens.Home.search
          }
          placeholderTextColor="#4B0082"
          className="border-[#4B0082] border-2 rounded-xl bg-white text-2xl w-11/12 my-5 pl-3 text-black"
        />
      </View>
      <FlatList
        refreshControl={
          <RefreshControl
            refreshing={load}
            onRefresh={() => {
              setLoad(true);
              setTimeout(() => {
                setLoad(false);
              }, 1000);
            }}
          />
        }
        style={{ width: "100%" }}
        data={activities}
        renderItem={({ item }) => {
          return (
            <TouchableOpacity
              onPress={() =>
                navigation.navigate("ActivityDetails", { activity: item })
              }
              className="w-44 h-56 m-6"
            >
              <Image
                source={{
                  uri: item.image,
                }}
                className="w-full h-40 rounded-t-2xl border-[#4B0082] border-2"
              />
              <Text className="flex-1 rounded-b-2xl border-[#4B0082] border-2 align-middle text-[#4B0082] text-center text-xl bg-white">
                {item.name}
              </Text>
            </TouchableOpacity>
          );
        }}
        keyExtractor={(item) => item.id}
        numColumns={2}
        onEndReached={loadMore}
      />
      <TouchableOpacity
        onPress={() => navigation.navigate("ActivityForm")}
        className="bg-[#E4007C] rounded-lg m-5"
      >
        <Text
          className="text-white text-8xl font-bold text-center"
          style={{ fontFamily: "InriaSans-Regular" }}
        >
          +
        </Text>
      </TouchableOpacity>
    </View>
  );
};

export default Activities;
