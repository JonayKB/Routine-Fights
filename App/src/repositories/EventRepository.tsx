import RNSecureKeyStore from "react-native-secure-key-store";
import { neo4jUri } from "../utils/Utils";
import axios from "axios";
import { Event } from "../utils/Event";

export const getNearestEvent = async (): Promise<Event> => {
  try {
    const token = await RNSecureKeyStore.get("token");
    const response = await axios.post(
      neo4jUri,
      {
        query: `query {
                    getNearestCommunityEvent {
                        finishDate
                        id
                        image
                        name
                        startDate
                        totalRequired
                    }
                }`,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    return response.data.data.getNearestCommunityEvent;
  } catch (error) {
    console.error("Error fetching nearest event:", error);
    throw error;
  }
};
