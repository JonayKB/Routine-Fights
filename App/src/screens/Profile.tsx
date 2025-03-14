import { Image, Text, View, TouchableOpacity } from "react-native";
import React, { useEffect, useState } from "react";
import { UserOut } from "../utils/User";

type Props = {};

const Profile = (props: Props) => {
  const [followers, setFollowers] = useState<string>("");
  const [following, setFollowing] = useState<string>("");

  const user: UserOut = {
    id: "1",
    username: "user",
    email: "email@gmail.com",
    nationality: "Spain",
    phoneNumber: "123456789",
    image: "https://picsum.photos/200/300",
    createdAt: "2021-09-01",
    followers: 3855555,
    following: 900000,
  };

  useEffect(() => {
    setFollowers(convertQuantityToString(user.followers));
    setFollowing(convertQuantityToString(user.following));
  }, []);

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

  return (
    <View>
      <View className="bg-[#F1FEFC] flex-row">
        <View className="m-5 items-center">
          <Image
            source={{ uri: user.image }}
            width={103}
            height={103}
            className="rounded-full border-2 border-[#4B0082]"
          />
          <View
            style={{ backgroundColor: "rgba(0, 0, 0, 0.75)" }}
            className="-mt-4 w-10 rounded-xl justify-center items-center"
          >
            <Text className="text-center">239</Text>
          </View>
        </View>
        <View className="mt-5">
          <Text className="text-black text-4xl font-bold mb-5">
            {user.username}
          </Text>
          <View className="flex-1 flex-col">
            <TouchableOpacity>
              <Text className="text-black text-lg">Followers: {followers}</Text>
            </TouchableOpacity>
            <TouchableOpacity>
              <Text className="text-black text-lg">Following: {following}</Text>
            </TouchableOpacity>
          </View>
        </View>
      </View>
    </View>
  );
};

export default Profile;
