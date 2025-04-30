import axios from "axios";
import RNSecureKeyStore from "react-native-secure-key-store";
import { UserOut } from "../utils/User";
import { neo4jUri } from "../utils/Utils";

export const getOwnUser = async (): Promise<UserOut> => {
  try {
    const token = await RNSecureKeyStore.get("token");

    const response = await axios.post(
      neo4jUri,
      {
        query: `
                query {
                    getOwnUser {
                        id
                        username
                        email
                        nationality
                        phoneNumber
                        image
                        createdAt
                        followers
                        following
                    }
                }
            `,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    return response.data.data.getOwnUser;
  } catch (error) {
    throw new error("Error", error.response.data);
  }
};

export const getOwnUserImage = async (): Promise<UserOut> => {
  try {
    const token = await RNSecureKeyStore.get("token");

    const response = await axios.post(
      neo4jUri,
      {
        query: `
                query {
                    getOwnUser {
                        image
                    }
                }
            `,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    return response.data.data.getOwnUser;
  } catch (error) {
    throw new error("Error", error.response.data);
  }
};

export const getUser = async (id: string): Promise<UserOut> => {
  try {
    const response = await axios.post(
      neo4jUri,
      {
        query: `
              query {
                  userV2(id: "${id}") {
                      id
                      username
                      email
                      nationality
                      phoneNumber
                      image
                      createdAt
                      followers
                      following
                  }
              }
            `,
      },
      {
        headers: {
          Authorization: `Bearer ${await RNSecureKeyStore.get("token")}`,
        },
      }
    );
    return response.data.data.userV2;
  } catch (error) {
    throw new error("Error", error.response.data);
  }
};

export const getFollows = async (email: string) => {
  const response = await axios.post(
    neo4jUri,
    {
      query: `
            query {
                followersByEmail(email: "${email}", usernameFilter: "") {
                    id
                    username
                    nationality
                    image
                    createdAt
                    followers
                    following
                    isFollowing
                }
                    
                followedByEmail(email: "${email}", usernameFilter: "") {
                    id
                    username
                    nationality
                    image
                    createdAt
                    followers
                    following
                    isFollowing
                }
            }
        `,
    },
    {
      headers: {
        Authorization: `Bearer ${await RNSecureKeyStore.get("token")}`,
      },
    }
  );

  return response.data.data;
};

export const followUser = async (email: string) => {
  try {
    const token = await RNSecureKeyStore.get("token");

    const response = await axios.post(
      neo4jUri,
      {
        query: `
                mutation {
                    followUser(followingEmail: "${email}") {
                }`,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    return response.data.data.followUser;
  } catch (error) {
    throw new error("Error", error.response.data);
  }
};

export const unfollowUser = async (email: string) => {
  try {
    const token = await RNSecureKeyStore.get("token");

    const response = await axios.post(
      neo4jUri,
      {
        query: `
                mutation {
                    unfollowUser(followingEmail: "${email}") {
                }`,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    return response.data.data.followUser;
  } catch (error) {
    throw new error("Error", error.response.data);
  }
};
