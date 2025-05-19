import React, {
  createContext,
  Dispatch,
  SetStateAction,
  useContext,
  useState,
} from "react";

export interface AppContextType {
  language: string;
  setLanguage: Dispatch<SetStateAction<string>>;
  darkmode: boolean;
  setDarkmode: Dispatch<SetStateAction<boolean>>;
  lefthand: boolean;
  setLefthand: Dispatch<SetStateAction<boolean>>;
}

export const AppContext = createContext<AppContextType>({} as AppContextType);

const SettingsContextProvider = (props: any) => {
  const [language, setLanguage] = useState<string>("en-EN");
  const [darkmode, setDarkmode] = useState<boolean>(true);
  const [lefthand, setLefthand] = useState<boolean>(false);

  const contextValues: AppContextType = {
    language: language,
    setLanguage: setLanguage,
    darkmode: darkmode,
    setDarkmode: setDarkmode,
    lefthand: lefthand,
    setLefthand: setLefthand,
  };

  return (
    <AppContext.Provider value={contextValues}>
      {props.children}
    </AppContext.Provider>
  );
};

export const useSettingsContext = () => {
  return useContext(AppContext);
};

export default SettingsContextProvider;
