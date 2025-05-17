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

export const postComment = async (message: string, postID: string) => {
  try {
    const token = await RNSecureKeyStore.get("token");

    const response = await axios.post(
      neo4jUri,
      {
        query: `
            mutation {
              postComment(
                commentInput: {message: "${message}", postID: "${postID}"}
              ) {
                id
              }
            }`,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    return response.data.data.postComment;
  } catch (error) {
    console.error("Error posting comment:", error);
    throw error;
  }
};
