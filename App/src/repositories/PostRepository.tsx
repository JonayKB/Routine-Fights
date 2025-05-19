import axios from "axios";
import RNSecureKeyStore from "react-native-secure-key-store";
import { limit, neo4jUri } from "../utils/Utils";
import { Post } from "../utils/Post";

export const getPostsFollowing = async (
  lastDate: string,
  perPage: number = limit
): Promise<Post[]> => {
  try {
    const token = await RNSecureKeyStore.get("token");

    const response = await axios.post(
      neo4jUri,
      {
        query: `query {
                  postsFollowingV2(lastDate: "${lastDate}", limit: ${perPage}) {
                    id
                    image
                    updatedAt
                    createdAt
                    streak
                    comments
                    likes
                    isLiked
                    user {
                      email
                      image
                    }
                    activity {
                      name
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
    console.error("Error fetching posts:", error);
    throw new Error("Error fetching posts");
  }
};

export const getPosts = async (
  lastDate: string,
  perPage: number = limit
): Promise<Post[]> => {
  try {
    const token = await RNSecureKeyStore.get("token");

    const response = await axios.post(
      neo4jUri,
      {
        query: `query {
                  postsV2(lastDate: "${lastDate}", limit: ${perPage}) {
                    id
                    image
                    updatedAt
                    createdAt
                    streak
                    comments
                    likes
                    isLiked
                    user {
                      email
                      image
                    }
                    activity {
                      name
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
    console.error("Error fetching posts:", error);
    throw new Error("Error fetching posts");
  }
};

export const getUserPosts = async (
  lastDate: string,
  userID: string,
  perPage: number = limit
) => {
  try {
    const token = await RNSecureKeyStore.get("token");
    const response = await axios.post(
      neo4jUri,
      {
        query: `query {
                postsByUserV2(lastDate: "${lastDate}", userID: "${userID}", limit: ${perPage}) {
                  id
                  streak
                  comments
                  image
                  likes
                  isLiked
                  updatedAt
                  createdAt
                  user {
                    email
                    image
                  }
                  activity {
                    name
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

    return response.data.data.postsByUserV2;
  } catch (error) {
    console.error("Error fetching user posts:", error);
    throw new Error("Error fetching user posts");
  }
};

export const getPostBySuscribedActivities = async (
  lastDate: string,
  perPage: number = limit
) => {
  try {
    const token = await RNSecureKeyStore.get("token");

    const response = await axios.post(
      neo4jUri,
      {
        query: `query {
                  postsSubscribedActivitiesV2(lastDate: "${lastDate}", limit: ${perPage}) {
                    id
                    image
                    updatedAt
                    createdAt
                    streak
                    comments
                    likes
                    isLiked
                    user {
                      email
                      image
                    }
                    activity {
                      name
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

    return response.data.data.postsSubscribedActivitiesV2;
  } catch (error) {
    console.error("Error fetching posts by activity:", error);
    throw new Error("Error fetching posts by activity");
  }
};

export const uploadPost = async (activityID: string, image: string) => {
  try {
    const token = await RNSecureKeyStore.get("token");

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

export const likePost = async (postID: string) => {
  try {
    const token = await RNSecureKeyStore.get("token");

    const response = await axios.post(
      neo4jUri,
      {
        query: `mutation {
                  likePost(postID: "${postID}")
              }`,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    return response.data.data.likePost;
  } catch (error) {
    console.error("Error liking post:", error);
    throw new Error("Error liking post");
  }
};

export const unLikePost = async (postID: string) => {
  try {
    const token = await RNSecureKeyStore.get("token");

    const response = await axios.post(
      neo4jUri,
      {
        query: `mutation {
                  unLikePost(postID: "${postID}")
              }`,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    return response.data.data.unLikePost;
  } catch (error) {
    console.error("Error unliking post:", error);
    throw new Error("Error unliking post");
  }
};
