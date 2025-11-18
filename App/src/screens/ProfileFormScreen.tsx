import { Alert, Text, TouchableOpacity, View } from "react-native";
import React, { useEffect, useState } from "react";
import FormInput from "../components/FormInput";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { translations } from "../../translations/translation";
import { UserIn, UserOut } from "../utils/User";
import { getOwnUser, updateUser } from "../repositories/UserRepository";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ProfileStackProps } from "../navigation/ProfileStackNavigation";
import { cardBgColor } from "../utils/Utils";

type Props = NativeStackScreenProps<ProfileStackProps, "ProfileForm">;

const ProfileFormScreen = ({ navigation }: Props) => {
  const { language, darkmode } = useSettingsContext();
  const [user, setUser] = useState<UserIn>({} as UserIn);

  useEffect(() => {
    fetchOwnUser();
  }, []);

  const fetchOwnUser = async () => {
    try {
      const response: UserOut = await getOwnUser();
      const ownUser: UserIn = {
        username: response.username,
        email: response.email,
        nationality: response.nationality,
        phoneNumber: response.phoneNumber,
        password: "",
        image: response.image,
      };

      setUser(ownUser);
    } catch (error) {
      Alert.alert("Error", error.response.data);
    }
  };

  const updateProfile = async () => {
    try {
      await updateUser(user);
      Alert.alert("Profile updated");
      navigation.navigate("Profile");
    } catch (error) {
      Alert.alert("Error", error.response.data);
    }
  };

  return (
    <View
      className={`flex-1 ${cardBgColor(darkmode)} items-center justify-center`}
    >
      <View
        className={`${
          darkmode ? "bg-[#E8E2F0]" : "bg-white"
        } rounded-lg w-10/12`}
      >
        <View className="m-10">
          <FormInput
            label={translations[language || "en-US"].screens.ProfileForm.email}
            name={user.email}
            setText={(text) => setUser({ ...user, email: text })}
            mode="text"
          />
          <FormInput
            label={
              translations[language || "en-US"].screens.ProfileForm.username
            }
            name={user.username}
            setText={(text) => setUser({ ...user, username: text })}
            mode="text"
          />
          <FormInput
            label={
              translations[language || "en-US"].screens.ProfileForm.password
            }
            name={user.password}
            setText={(text) => setUser({ ...user, password: text })}
            mode="text"
          />
          <FormInput
            label={
              translations[language || "en-US"].screens.ProfileForm.nationality
            }
            name={user.nationality}
            setText={(text) => setUser({ ...user, nationality: text })}
            mode="text"
          />
          <FormInput
            label={
              translations[language || "en-US"].screens.ProfileForm.phoneNumber
            }
            name={user.phoneNumber}
            setText={(text) => setUser({ ...user, phoneNumber: text })}
            mode="text"
          />
          <TouchableOpacity
            className="bg-[#F65261] rounded-lg py-3 w-full"
            onPress={updateProfile}
          >
            <Text className="text-white font-bold text-xl text-center">
              {translations[language || "en-US"].screens.ProfileForm.update}
            </Text>
          </TouchableOpacity>
        </View>
      </View>
    </View>
  );
};

export default ProfileFormScreen;
