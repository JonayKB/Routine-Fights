import { useEffect } from "react";
import { setSessionExpiredHandler } from "../utils/session";
import { useAccounts } from "../contexts/AccountsContextProvider";

/**
 * When the API stops accepting the saved token (it expired, or the account
 * changed), forget that account: move to another signed-in one, or to the
 * login screen.
 */
const SessionGuard = () => {
  const { signOut } = useAccounts();

  useEffect(() => {
    setSessionExpiredHandler(() => signOut("expired"));
    return () => setSessionExpiredHandler(null);
  }, [signOut]);

  return null;
};

export default SessionGuard;
