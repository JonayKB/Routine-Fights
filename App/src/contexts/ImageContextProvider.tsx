import React, { createContext, Dispatch, SetStateAction, useCallback, useContext, useState } from "react";
import { PickedImage } from "../utils/images";

export interface ImageContextType {
  /** Photo picked and cropped on the device, waiting to be uploaded. */
  picked: PickedImage | null;
  setPicked: Dispatch<SetStateAction<PickedImage | null>>;
  /** Local URI of {@link picked}, or null. */
  uri: string | null;
  /** Forgets the picked photo. */
  clear: () => void;
  /** Aspect ratio the crop editor enforces (width:height). */
  width: number;
  setWidth: Dispatch<SetStateAction<number>>;
  height: number;
  setHeight: Dispatch<SetStateAction<number>>;
}

export const ImageContext = createContext<ImageContextType>({} as ImageContextType);

const ImageContextProvider = (props: any) => {
  const [picked, setPicked] = useState<PickedImage | null>(null);
  const [width, setWidth] = useState<number>(4);
  const [height, setHeight] = useState<number>(5);
  const clear = useCallback(() => setPicked(null), []);

  const contextValues: ImageContextType = {
    picked,
    setPicked,
    uri: picked?.uri ?? null,
    clear,
    width,
    setWidth,
    height,
    setHeight,
  };

  return <ImageContext.Provider value={contextValues}>{props.children}</ImageContext.Provider>;
};

export const useImageContext = () => {
  return useContext(ImageContext);
};

export default ImageContextProvider;
