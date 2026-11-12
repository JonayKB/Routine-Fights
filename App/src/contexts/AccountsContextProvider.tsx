import React, { createContext, useCallback, useContext, useMemo, useState } from "react";
import { ActivityIndicator, Modal, Pressable, ScrollView, Text, TouchableOpacity, View } from "react-native";
import { CommonActions } from "@react-navigation/native";
import { useSafeAreaInsets } from "react-native-safe-area-context";
import Icon from "react-native-vector-icons/Ionicons";
import RNSecureKeyStore from "react-native-secure-key-store";
import { useSettingsContext } from "./SettingsContextProvider";
import { useTokenContext } from "./TokenContextProvider";
import { useToast } from "./FeedbackContextProvider";
import { Account, activate, listAccounts, upsertAccount } from "../utils/accounts";
import { logout } from "../repositories/SettingsRepository";
import { navigationRef, sessionStarted } from "../utils/session";
import { formatString } from "../components/Streak";
import { translations } from "../../translations/translation";
import { colors, palette } from "../styles/theme";
import Avatar from "../components/Avatar";

type AccountsContextType = {
  /** Shows the account switcher. */
  openSwitcher: () => void;
  /** Remembers the signed-in account (after logging in, or to refresh its name and picture). */
  remember: (user: { email: string; username?: string | null; image?: string | null }) => Promise<void>;
  /**
   * Signs the active account out. Moves to another signed-in account if
   * there is one, otherwise to the login screen.
   */
  signOut: (reason?: "expired") => Promise<void>;
};

const AccountsContext = createContext<AccountsContextType>({
  openSwitcher: () => {},
  remember: async () => {},
  signOut: async () => {},
});

export const useAccounts = () => useContext(AccountsContext);

/** Leaves `name` as the only screen of the root stack, mounted from scratch. */
const resetRoot = (name: string, params?: object) => {
  if (!navigationRef.isReady()) return;
  navigationRef.dispatch(CommonActions.reset({ index: 0, routes: [{ name, params }] }));
};

/**
 * Several accounts signed in at once, like Instagram. Switching resets the
 * whole app so nothing of the previous account stays on screen.
 */
const AccountsContextProvider = ({ children }: { children: React.ReactNode }) => {
  const { darkmode, language } = useSettingsContext();
  const { email, setToken, setEmail } = useTokenContext();
  const toast = useToast();
  const insets = useSafeAreaInsets();
  const t = translations[language || "en-EN"].screens.Accounts;
  const c = colors(darkmode);
  const accent = darkmode ? palette.accentOnDark : palette.accent;
  const [visible, setVisible] = useState(false);
  const [accounts, setAccounts] = useState<Account[]>([]);
  const [switching, setSwitching] = useState<string | null>(null);

  const remember = useCallback(
    async (user: { email: string; username?: string | null; image?: string | null }) => {
      try {
        const token = await RNSecureKeyStore.get("token");
        if (token && user?.email) {
          await upsertAccount({ email: user.email, username: user.username, image: user.image, token });
        }
      } catch {
        // No active session.
      }
    },
    []
  );

  const switchTo = useCallback(
    async (account: Account) => {
      await activate(account);
      sessionStarted();
      setToken(account.token);
      setEmail(account.email);
      resetRoot("MainTabNavigation");
    },
    [setToken, setEmail]
  );

  const openSwitcher = useCallback(() => {
    listAccounts().then(setAccounts);
    setVisible(true);
  }, []);

  const signOut = useCallback(
    async (reason?: "expired") => {
      let remaining: Account[] = [];
      try {
        remaining = await logout();
      } catch {
        // Nothing saved to remove.
      }
      setToken("");
      setEmail("");
      if (remaining.length) {
        await switchTo(remaining[0]);
        const name = remaining[0].username || remaining[0].email;
        toast.info(
          reason === "expired"
            ? translations[language || "en-EN"].screens.Errors.sessionExpired
            : formatString(t.switchedTo, { name })
        );
      } else {
        resetRoot("Login");
        if (reason === "expired") toast.info(translations[language || "en-EN"].screens.Errors.sessionExpired);
      }
    },
    [setToken, setEmail, switchTo, toast, language, t]
  );

  const choose = async (account: Account) => {
    if (account.email === email) {
      setVisible(false);
      return;
    }
    setSwitching(account.email);
    try {
      await switchTo(account);
      setVisible(false);
    } finally {
      setSwitching(null);
    }
  };

  const addAccount = () => {
    setVisible(false);
    // The current session stays active until the new one signs in.
    if (navigationRef.isReady()) navigationRef.navigate("Login", { adding: true });
  };

  const value = useMemo(() => ({ openSwitcher, remember, signOut }), [openSwitcher, remember, signOut]);

  return (
    <AccountsContext.Provider value={value}>
      {children}
      <Modal visible={visible} transparent animationType="fade" onRequestClose={() => setVisible(false)}>
        <Pressable style={{ flex: 1, backgroundColor: "#00000080" }} onPress={() => setVisible(false)} />
        <View
          style={{
            maxHeight: "75%",
            backgroundColor: c.card,
            borderTopLeftRadius: 20,
            borderTopRightRadius: 20,
            paddingTop: 12,
            paddingBottom: Math.max(insets.bottom, 16),
          }}
        >
          <View style={{ alignSelf: "center", width: 36, height: 4, borderRadius: 2, backgroundColor: c.raised }} />
          <Text style={{ color: c.text }} className="text-lg font-semibold mt-4 mb-2 px-5">
            {t.title}
          </Text>
          <ScrollView>
            {accounts.map((account) => {
              const active = account.email === email;
              const name = account.username || account.email.split("@")[0];
              return (
                <TouchableOpacity
                  key={account.email}
                  activeOpacity={0.6}
                  accessibilityRole="radio"
                  accessibilityState={{ selected: active }}
                  onPress={() => choose(account)}
                  className="flex-row items-center px-5 py-3"
                >
                  <Avatar image={account.image} name={name} size={44} ring={active ? accent : undefined} />
                  <View className="flex-1 ml-3">
                    <Text numberOfLines={1} style={{ color: c.text }} className="text-base font-medium">
                      {name}
                    </Text>
                    <Text numberOfLines={1} style={{ color: c.textMuted }} className="text-xs">
                      {account.email}
                    </Text>
                  </View>
                  {switching === account.email ? (
                    <ActivityIndicator color={accent} />
                  ) : active ? (
                    <Icon name="checkmark-circle" size={22} color={accent} />
                  ) : null}
                </TouchableOpacity>
              );
            })}
            <TouchableOpacity activeOpacity={0.6} onPress={addAccount} className="flex-row items-center px-5 py-3">
              <View
                style={{ width: 44, height: 44, borderRadius: 22, borderWidth: 1, borderColor: c.hairline }}
                className="items-center justify-center"
              >
                <Icon name="add" size={22} color={c.text} />
              </View>
              <Text style={{ color: c.text }} className="ml-3 text-base font-medium">
                {t.add}
              </Text>
            </TouchableOpacity>
          </ScrollView>
        </View>
      </Modal>
    </AccountsContext.Provider>
  );
};

export default AccountsContextProvider;
