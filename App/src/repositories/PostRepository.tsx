import axios from "axios";
import RNSecureKeyStore from "react-native-secure-key-store";
import { neo4jUri } from "../utils/Utils";

export const uploadPost = async (activityID: string, image: string) => {
  const token = await RNSecureKeyStore.get("token");

  try {
    const response = await axios.post(
      neo4jUri,
      {
        query: `mutation {
                  uploadPost(activityID: "${activityID}", image: "${image}") {
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

    return response.data.data.uploadPost;
  } catch (error) {
    throw new error("Error uploading post", error.response.data);
  }
};
