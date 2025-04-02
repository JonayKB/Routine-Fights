import {
  View,
  Text,
  FlatList,
  Image,
  TouchableOpacity,
  RefreshControl,
} from "react-native";
import { Followers } from "../utils/User";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ProfileStackProps } from "../navigation/ProfileStackNavigation";
import Icon from "react-native-vector-icons/Ionicons";
import { useEffect, useState } from "react";
import { convertQuantityToString, uri } from "../utils/Utils";
import { getFollows } from "../services/ProfileService";
import { useAppContext } from "../contexts/TokenContextProvider";

type Props = NativeStackScreenProps<ProfileStackProps, "FollowList">;

const FollowList = ({ navigation, route }: Props) => {
  const [load, setLoad] = useState<boolean>(false);
  const [users, setUsers] = useState<Followers[]>([]);
  const { token } = useAppContext();

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
      <View className="flex-row bg-[#F1FEFC] border-b-2 border-[#4B0082] p-5">
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <Icon name="arrow-back" size={30} color="#4B0082" />
        </TouchableOpacity>
        <Text className="text-[#4B0082] font-bold text-2xl ml-5">
          {route.params.type}: {users.length}
        </Text>
      </View>
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
                  className="items-center bg-[#F1FEFC] flex-row mt-5 w-11/12 mx-auto rounded-xl"
                >
                  <Image
                    className="rounded-full m-5"
                    source={{
                      uri: uri + "/images/" + item.image,
                      headers: {
                        Authorization: `Bearer ${token}`,
                      },
                    }}
                    width={80}
                    height={80}
                  />
                  <View>
                    <Text className="text-black font-bold text-2xl">
                      {item.username}
                    </Text>
                    <Text className="text-black">
                      Followers: {convertQuantityToString(item.followers)}
                    </Text>
                    <Text className="text-black">
                      Following: {convertQuantityToString(item.following)}
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
