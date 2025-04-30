import {
  View,
  FlatList,
} from "react-native";
import React, { useState } from "react";
import { Followers } from "../utils/User";
import { fetchUsersByName } from "../repositories/SearchRepository";
import FollowBox from "../components/FollowBox";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { HomeStackProps } from "../navigation/HomeStackNavigation";
import SearchBar from '../components/SearchBar';

type Props = NativeStackScreenProps<HomeStackProps, "Search">;

const Search = ({ navigation }: Props) => {
  const [users, setUsers] = useState<Followers[]>([]);

  return (
    <View>
      <SearchBar navigation={navigation} searchFunction={async (text) => setUsers(await fetchUsersByName(text))}/>
      <View className="items-center">
        <FlatList
          style={{ width: "100%" }}
          data={users}
          renderItem={({ item }) => {
            return <FollowBox navigation={navigation} item={item} />;
          }}
          keyExtractor={(item) => item.id}
        />
      </View>
    </View>
  );
};

export default Search;
