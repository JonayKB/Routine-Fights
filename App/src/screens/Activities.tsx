import {
  View,
  FlatList,
  TouchableOpacity,
  TextInput,
  Text,
  Image,
  RefreshControl,
} from "react-native";
import React, { useEffect, useState } from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ActivitiesStackProps } from "../navigation/ActivitiesStackNavigation";
import { Activity } from "../utils/Activity";

type Props = NativeStackScreenProps<ActivitiesStackProps, "Activities">;

const Activities = ({ navigation, route }: Props) => {
  const [filteredActivities, setFilteredActivities] = useState<Activity[]>([]);
  const [search, setSearch] = useState<string>("");
  const [load, setLoad] = useState<boolean>(false);

  const activities: Activity[] = [
    {
      id: "1",
      name: "Activity 1",
      description: "Description 1",
      image: "https://picsum.photos/200/300",
      timeRate: "1",
      timesRequired: "1",
      category: "1",
    },
    {
      id: "2",
      name: "Activity 2",
      description: "Description 2",
      image: "https://picsum.photos/200/300",
      timeRate: "2",
      timesRequired: "2",
      category: "2",
    },
    {
      id: "3",
      name: "Activity 3",
      description: "Description 3",
      image: "https://picsum.photos/200/300",
      timeRate: "3",
      timesRequired: "3",
      category: "3",
    },
  ];

  useEffect(() => {
    if (search === "") {
      return setFilteredActivities(activities);
    }
    setFilteredActivities(
      activities.filter((activity) =>
        activity.name.toLowerCase().includes(search.toLowerCase())
      )
    );
  }, [search]);

  return (
    <View className="flex-1 bg-[#E4D8E9]">
      <View className="justify-center items-center">
        <TextInput
          placeholder="Name"
          placeholderTextColor="#4B0082"
          inputMode="email"
          className="border-[#4B0082] border-2 rounded-xl bg-white text-2xl w-11/12 my-5 pl-3 text-black"
          onChangeText={(text) => setSearch(text)}
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
        data={filteredActivities}
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
      />
      <TouchableOpacity
        onPress={() => navigation.navigate("ActivityForm")}
      >
        <Text
          className="bg-[#E4007C] rounded-lg m-5 text-white text-8xl font-bold text-center"
          style={{ fontFamily: "InriaSans-Regular" }}
        >
          +
        </Text>
      </TouchableOpacity>
    </View>
  );
};

export default Activities;
