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
    // TODO: try with typeORM
    throw new error("Error", error.response.data);
  }
};

export const getUser = async (id: string): Promise<UserOut> => {
  try {
    const response = await axios.post(neo4jUri, {
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
      })
    return response.data.data.userV2;
  } catch (error) {
    // TODO: try with typeORM
    throw new error("Error", error.response.data);
  }
};

export const getFollows = async (email: string) => {
  const response = await axios.post(
    neo4jUri,
    {
      query: `
            query {
                followersByEmail(email: "${email}") {
                    id
                    username
                    nationality
                    image
                    createdAt
                    followers
                    following
                }
                    
                followedByEmail(email: "${email}") {
                    id
                    username
                    nationality
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

  return response.data.data;
};
