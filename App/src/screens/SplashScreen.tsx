import React, { useEffect } from "react";
import { ActivityIndicator, Text, View } from "react-native";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import RNSecureKeyStore from "react-native-secure-key-store";
import { LoginStackProps } from "../navigation/LoginStackNavigation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { useTokenContext } from "../contexts/TokenContextProvider";
import StreakFlame from "../components/StreakFlame";
import { fonts, palette } from "../styles/theme";
import { bgColor, resetNavigation, textColor } from "../utils/Utils";
import { getOwnUserImageAndEmail } from "../repositories/UserRepository";
import { SESSION_EXPIRED } from "../utils/graphql";
import { useAccounts } from "../contexts/AccountsContextProvider";

type Props = NativeStackScreenProps<LoginStackProps, "Splash">;

/** Reads the saved session on start and opens the feed or the login form. */
const SplashScreen = ({ navigation }: Props) => {
  const { darkmode, ready } = useSettingsContext();
  const { setToken } = useTokenContext();
  const { remember } = useAccounts();

  useEffect(() => {
    if (!ready) return;
    const open = async () => {
      let token: string | null = null;
      try {
        token = await RNSecureKeyStore.get("token");
      } catch {
        // No saved session.
      }
      if (token) {
        // Check the saved session is still accepted before opening the feed.
        // Without a connection, open it anyway and let the feed show the error.
        try {
          // Sessions from before multi-account are added to the list here.
          await remember(await getOwnUserImageAndEmail());
        } catch (error: any) {
          // SessionGuard has already sent the user to the login screen.
          if (error?.message === SESSION_EXPIRED) return;
        }
        setToken(token);
        resetNavigation(navigation, "MainTabNavigation");
      } else {
        resetNavigation(navigation, "Login");
      }
    };
    open();
  }, [ready]);

  return (
    <View className={`flex-1 items-center justify-center ${bgColor(darkmode)}`}>
      <StreakFlame streak={3} size={72} animated />
      <Text
        style={{ fontFamily: fonts.display }}
        className={`text-3xl mt-5 ${textColor(darkmode)}`}
      >
        Routine Fights
      </Text>
      <ActivityIndicator
        className="mt-8"
        color={darkmode ? palette.accentOnDark : palette.accent}
      />
    </View>
  );
};

export default SplashScreen;
