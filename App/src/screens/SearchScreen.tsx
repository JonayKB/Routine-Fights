import { View, FlatList, RefreshControl } from "react-native";
import React, { useEffect, useRef, useState } from "react";
import { Followers } from "../utils/User";
import { fetchUsersByName } from "../repositories/SearchRepository";
import FollowBox from "../components/FollowBox";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { HomeStackProps } from "../navigation/HomeStackNavigation";
import SearchBarHeader from "../components/SearchBarHeader";
import { useSettingsContext } from "../contexts/SettingsContextProvider";

type Props = NativeStackScreenProps<HomeStackProps, "Search">;

const SearchScreen = ({ navigation }: Props) => {
  const [users, setUsers] = useState<Followers[]>([]);
  const [searchText, setSearchText] = useState<string>("");
  const pageNum = useRef<number>(1);
  const [load, setLoad] = useState<boolean>(false);
  const { darkmode } = useSettingsContext();

  useEffect(() => {
    pageNum.current = 1;
    getUsers();
  }, [load === true, searchText]);

  const getUsers = async () => {
    try {
      const response = await fetchUsersByName(pageNum.current, searchText);
      setUsers([...users, ...response]);
    } catch (error) {
      console.error("Error fetching users:", error);
    }
  };
  
  const reload = () => {
    setLoad(true);
    setTimeout(() => {
      setLoad(false);
    }, 1000);
  };

  return (
    <View className={`flex-1 bg-[#${darkmode ? "2C2C2C" : "CCCCCC"}]`}>
      <SearchBarHeader
        navigation={navigation}
        searchFunction={(text) => setSearchText(text)}
      />
      <View className="items-center">
        <FlatList
          refreshControl={
            <RefreshControl
              refreshing={load}
              onRefresh={() => {
                pageNum.current = 1;
                setUsers([]);
                reload();
              }}
            />
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
          onMomentumScrollEnd={() => {
            pageNum.current += 1;
            reload();
          }}
        />
      </View>
    </View>
  );
};

export default SearchScreen;
