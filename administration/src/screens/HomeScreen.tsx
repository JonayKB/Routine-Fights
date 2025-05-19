import React, { useContext, useEffect, useState } from "react";
import { MainContext } from "../components/MainContext";
import GraphData from "../models/GraphData";
import styles from "../styles/Styles";
import GraphCard from "../components/GraphCard";
import { GraphRepository } from "../repositories/GraphRepository";

type Props = {};

const HomeScreen = (props: Props) => {
  const { token } = useContext(MainContext);
  const [pointsPerUserGraph, setPointsPerUserGraph] = useState<GraphData>();
  const [usersCreationGraph, setUsersCreationGraph] = useState<GraphData>();
  const [postsCreationGraph, setPostsCreationGraph] = useState<GraphData>();
  const [activitiesTimeRateGraph, setActivitiesTimeRateGraph] = useState<GraphData>();
  const [lastGraphDate, setLastGraphDate] = useState(new Date());

  useEffect(() => {
    if (!token) {
      window.location.href = "/login";
    }



    GraphRepository.getGraphsData(
      token,
      setUsersCreationGraph,
      setPostsCreationGraph,
      setActivitiesTimeRateGraph,
      setPointsPerUserGraph
    );

  }, [token]);

  return (
    <div style={styles.page}>
      <div style={styles.container}>
        <div style={styles.header}>
          <h1>Graphs of {lastGraphDate.toDateString()} at 00:00</h1>
        </div>

        <div style={styles.graphGrid}>
          <GraphCard title="Users Creation" data={usersCreationGraph} />
          <GraphCard title="Posts Creation" data={postsCreationGraph} />
          <GraphCard title="Activities Time Rate" data={activitiesTimeRateGraph} />
          <GraphCard title="Points Per User" data={pointsPerUserGraph} />
        </div>
      </div>
    </div>
  );
};





export default HomeScreen;