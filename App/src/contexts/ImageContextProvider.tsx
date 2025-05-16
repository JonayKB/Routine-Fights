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
  width: number;
  setWidth: Dispatch<SetStateAction<number>>;
  height: number;
  setHeight: Dispatch<SetStateAction<number>>;
}

export const ImageContext = createContext<ImageContextType>(
  {} as ImageContextType
);

const ImageContextProvider = (props: any) => {
  const [uri, setUri] = useState<string>("");
  const [width, setWidth] = useState<number>(9);
  const [height, setHeight] = useState<number>(16);

  const contextValues: ImageContextType = {
    width,
    setWidth,
    height,
    setHeight,
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
