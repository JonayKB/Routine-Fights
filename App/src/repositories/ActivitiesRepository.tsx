import axios from "axios";
import { neo4jUri } from "../utils/Utils";
import RNSecureKeyStore from "react-native-secure-key-store";

export const getActivities = async (page: number, perPage: number = 10) => {
  try {
    const token = await RNSecureKeyStore.get("token");

    const response = await axios.post(
      neo4jUri,
      {
        query: `
                query {
                  paginationActivitiesV2(page: ${page}, perPage: ${perPage}) {
                    id
                    image
                    name
                    description
                    timeRate
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

    return response.data.data.paginationActivitiesV2;
  } catch (error) {
    throw new Error(error.response.data);
  }
};

export const subscribeActivity = async (activityId: string) => {
  try {
    const token = await RNSecureKeyStore.get("token");

    const response = await axios.post(
      neo4jUri,
      {
        query: `
              mutation {
                subscribeActivity(activityID: "${activityId}")
              }`,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    return response.data.data.subscribeActivity;
  } catch (error) {
    throw new Error(error.response.data);
  }
};

export const unsubscribeActivity = async (activityId: string) => {
  try {
    const token = await RNSecureKeyStore.get("token");

    const response = await axios.post(
      neo4jUri,
      {
        query: `
              mutation {
                unSubscribeActivity(activityID: "${activityId}")
              }`,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    return response.data.data.unSubscribeActivity;
  } catch (error) {
    throw new Error(error.response.data);
  }
};
