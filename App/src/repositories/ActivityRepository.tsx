import axios from "axios";
import { limit, neo4jUri } from "../utils/Utils";
import RNSecureKeyStore from "react-native-secure-key-store";
import { Activity } from "../utils/Activity";

export const getActivitiesNotSubscribed = async (
  page: number,
  name: string,
  perPage: number = limit
): Promise<Activity[]> => {
  try {
    const token = await RNSecureKeyStore.get("token");

    const response = await axios.post(
      neo4jUri,
      {
        query: `
                query {
                  paginationActivitiesNotSubscribed(page: ${page}, perPage: ${perPage}, activityName: "${name}") {
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

    return response.data.data.paginationActivitiesNotSubscribed;
  } catch (error) {
    throw new Error(error.response.data);
  }
};

export const getSubscribedActivities = async () => {
  try {
    const token = await RNSecureKeyStore.get("token");

    const response = await axios.post(
      neo4jUri,
      {
        query: `
                query {
                    getSubscribedActivitiesWithStreakByName(activityName: "") {
                        id,
                        name,
                        description,
                        image,
                        timeRate,
                        timesRequiered,
                        streak
                    }
                }`,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    return response.data.data.getSubscribedActivitiesWithStreakByName;
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

export const createActivity = async (
  name: string,
  description: string,
  timeRate: string,
  timesRequiered: string,
  image: string,
) => {
  try {
    const token = await RNSecureKeyStore.get("token");

    const response = await axios.post(
      neo4jUri,
      {
        query: `
                mutation {
                  createActivity(
                    activityInput: {
                      name: "${name}", 
                      description: "${description}", 
                      image: "${image}", 
                      timeRate: "${timeRate}", 
                      timesRequiered: "${timesRequiered}"
                    } 
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

    return response.data.data.createActivity;
  } catch (error) {
    throw new Error(error.response.data);
  }
};
