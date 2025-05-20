import axios from "axios";
import GraphData from "../models/GraphData";
import { CommonData } from "../utils/CommonData";
import { Dispatch } from "react";

export class GraphRepository {
  static async getGraphsData(
    token: string,
    setUsersCreationGraph: Dispatch<
      React.SetStateAction<GraphData | undefined>
    >,
    setPostsCreationGraph: Dispatch<
      React.SetStateAction<GraphData | undefined>
    >,
    setActivitiesTimeRateGraph: Dispatch<
      React.SetStateAction<GraphData | undefined>
    >,
    setPointsPerUserGraph: Dispatch<React.SetStateAction<GraphData | undefined>>
  ) {
    const getGraphData = async (
      endpoint: string,
      state: React.Dispatch<React.SetStateAction<GraphData | undefined>>
    ) => {
      try {
        const response = await axios.get(
          CommonData.BASE_URL + "graphs/" + endpoint,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        state(response.data as GraphData);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };

    await getGraphData("users/creation", setUsersCreationGraph);
    await getGraphData("posts/creation", setPostsCreationGraph);
    await getGraphData("activities/timerate", setActivitiesTimeRateGraph);
    await getGraphData("users/points", setPointsPerUserGraph);
  }
}
