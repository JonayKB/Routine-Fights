import { View, FlatList, RefreshControl } from "react-native";
import { Followers } from "../utils/User";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ProfileStackProps } from "../navigation/ProfileStackNavigation";
import { useEffect, useState } from "react";
import { translations } from "../../translations/translation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import ProfileNavigation from "../components/ProfileNavigation";
import FollowBox from "../components/FollowBox";
import SearchBar from "../components/SearchBar";
import { getFollows } from "../repositories/UserRepository";
import { bgColor } from "../utils/Utils";

type Props = NativeStackScreenProps<ProfileStackProps, "FollowList">;

const FollowListScreen = ({ navigation, route }: Props) => {
  const [load, setLoad] = useState<boolean>(false);
  const [users, setUsers] = useState<Followers[]>([]);
  const { language, darkmode } = useSettingsContext();
  const [searchText, setSearchText] = useState<string>("");

  useEffect(() => {
    fetchFollows();
  }, [route.params.email, route.params.type, load === true, searchText]);

  const fetchFollows = async () => {
    try {
      if (route.params.type === "followers") {
        const { followersByEmail } = await getFollows(
          route.params.email,
          searchText
        );
        setUsers(followersByEmail);
      } else if (route.params.type === "following") {
        const { followedByEmail } = await getFollows(
          route.params.email,
          searchText
        );
        setUsers(followedByEmail);
      }
    } catch (error) {
      console.error("Error fetching follows:", error);
    } finally {
      setLoad(false);
    }
  };

  return (
    <View className={`flex-1 ${bgColor(darkmode)}`}>
      <ProfileNavigation
        navigation={navigation}
        message={`${
          translations[language || "en-EN"].screens.Profile[route.params.type]
        }: ${users.length}`}
      />
      <View className="items-center">
        <View className="w-full">
          <SearchBar searchFunction={(text) => setSearchText(text)} />
        </View>
        <FlatList
          refreshControl={
            <RefreshControl refreshing={load} onRefresh={() => setLoad(true)} />
          }
          style={{ width: "100%" }}
          data={users}
          renderItem={({ item }) => {
            return (
              <FollowBox
                navigateFunction={() =>
                  navigation.navigate("Profile", { email: item.email })
                }
                item={item}
              />
            );
          }}
          keyExtractor={(item) => item.id}
        />
      </View>
    </View>
  );
};

export default FollowListScreen;
