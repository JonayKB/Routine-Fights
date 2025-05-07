import { View, FlatList, RefreshControl } from "react-native";
import React, { useEffect, useRef, useState } from "react";
import { Followers } from "../utils/User";
import { fetchUsersByName } from "../repositories/SearchRepository";
import FollowBox from "../components/FollowBox";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { HomeStackProps } from "../navigation/HomeStackNavigation";
import { followUser, unfollowUser } from "../repositories/UserRepository";
import SearchBarHeader from "../components/SearchBarHeader";

type Props = NativeStackScreenProps<HomeStackProps, "Search">;

const Search = ({ navigation }: Props) => {
  const [users, setUsers] = useState<Followers[]>([]);
  const [searchText, setSearchText] = useState<string>("");
  const pageNum = useRef<number>(1);
  const [load, setLoad] = useState<boolean>(false);

  useEffect(() => {
    pageNum.current = 1;
    const getUsers = async () => {
      try {
        const response = await fetchUsersByName(pageNum.current, searchText);
        setUsers(response);
        console.log(response);
      } catch (error) {
        console.error("Error fetching users:", error);
      }
    };
    getUsers();
  }, [load === true, searchText]);

  const loadMore = async () => {
    pageNum.current += 1;
    try {
      const response = await fetchUsersByName(pageNum.current, searchText);
      setUsers([...users, ...response]);
    } catch (error) {
      console.error("Error fetching users:", error);
    }
  };

  return (
    <View>
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
                setLoad(true);
                setTimeout(() => {
                  setLoad(false);
                }, 1000);
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
                following={item.isFollowing}
                followFunction={async (item) => {
                  item.isFollowing
                    ? await unfollowUser(item.email)
                    : await followUser(item.email);
                  setLoad(true);
                  setTimeout(() => {
                    setLoad(false);
                  }, 1000);
                }}
              />
            );
          }}
          keyExtractor={(item) => item.id}
          onEndReached={loadMore}
        />
      </View>
    </View>
  );
};

export default Search;
