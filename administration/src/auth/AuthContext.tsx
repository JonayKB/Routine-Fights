import React, { createContext, ReactNode, useCallback, useContext, useEffect, useMemo, useState } from "react";
import { Navigate, useLocation } from "react-router-dom";
import { login as apiLogin } from "../api/admin";
import { ApiError, readToken, roleOf, saveToken, setUnauthorizedHandler } from "../api/client";

type AuthContextType = {
  token: string;
  /** Signs in; only administrators get through. */
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
  /** Set when the session ended on its own (expired, role removed). */
  expired: boolean;
};

const AuthContext = createContext<AuthContextType>({} as AuthContextType);

export const useAuth = () => useContext(AuthContext);

const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [token, setToken] = useState(() => {
    const saved = readToken();
    return roleOf(saved) === "ROLE_ADMIN" ? saved : "";
  });
  const [expired, setExpired] = useState(false);

  const logout = useCallback(() => {
    saveToken("");
    setToken("");
  }, []);

  useEffect(() => {
    setUnauthorizedHandler(() => {
      setExpired(true);
      logout();
    });
    return () => setUnauthorizedHandler(null);
  }, [logout]);

  const login = useCallback(async (email: string, password: string) => {
    const next = await apiLogin(email, password);
    if (roleOf(next) !== "ROLE_ADMIN") {
      throw new ApiError("This account is not an administrator");
    }
    saveToken(next);
    setExpired(false);
    setToken(next);
  }, []);

  const value = useMemo(() => ({ token, login, logout, expired }), [token, login, logout, expired]);
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

/** Sends visitors without an admin session to the login page, then back. */
export const RequireAuth = ({ children }: { children: ReactNode }) => {
  const { token } = useAuth();
  const location = useLocation();
  if (!token) return <Navigate to="/login" replace state={{ from: location.pathname }} />;
  return <>{children}</>;
};

export default AuthProvider;
