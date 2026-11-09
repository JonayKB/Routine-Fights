import React, { useEffect, useState } from "react";
import { ActivityIndicator, Text, TouchableOpacity, View } from "react-native";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { translations } from "../../translations/translation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { useImageContext } from "../contexts/ImageContextProvider";
import { useToast } from "../contexts/FeedbackContextProvider";
import PhotoPicker from "../components/PhotoPicker";
import PickedPhotoPreview from "../components/PickedPhotoPreview";
import ProfileNavigation from "../components/ProfileNavigation";
import { uploadImage } from "../repositories/ImageRepository";
import { ProfileStackProps } from "../navigation/ProfileStackNavigation";
import { getUser, updateUser } from "../repositories/UserRepository";
import { UserIn, UserOut } from "../utils/User";
import { errorMessage } from "../utils/graphql";
import { bgColor, borderColor, mutedTextColor, raisedBgColor } from "../utils/Utils";

type Props = NativeStackScreenProps<ProfileStackProps, "ProfilePictureScreen">;

const ProfilePictureScreen = (props: Props) => {
  const toast = useToast();
  const { picked, clear, setWidth, setHeight } = useImageContext();
  const { language, darkmode } = useSettingsContext();
  const t = translations[language || "en-EN"].screens;
  const [user, setUser] = useState<UserOut>({} as UserOut);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    fetchUser();
  }, [props.route.params?.email]);

  useEffect(() => {
    clear();
    setWidth(1);
    setHeight(1);
    return () => {
      // Don't leak this photo or the square ratio into the other forms.
      clear();
      setWidth(4);
      setHeight(5);
    };
  }, []);

  const fetchUser = async () => {
    try {
      setUser(await getUser(props.route.params.email));
    } catch (error) {
      toast.error(errorMessage(error));
    }
  };

  const save = async () => {
    if (!picked) return;
    setSaving(true);
    try {
      const imageName = await uploadImage(picked);
      const foundUser: UserIn = {
        username: user.username,
        email: user.email,
        nationality: user.nationality,
        phoneNumber: user.phoneNumber,
        password: "",
        image: imageName,
      };
      if (await updateUser(foundUser, language)) {
        props.navigation.goBack();
      }
    } catch (error) {
      toast.error(errorMessage(error));
    } finally {
      setSaving(false);
    }
  };

  return (
    <View className={`flex-1 ${bgColor(darkmode)}`}>
      <ProfileNavigation navigation={props.navigation} message={t.UploadForm.changePicture} />

      <View className="flex-1 px-4 pt-2">
        <View
          className={`flex-1 rounded-2xl overflow-hidden border ${picked ? "" : "border-dashed"} ${borderColor(
            darkmode
          )}`}
          style={{ maxHeight: 460 }}
        >
          {picked ? <PickedPhotoPreview /> : <PhotoPicker />}
        </View>
      </View>

      <View className="px-4 pt-4 pb-6">
        <TouchableOpacity
          activeOpacity={0.85}
          disabled={!picked || saving}
          onPress={save}
          className={`h-12 rounded-xl items-center justify-center ${
            picked ? "bg-[#C2410C]" : raisedBgColor(darkmode)
          }`}
        >
          {saving ? (
            <ActivityIndicator color="#FFFFFF" />
          ) : (
            <Text className={`text-base font-semibold ${picked ? "text-white" : mutedTextColor(darkmode)}`}>
              {t.ImageEditor.save}
            </Text>
          )}
        </TouchableOpacity>
      </View>
    </View>
  );
};

export default ProfilePictureScreen;
