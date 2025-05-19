import axios from "axios";
import { CommonData } from "../utils/CommonData";
import CommunityEvent from "../models/CommunityEvent";

export class CommunityEventRepository {
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
}
