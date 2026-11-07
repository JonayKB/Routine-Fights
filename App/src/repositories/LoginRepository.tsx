import RNSecureKeyStore, { ACCESSIBLE } from "react-native-secure-key-store";
import { gql } from "../utils/graphql";

export const login = async (email: string, password: string) => {
  const data = await gql(
    `mutation Login($email: String!, $password: String!) {
      login(email: $email, password: $password)
    }`,
    { email, password },
    { auth: false }
  );
  await RNSecureKeyStore.set("token", data.login, {
    accessible: ACCESSIBLE.ALWAYS_THIS_DEVICE_ONLY,
  });
  return await RNSecureKeyStore.get("token");
};

export const getToken = async (): Promise<string | null> => {
  try {
    return await RNSecureKeyStore.get("token");
  } catch {
    return null;
  }
};
