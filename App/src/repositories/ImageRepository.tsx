import axios from "axios";
import RNFS from "react-native-fs";
import RNSecureKeyStore from "react-native-secure-key-store";
import { uri } from "../utils/Utils";

// const getBinaryImage = async (uri: string): Promise<ArrayBuffer> => {
//   try {
//     const base64: string = await RNFS.readFile(uri, "base64");
//     const binary = Buffer.from(base64, "base64");
//     return binary;
//   } catch (error) {
//     throw new Error("Error reading file");
//   }
// };

export const uploadImage = async (imageUri: string): Promise<string> => {
  try {
    const token = await RNSecureKeyStore.get("token");
    const image = {
      uri: "file://" + imageUri,
      name: new Date().toISOString().replace(/:/g, "-") + ".png",
      type: "image/png",
    };

    const formData = new FormData();
    formData.append("file", image);

    const response = await axios.post(uri + "/images/", formData, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    return response.data;
  } catch (error) {
    console.log("Error uploading image:", error);
    throw new Error("Error uploading image");
  }
};
