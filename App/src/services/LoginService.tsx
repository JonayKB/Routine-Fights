import axios from "axios";
import RNSecureKeyStore, { ACCESSIBLE } from "react-native-secure-key-store";
import { uri } from "../utils/Utils";

export const login = async (email: string, password: string) => {
  try {
    const { status, data } = await axios.post(
      uri + "/auth/login?email=" + email + "&password=" + password
    );

    if (status === 200) {
      RNSecureKeyStore.set("token", data, {
        accessible: ACCESSIBLE.ALWAYS_THIS_DEVICE_ONLY,
      });
    }
  } catch (error) {
    throw new error("Error", error.response.data);
  }
};

export const getToken = async (): Promise<string> => {
  return await RNSecureKeyStore.get("token");
};
