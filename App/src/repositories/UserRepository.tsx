import axios from "axios";
import RNSecureKeyStore from "react-native-secure-key-store";
import { UserIn, UserOut } from "../utils/User";
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

export const getUser = async (email: string): Promise<UserOut> => {
  try {
    const response = await axios.post(
      neo4jUri,
      {
        query: `
              query {
                  getUserV2IsFollowing(email: "${email}") {
                      id
                      username
                      email
                      phoneNumber
                      image
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
    return response.data.data.getUserV2IsFollowing;
  } catch (error) {
    throw new error("Error", error.response.data);
  }
};

export const getFollows = async (email: string, name?: string) => {
  const response = await axios.post(
    neo4jUri,
    {
      query: `
            query {
                followersByEmail(email: "${email}", usernameFilter: "${name}") {
                    id
                    username
                    nationality
                    image
                    createdAt
                    followers
                    following
                    isFollowing
                    email
                }
                    
                followedByEmail(email: "${email}", usernameFilter: "${name}") {
                    id
                    username
                    nationality
                    image
                    createdAt
                    followers
                    following
                    isFollowing
                    email
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
                    followUser(followingEmail: "${email}")
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
                    unfollowUser(followingEmail: "${email}")
                }`,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    return response.data.data.unfollowUser;
  } catch (error) {
    throw new error("Error", error.response.data);
  }
};

export const updateUser = async (user: UserIn): Promise<UserOut> => {
  let password = user.password ? `, password: "${user.password}"` : "";
  try {
    const token = await RNSecureKeyStore.get("token");

    const response = await axios.post(
      neo4jUri,
      {
        query: `
          mutation {
            updateUserV2(
              user: {username: "${user.username}", email: "${user.email}", nationality: "${user.nationality}", phoneNumber: "${user.phoneNumber}" ${password}}
            ) {
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
          }`,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    console.log(response.data);
    return response.data.data.updateUserV2;
  } catch (error) {
    console.error("Error updating user:", error);
  }
};
