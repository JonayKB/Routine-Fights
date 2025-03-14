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
import { useState } from "react";

type Props = NativeStackScreenProps<ProfileStackProps, "FollowList">;

const FollowList = ({ navigation, route }: Props) => {
  const [load, setLoad] = useState<boolean>(false);

  const convertQuantityToString = (quantity: number) => {
    if (quantity.toString().length > 5) {
      if (quantity > 999999) {
        return numberToString(quantity).slice(0, 5) + "M";
      }
      return numberToString(quantity).slice(0, 5) + "K";
    } else {
      return numberToString(quantity);
    }
  };

  const numberToString = (number: number) => {
    return new Intl.NumberFormat("en-EN").format(number);
  };

  const users: Followers[] = [
    {
      id: "1",
      username: "user",
      nationality: "Spain",
      image: "https://picsum.photos/200/300",
      createdAt: "2021-09-01",
      followers: 3855555,
      following: 900000,
    },
    {
      id: "2",
      username: "user2",
      nationality: "Russia",
      image: "https://picsum.photos/200/300",
      createdAt: "2021-09-01",
      followers: 72431,
      following: 145219042,
    },
    {
      id: "3",
      username: "user3",
      nationality: "USA",
      image: "https://picsum.photos/200/300",
      createdAt: "2021-09-01",
      followers: 38,
      following: 4762,
    },
  ];

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
              <View className="items-center bg-[#F1FEFC] flex-row mt-5 w-11/12 mx-auto rounded-xl">
                <Image
                  className="rounded-full m-5"
                  source={{
                    uri: item.image,
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
