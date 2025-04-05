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
}

export const AppContext = createContext<AppContextType>({} as AppContextType);

const LanguageContextProvider = (props: any) => {
  const [language, setLanguage] = useState<string>("");

  const contextValues: AppContextType = {
    language: language,
    setLanguage: setLanguage,
  };

  return (
    <AppContext.Provider value={contextValues}>
      {props.children}
    </AppContext.Provider>
  );
};

export const useLanguageContext = () => {
  return useContext(AppContext);
};

export default LanguageContextProvider;
