import { View, FlatList, RefreshControl } from "react-native";
import React, { useEffect, useRef, useState } from "react";
import { Followers } from "../utils/User";
import { fetchUsersByName } from "../repositories/SearchRepository";
import FollowBox from "../components/FollowBox";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { HomeStackProps } from "../navigation/HomeStackNavigation";
import SearchBarHeader from "../components/SearchBarHeader";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { bgColor } from "../utils/Utils";

type Props = NativeStackScreenProps<HomeStackProps, "Search">;

const SearchScreen = ({ navigation }: Props) => {
  const [users, setUsers] = useState<Followers[]>([]);
  const [searchText, setSearchText] = useState<string>("");
  const pageNum = useRef<number>(1);
  const [load, setLoad] = useState<boolean>(false);
  const [isLoadingMore, setIsLoadingMore] = useState<boolean>(false);
  const { darkmode } = useSettingsContext();

  useEffect(() => {
    if (load || isLoadingMore) {
      getUsers();
    }
  }, [load, isLoadingMore]);

  const getUsers = async () => {
    try {
      const response = await fetchUsersByName(pageNum.current, searchText);
      setUsers(isLoadingMore ? [...users, ...response] : response);
    } catch (error) {
      console.error("Error fetching users:", error);
    } finally {
      setLoad(false);
      setIsLoadingMore(false);
    }
  };

  const reload = () => {
    pageNum.current = 1;
    setLoad(true);
  };

  const changeText = (text: string) => {
    setSearchText(text);
    reload();
  };

  const loadMore = () => {
    if (isLoadingMore || users.length < pageNum.current * 10) return;
    pageNum.current += 1;
    setIsLoadingMore(true);
  };

  return (
    <View className={`flex-1 ${bgColor(darkmode)}`}>
      <SearchBarHeader
        navigation={navigation}
        searchFunction={(text) => changeText(text)}
      />
      <View className="items-center">
        <FlatList
          refreshControl={
            <RefreshControl refreshing={load} onRefresh={reload} />
          }
          style={{ width: "100%" }}
          data={users}
          renderItem={({ item }) => {
            return (
              <FollowBox
                navigateFunction={() =>
                  navigation.navigate("ProfileStackNavigation", {
                    screen: "Profile",
                    params: { email: item.email },
                  })
                }
                item={item}
              />
            );
          }}
          keyExtractor={(item) => item.id}
          onEndReached={loadMore}
          onEndReachedThreshold={0.5}
        />
      </View>
    </View>
  );
};

export default SearchScreen;
