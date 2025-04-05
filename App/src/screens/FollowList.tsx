import {
  View,
  Text,
  FlatList,
  TouchableOpacity,
  RefreshControl,
} from "react-native";
import { Followers } from "../utils/User";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ProfileStackProps } from "../navigation/ProfileStackNavigation";
import { useEffect, useState } from "react";
import { convertQuantityToString } from "../utils/Utils";
import { getFollows } from "../services/ProfileService";
import { translations } from "../../translations/translation";
import { useLanguageContext } from "../contexts/LanguageContextProvider";
import ProfilePicture from "../components/ProfilePicture";
import ProfileNavigation from "../components/ProfileNavigation";

type Props = NativeStackScreenProps<ProfileStackProps, "FollowList">;

const FollowList = ({ navigation, route }: Props) => {
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
  }, [route.params.email, route.params.type]);

  return (
    <View>
      <ProfileNavigation
        navigation={navigation}
        message={`${translations[language || 'en-EN'].screens.Profile[route.params.type]}: ${users.length}`}
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
              <View>
                <TouchableOpacity
                  onPress={() =>
                    navigation.navigate("Profile", { id: item.id })
                  }
                  className="items-center bg-[#F1FEFC] flex-row mt-5 w-11/12 mx-auto rounded-xl p-2"
                >
                  <ProfilePicture image={item.image} size={80} />
                  <View className="ml-5">
                    <Text className="text-black font-bold text-2xl">
                      {item.username}
                    </Text>
                    <Text className="text-black">
                      {translations[language || 'en-EN'].screens.Profile.followers}:{" "}
                      {convertQuantityToString(item.followers)}
                    </Text>
                    <Text className="text-black">
                      {translations[language || 'en-EN'].screens.Profile.following}:{" "}
                      {convertQuantityToString(item.following)}
                    </Text>
                  </View>
                  <TouchableOpacity className="border-[#E4007C] border-2 rounded-lg ml-5">
                    <Text className="text-[#4B0082] font-bold text-xl text-center px-6 py-2">
                      Follow
                    </Text>
                  </TouchableOpacity>
                </TouchableOpacity>
              </View>
            );
          }}
          keyExtractor={(item) => item.id}
        />
      </View>
    </View>
  );
};

export default FollowList;
