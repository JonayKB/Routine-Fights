import React, {
  createContext,
  Dispatch,
  SetStateAction,
  useContext,
  useState,
} from "react";

export interface ImageContextType {
  uri: string;
  setUri: Dispatch<SetStateAction<string>>;
}

export const ImageContext = createContext<ImageContextType>({} as ImageContextType);

const ImageContextProvider = (props: any) => {
  const [uri, setUri] = useState<string>("");

  const contextValues: ImageContextType = {
    uri,
    setUri,
  };

  return (
    <ImageContext.Provider value={contextValues}>
      {props.children}
    </ImageContext.Provider>
  );
};

export const useImageContext = () => {
  return useContext(ImageContext);
};

export default ImageContextProvider;
