import axios from "axios";
import { neo4jUri } from "../utils/Utils";
import RNSecureKeyStore from "react-native-secure-key-store";
import { Followers } from "../utils/User";

export const fetchUsersByName = async (text: string): Promise<Followers[]> => {
  if (!text) {
    return [];
  }

  const token = await RNSecureKeyStore.get("token");

  try {
    const response = await axios.post(
      neo4jUri,
      {
        query: `
            query {
                usersV2(regex: "${text}") {
                    createdAt
                    followers
                    following
                    id
                    image
                    nationality
                    username
                }
            }`,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    return response.data.data.usersV2;
  } catch (error) {
    throw new Error(error.response.data);
  }
};
