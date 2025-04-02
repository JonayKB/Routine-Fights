import axios from "axios";
import { uri } from "../utils/Utils";
import RNSecureKeyStore from "react-native-secure-key-store";

export const getImage = async (image: string): Promise<string> => {
  try {
    const response = await axios.get(uri + "/images/" + image, {
      headers: {
        Authorization: `Bearer ${await RNSecureKeyStore.get("token")}`,
      },
    });
    console.log(response.config.url);
    const base64 = Buffer.from(response.data, "binary").toString("base64");
    return `data:image/jpg;base64,${base64}`;
  } catch (error) {
    console.log(error.response.data);
    throw new error("Error", error.response.data);
  }
};
