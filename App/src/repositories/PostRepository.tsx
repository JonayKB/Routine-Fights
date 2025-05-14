import axios from "axios";
import RNSecureKeyStore from "react-native-secure-key-store";
import { neo4jUri } from "../utils/Utils";
import { Post } from "../utils/Post";

export const getPostsFollowing = async (
  lastDate: string,
  limit: number = 10
): Promise<Post> => {
  try {
    const token = await RNSecureKeyStore.get("token");

    const response = await axios.post(
      neo4jUri,
      {
        query: `query {
                  postsFollowingV2(lastDate: "${lastDate}", limit: "${limit}") {
                    id
                    image
                    updatedAt
                    user {
                      email
                      image
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

    return response.data.data.getPostsFollowing;
  } catch (error) {
    throw new Error("Error fetching posts");
  }
};

export const getPosts = async (
  lastDate: string,
  limit: number = 10
): Promise<Post> => {
  try {
    const token = await RNSecureKeyStore.get("token");

    const response = await axios.post(
      neo4jUri,
      {
        query: `query {
                  postsV2(lastDate: "${lastDate}", limit: "${limit}") {
                    id
                    image
                    updatedAt
                    user {
                      email
                      image
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

    return response.data.data.postsV2;
  } catch (error) {
    throw new Error("Error fetching posts");
  }
};

export const uploadPost = async (activityID: string, image: string) => {
  try {
    const token = await RNSecureKeyStore.get("token");

    console.log(activityID, image);

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
    console.error("Error uploading post:", error);
    throw new Error("Error uploading post");
  }
};
