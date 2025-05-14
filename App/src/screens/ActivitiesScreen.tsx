import { View, FlatList, RefreshControl } from "react-native";
import React, { useEffect, useRef, useState } from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ActivitiesStackProps } from "../navigation/ActivitiesStackNavigation";
import { Activity } from "../utils/Activity";
import { getActivitiesNotSubscribed } from "../repositories/ActivityRepository";
import ActivityCard from "../components/ActivityCard";
import AddButton from "../components/AddButton";
import SearchBar from "../components/SearchBar";
import { useSettingsContext } from "../contexts/SettingsContextProvider";

type Props = NativeStackScreenProps<ActivitiesStackProps, "Activities">;

const ActivitiesScreen = ({ navigation }: Props) => {
  const [load, setLoad] = useState<boolean>(false);
  const [activities, setActivities] = useState<Activity[]>([]);
  const [searchText, setSearchText] = useState<string>("");
  const pageNum = useRef(1);
  const { darkmode } = useSettingsContext();

  useEffect(() => {
    pageNum.current = 1;
    const fetchActivities = async () => {
      try {
        const response = await getActivitiesNotSubscribed(
          pageNum.current,
          searchText
        );
        setActivities(response);
      } catch (error) {
        console.error("Error fetching activities:", error);
      }
    };
    fetchActivities();
  }, [load === true, searchText]);

  const reload = () => {
    setLoad(true);
    setTimeout(() => {
      setLoad(false);
    }, 1000);
  };

  const loadMore = async () => {
    pageNum.current += 1;
    try {
      const response = await getActivitiesNotSubscribed(
        pageNum.current,
        searchText
      );
      setActivities([...activities, ...response]);
    } catch (error) {
      console.error("Error fetching activities:", error);
    }
  };

  return (
    <View className={`flex-1 bg-[#${darkmode ? "2C2C2C" : "CCCCCC"}]`}>
      <SearchBar searchFunction={(text) => setSearchText(text)} />
      <FlatList
        refreshControl={<RefreshControl refreshing={load} onRefresh={reload} />}
        style={{ width: "100%" }}
        data={activities}
        renderItem={({ item }) => {
          return (
            <ActivityCard
              item={item}
              navigateFunction={() =>
                navigation.navigate("ActivityDetails", { activity: item })
              }
            />
          );
        }}
        keyExtractor={(item) => item.id}
        numColumns={2}
        onEndReached={loadMore}
      />
      <AddButton navigateFunction={() => navigation.navigate("ActivityForm")} />
    </View>
  );
};

export default ActivitiesScreen;
