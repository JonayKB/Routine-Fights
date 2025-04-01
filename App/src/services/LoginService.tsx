import axios from "axios";
import RNSecureKeyStore, { ACCESSIBLE } from "react-native-secure-key-store";

export const login = async (email: string, password: string) => {
  try {
    const { status, data } = await axios.post(
      "http://64.226.71.234:8080/auth/login?email=" +
        email +
        "&password=" +
        password
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

export const getToken = async () => {
  return await RNSecureKeyStore.get("token");
};
