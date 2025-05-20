import axios from "axios";
import { CommonData } from "../utils/CommonData";
import CommunityEvent from "../models/CommunityEvent";
import Activity from "../models/Activity";
import Badge from "../models/Badge";
import { ImageRepository } from "./ImageRepository";

export class CommunityEventRepository {
  static async uploadBadge(
    token: string,
    id: string,
    file: File,
    level: number
  ) {
    const imageResponse = await ImageRepository.uploadImage(token, file);
    if (!imageResponse) {
      throw new Error("Image upload failed");
    }
    try {
      console.log("Image response:", imageResponse);
      console.log("Token:", token);
      console.log("ID:", id);
      console.log("Level:", level);
      const response = await axios.post(
        CommonData.BASE_URL + "graphql",
        {
          query: `mutation {
    createBadge(communityEventId: "${id}", image: "${imageResponse}", level: ${level}) {
      id
      level
      image
    }
  }`,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      return response.data.data.createBadge as Badge;
    } catch (error) {
      console.error("Error creating badge:", error);
      throw new Error("Badge creation failed");
    }
  }
  static async getAllEvents(token: string) {
    const response = await axios.post(
      CommonData.BASE_URL + "graphql",
      {
        query: `query{
  getAllCommunityEvents {
    finishDate
    id
    image
    name
    startDate
    totalRequired
  }}`,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    return response.data.data.getAllCommunityEvents as [CommunityEvent];
  }

  static async createEvent(
    token: string,
    event: CommunityEvent,
    activitiesIDs: string[]
  ) {
    const response = await axios.post(
      CommonData.BASE_URL + "graphql",
      {
        query: `mutation{
      createCommunityEvent(
       activitiesIDs: [${activitiesIDs.map((id) => `"${id}"`).join(",")}],
        finishDate: "${event.finishDate.toISOString().slice(0, -1)}",
        id: "${event.id}",
        image: "${event.image}",
        name: "${event.name}",
        startDate: "${event.startDate.toISOString().slice(0, -1)}",
        totalRequired: ${event.totalRequired}
      ) {
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
    return response.data.data.createCommunityEvent as CommunityEvent;
  }

  static async getActivities(token: string) {
    const response = await axios.post(
      CommonData.BASE_URL + "graphql",
      {
        query: `query{
  getAllActivities {
    id
    name
      }
}`,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    return response.data.data.getAllActivities as [Activity];
  }

  static async getBadges(token: string, eventId: string) {
    try {
      const response = await axios.post(
        CommonData.BASE_URL + "graphql",
        {
          query: `query {
    findBadgeByCommunityEvent(communityEventId: "${eventId}") {
      id
      image
      level
    }
  }`,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      return response.data.data.findBadgeByCommunityEvent as [Badge];
    } catch (error) {
      console.error("Error fetching badges:", error);
      throw new Error("Failed to fetch badges");
    }
  }
}
