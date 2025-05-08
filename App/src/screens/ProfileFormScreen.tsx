import { View } from "react-native";
import React, { useEffect, useState } from "react";
import FormInput from "../components/FormInput";
import { useLanguageContext } from "../contexts/SettingsContextProvider";
import { translations } from "../../translations/translation";
import { UserIn, UserOut } from "../utils/User";
import { getOwnUser } from "../repositories/UserRepository";
import { runOnRuntime } from "react-native-reanimated";

type Props = {};

const ProfileFormScreen = (props: Props) => {
  const { language } = useLanguageContext();
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

  return (
    <View className="flex-1 items-center my-10">
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
        </View>
      </View>
    </View>
  );
};

export default ProfileFormScreen;
