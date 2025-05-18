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
import { limit } from "../utils/Utils";

type Props = NativeStackScreenProps<ActivitiesStackProps, "Activities">;

const ActivitiesScreen = ({ navigation }: Props) => {
  const [load, setLoad] = useState<boolean>(false);
  const [isLoadingMore, setIsLoadingMore] = useState<boolean>(false);
  const [activities, setActivities] = useState<Activity[]>([]);
  const [searchText, setSearchText] = useState<string>("");
  const pageNum = useRef(1);
  const { darkmode } = useSettingsContext();

  useEffect(() => {
    fetchActivities();
  }, []);

  useEffect(() => {
    if (load || isLoadingMore) {
      fetchActivities();
    }
  }, [load, isLoadingMore]);

  const fetchActivities = async () => {
    try {
      const response = await getActivitiesNotSubscribed(
        pageNum.current,
        searchText
      );
      setActivities(isLoadingMore ? [...activities, ...response] : response);
    } catch (error) {
      console.error("Error fetching activities:", error);
    } finally {
      setLoad(false);
      setIsLoadingMore(false);
    }
  };

  const changeText = (text: string) => {
    pageNum.current = 1;
    setSearchText(text);
    setLoad(true);
  };

  const reload = () => {
    pageNum.current = 1;
    setLoad(true);
  };

  const loadMore = () => {
    if (isLoadingMore || activities.length < pageNum.current * limit) return;
    pageNum.current += 1;
    setIsLoadingMore(true);
  };

  return (
    <View className={`flex-1 ${darkmode ? "bg-[#1C1C1E]" : "bg-[#FCFCFC]"}`}>
      <SearchBar searchFunction={changeText} />
      <FlatList
        refreshControl={<RefreshControl refreshing={load} onRefresh={reload} />}
        style={{ width: "100%" }}
        data={activities}
        renderItem={({ item }) => (
          <ActivityCard
            item={item}
            navigateFunction={() =>
              navigation.navigate("ActivityDetails", {
                activity: item,
                suscribed: false,
              })
            }
          />
        )}
        keyExtractor={(item) => item.id}
        numColumns={2}
        onEndReached={loadMore}
        onEndReachedThreshold={0.5}
      />
      <AddButton navigateFunction={() => navigation.navigate("ActivityForm")} />
    </View>
  );
};

export default ActivitiesScreen;
