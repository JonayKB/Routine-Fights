import { View, FlatList, RefreshControl } from "react-native";
import { Followers } from "../utils/User";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ProfileStackProps } from "../navigation/ProfileStackNavigation";
import { useEffect, useState } from "react";
import { translations } from "../../translations/translation";
import { useLanguageContext } from "../contexts/SettingsContextProvider";
import ProfileNavigation from "../components/ProfileNavigation";
import FollowBox from "../components/FollowBox";
import {
  followUser,
  getFollows,
  unfollowUser,
} from "../repositories/UserRepository";

type Props = NativeStackScreenProps<ProfileStackProps, "FollowList">;

const FollowListScreen = ({ navigation, route }: Props) => {
  const [load, setLoad] = useState<boolean>(false);
  const [users, setUsers] = useState<Followers[]>([]);
  const { language } = useLanguageContext();

  useEffect(() => {
    const fetchFollows = async () => {
      if (route.params.type === "followers") {
        const { followersByEmail } = await getFollows(route.params.email);
        setUsers(followersByEmail);
      } else if (route.params.type === "following") {
        const { followedByEmail } = await getFollows(route.params.email);
        setUsers(followedByEmail);
      }
    };
    fetchFollows();
  }, [route.params.email, route.params.type, load === true]);

  const reload = () => {
    setLoad(true);
    setTimeout(() => {
      setLoad(false);
    }, 1000);
  };

  return (
    <View>
      <ProfileNavigation
        navigation={navigation}
        message={`${
          translations[language || "en-EN"].screens.Profile[route.params.type]
        }: ${users.length}`}
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
                  navigation.navigate("Profile", { email: item.email })
                }
                item={item}
                following={item.isFollowing}
                followFunction={async (item) => {
                  item.isFollowing
                    ? await unfollowUser(item.email)
                    : await followUser(item.email);
                  reload();
                }}
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
