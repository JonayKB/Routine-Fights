import axios from "axios";
import { limit, neo4jUri } from "../utils/Utils";
import RNSecureKeyStore from "react-native-secure-key-store";
import { Followers } from "../utils/User";

export const fetchUsersByName = async (pageNum: number, text: string, perPage: number = limit): Promise<Followers[]> => {
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
                getUserPaginationByName(page: ${pageNum}, perPage: ${perPage}, userName: "${text}") {
                    followers
                    following
                    id
                    image
                    username
                    email
                    isFollowing
                }
            }`,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    return response.data.data.getUserPaginationByName;
  } catch (error) {
    throw new Error(error.response.data);
  }
};
