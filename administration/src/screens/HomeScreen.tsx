import React, { useContext, useEffect } from "react";
import { MainContext } from "../components/MainContext";
import { BarChart } from "@mui/x-charts";

type Props = {};

const HomeScreen = (props: Props) => {
  const { token } = useContext(MainContext);

  useEffect(() => {
    if (!token) {
      window.location.href = "/login";
    }
  }, [token]);

  return (
    <div>
      @ts-ignore
      <BarChart
        series={[
          { data: [2, 5, 3] }
        ]}
        xAxis={[{ scaleType: "band", data: ["A", "B", "C"] }]}
        yAxis={[{ scaleType: "linear" }]}
        height={300}
      />
    </div>
  );
};

export default HomeScreen;
