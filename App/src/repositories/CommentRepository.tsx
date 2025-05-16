import axios from "axios";
import { neo4jUri } from "../utils/Utils";
import RNSecureKeyStore from "react-native-secure-key-store";

export const getComments = async (postID: string) => {
  try {
    const token = await RNSecureKeyStore.get("token");

    const response = await axios.post(
      neo4jUri,
      {
        query: `
            query {
                getComments(postID: "${postID}") {
                    createdAt
                    id
                    message
                    user {
                        image
                        email
                        username
                    }
                }
            }`,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    return response.data.data.getComments;
  } catch (error) {
    console.error("Error fetching comments:", error);
    throw error;
  }
};
