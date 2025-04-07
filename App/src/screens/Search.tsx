import {
  View,
  TextInput,
  FlatList,
  RefreshControl,
  TouchableOpacity,
  Alert,
} from "react-native";
import React, { useEffect, useRef, useState } from "react";
import { translations } from "../../translations/translation";
import { useLanguageContext } from "../contexts/LanguageContextProvider";
import { Followers } from "../utils/User";
import { fetchUsersByName } from "../repositories/SearchRepository";
import FollowBox from "../components/FollowBox";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { HomeStackProps } from "../navigation/HomeStackNavigation";
import Icon from "react-native-vector-icons/Ionicons";

type Props = NativeStackScreenProps<HomeStackProps, "Search">;

const Search = ({ navigation }: Props) => {
  const { language } = useLanguageContext();
  const [users, setUsers] = useState<Followers[]>([]);
  const regex = useRef<string>(null);

  return (
    <View>
      <View className="flex-row bg-[#F1FEFC] border-b-2 border-[#4B0082] p-5 items-center">
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <Icon name="arrow-back" size={30} color="#4B0082" />
        </TouchableOpacity>
        <TextInput
          className="flex-1 bg-[#4B0082] rounded-xl p-3 ml-2"
          placeholder={translations[language].screens.Home.search}
          onChangeText={async (text) => {
            regex.current = text;
            setUsers(await fetchUsersByName(text));
          }}
        />
      </View>
      <View className="items-center">
        <FlatList
          style={{ width: "100%" }}
          data={users}
          renderItem={({ item }) => {
            regex.current = item.username;
            return <FollowBox navigation={navigation} item={item} />;
          }}
          keyExtractor={(item) => item.id}
        />
      </View>
    </View>
  );
};

export default Search;
