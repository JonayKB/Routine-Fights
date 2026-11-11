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
      chart: string,
      state: React.Dispatch<React.SetStateAction<GraphData | undefined>>
    ) => {
      try {
        const response = await axios.post(
          CommonData.BASE_URL + "graphql",
          { query: `query { ${chart} { labels data } }` },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        if (response.data.errors?.length) {
          throw new Error(response.data.errors[0].message);
        }
        state(response.data.data[chart] as GraphData);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };

    await getGraphData("userCreationChart", setUsersCreationGraph);
    await getGraphData("postCreationChart", setPostsCreationGraph);
    await getGraphData("activityTimeRateChart", setActivitiesTimeRateGraph);
    await getGraphData("userPointsChart", setPointsPerUserGraph);
  }
}
