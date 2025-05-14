import { Alert, Text, TouchableOpacity, View } from "react-native";
import React, { useEffect, useState } from "react";
import FormInput from "../components/FormInput";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { translations } from "../../translations/translation";
import { UserIn, UserOut } from "../utils/User";
import { getOwnUser, updateUser } from "../repositories/UserRepository";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ProfileStackProps } from "../navigation/ProfileStackNavigation";

type Props = NativeStackScreenProps<ProfileStackProps, "ProfileForm">;

const ProfileFormScreen = ({ navigation }: Props) => {
  const { language, darkmode } = useSettingsContext();
  const [user, setUser] = useState<UserIn>({} as UserIn);

  useEffect(() => {
    const fetchOwnUser = async () => {
      try {
        const response: UserOut = await getOwnUser();
        const ownUser: UserIn = {
          username: response.username,
          email: response.email,
          nationality: response.nationality,
          phoneNumber: response.phoneNumber,
          password: "",
          image: "null.jpg",
        };
        setUser(ownUser);
      } catch (error) {
        console.log(error);
      }
    };
    fetchOwnUser();
  }, []);

  const updateProfile = async () => {
    try {
      const response = await updateUser(user);
      if (response) {
        Alert.alert("Profile updated");
        navigation.navigate("Profile");
      } else {
        throw new Error("Error updating profile");
      }
    } catch (error) {
      console.log("Error updating profile", error);
    }
  };

  return (
    <View className={`flex-1 bg-[#${darkmode ? "2C2C2C" : "CCCCCC"}] items-center my-10`}>
      <View className="bg-[#E4D8E9] rounded-lg w-10/12">
        <View className="m-10">
          <FormInput
            label={translations[language || "en-EN"].screens.ProfileForm.email}
            name={user.email}
            setText={(text) => setUser({ ...user, email: text })}
            mode="text"
          />
          <FormInput
            label={
              translations[language || "en-EN"].screens.ProfileForm.username
            }
            name={user.username}
            setText={(text) => setUser({ ...user, username: text })}
            mode="text"
          />
          <FormInput
            label={
              translations[language || "en-EN"].screens.ProfileForm.password
            }
            name={user.password}
            setText={(text) => setUser({ ...user, password: text })}
            mode="text"
          />
          <FormInput
            label={
              translations[language || "en-EN"].screens.ProfileForm.nationality
            }
            name={user.nationality}
            setText={(text) => setUser({ ...user, nationality: text })}
            mode="text"
          />
          <FormInput
            label={
              translations[language || "en-EN"].screens.ProfileForm.phoneNumber
            }
            name={user.phoneNumber}
            setText={(text) => setUser({ ...user, phoneNumber: text })}
            mode="text"
          />
          <TouchableOpacity
            className="bg-[#E4007C] rounded-lg py-3 w-full"
            onPress={updateProfile}
          >
            <Text className="text-white font-bold text-xl text-center">
              {translations[language || "en-EN"].screens.ProfileForm.update}
            </Text>
          </TouchableOpacity>
        </View>
      </View>
    </View>
  );
};

export default ProfileFormScreen;
