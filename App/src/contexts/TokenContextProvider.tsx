import React, {
  createContext,
  Dispatch,
  SetStateAction,
  useContext,
  useState,
} from "react";

export interface AppContextType {
  token: string;
  setToken: Dispatch<SetStateAction<string>>;
  email: string;
  setEmail: Dispatch<SetStateAction<string>>;
}

export const AppContext = createContext<AppContextType>({} as AppContextType);

const TokenContextProvider = (props: any) => {
  const [token, setToken] = useState<string>("");
  const [email, setEmail] = useState<string>("");

  const contextValues: AppContextType = {
    token,
    setToken,
    email,
    setEmail,
  };

  return (
    <AppContext.Provider value={contextValues}>
      {props.children}
    </AppContext.Provider>
  );
};

export const useTokenContext = () => {
  return useContext(AppContext);
};

export default TokenContextProvider;
