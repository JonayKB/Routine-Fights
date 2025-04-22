import axios from "axios";
import { neo4jUri } from "../utils/Utils";
import RNSecureKeyStore from "react-native-secure-key-store";

export const getSubscribedActivities = async () => {
  try {
    const token = await RNSecureKeyStore.get("token");

    const response = await axios.post(
      neo4jUri,
      {
        query: `
                query {
                    getSubscribedActivities {
                        id,
                        name,
                        description,
                        image,
                        timeRate,
                        timesRequiered
                    }
                }`,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    return response.data.data.getSubscribedActivities;
  } catch (error) {
    throw new Error(error.response.data);
  }
};
