import { useEffect } from "react";
import { CommonActions } from "@react-navigation/native";
import { logout } from "../repositories/SettingsRepository";
import { useTokenContext } from "../contexts/TokenContextProvider";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import { useToast } from "../contexts/FeedbackContextProvider";
import { navigationRef, setSessionExpiredHandler } from "../utils/session";
import { translations } from "../../translations/translation";

/**
 * When the API stops accepting the saved token (it expired, or the account
 * changed), forget it and send the user back to the login screen once.
 */
const SessionGuard = () => {
  const { setToken, setEmail } = useTokenContext();
  const { language } = useSettingsContext();
  const toast = useToast();

  useEffect(() => {
    setSessionExpiredHandler(async () => {
      try {
        await logout();
      } catch {
        // Nothing saved to remove.
      }
      setToken("");
      setEmail("");
      if (navigationRef.isReady()) {
        navigationRef.dispatch(
          CommonActions.reset({ index: 0, routes: [{ name: "Login" }] })
        );
      }
      toast.info(translations[language || "en-EN"].screens.Errors.sessionExpired);
    });
    return () => setSessionExpiredHandler(null);
  }, [language, toast]);

  return null;
};

export default SessionGuard;
