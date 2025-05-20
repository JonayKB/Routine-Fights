import React, { useEffect, useState } from "react";
import ImageStackNavigation from "../navigation/ImageStackNavigation";
import { useImageContext } from "../contexts/ImageContextProvider";
import ChangePicture from "../components/ChangePicture";
import { Alert, Button, View } from "react-native";
import { uploadImage } from "../repositories/ImageRepository";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ProfileStackProps } from "../navigation/ProfileStackNavigation";
import { getUser, updateUser } from "../repositories/UserRepository";
import { UserIn, UserOut } from "../utils/User";

type Props = NativeStackScreenProps<ProfileStackProps, "ProfilePictureScreen">;

const ProfilePictureScreen = (props: Props) => {
  const { uri, setUri, setWidth, setHeight } = useImageContext();
  const [user, setUser] = useState<UserOut>({} as UserOut);

  useEffect(() => {
    fetchUser();
  }, [props.route.params?.email]);

  useEffect(() => {
    setWidth(1);
    setHeight(1);
  }, []);

  const fetchUser = async () => {
    try {
      const response = await getUser(props.route.params.email);
      setUser(response);
    } catch (error) {
      Alert.alert("Error", error.response.data);
    }
  };

  const handleChange = async () => {
    try {
      const imageName = await uploadImage(uri);
      const foundUser: UserIn = {
        username: user.username,
        email: user.email,
        nationality: user.nationality,
        phoneNumber: user.phoneNumber,
        password: "",
        image: imageName,
      };
      const response = await updateUser(foundUser);
      if (response) {
        setUri(null);
        props.navigation.goBack();
      }
    } catch (error) {
      Alert.alert("Error", error.response.data);
    }
  };

  return (
    <View className="flex-1">
      {uri ? (
        <View className="flex-1">
          <ChangePicture uri={uri} setUri={setUri} />
          <Button title="Upload" onPress={handleChange} />
        </View>
      ) : (
        <View className="flex-1 w-full h-3/5">
          <ImageStackNavigation />
        </View>
      )}
    </View>
  );
};

export default ProfilePictureScreen;
