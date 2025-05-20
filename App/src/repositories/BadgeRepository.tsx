import axios from "axios";
import { Badge } from "../utils/Badge";
import RNSecureKeyStore from "react-native-secure-key-store";
import { neo4jUri } from "../utils/Utils";

export const getBadgesByEmail = async (userEmail: string): Promise<Badge[]> => {
  try {
    const token = await RNSecureKeyStore.get("token");

    const response = await axios.post(
      neo4jUri,
      {
        query: `
            query {
                getBadgesByEmail(email: "${userEmail}") {
                    image
                    level
                    id
                    communityEvent {
                        id
                        name
                        totalRequired
                        finishDate
                        image
                        startDate
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

    return response.data.data.getBadgesByEmail;
  } catch (error) {
    console.error("Error:", error.response.data);
    throw error;
  }
};

export const getBadgesByEvent = async (eventId: string): Promise<Badge[]> => {
  try {
    const token = await RNSecureKeyStore.get("token");

    const response = await axios.post(
      neo4jUri,
      {
        query: `
            query {
                findBadgeByCommunityEvent(communityEventId: "${eventId}") {
                    image
                    level
                    id
                    communityEvent {
                        id
                        name
                        totalRequired
                        finishDate
                        image
                        startDate
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

    return response.data.data.findBadgeByCommunityEvent;
  } catch (error) {
    console.error("Error:", error.response.data);
    throw error;
  }
};
