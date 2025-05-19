import { BarChart } from '@mui/x-charts/BarChart';
import GraphData from "../models/GraphData";
import styles from "../styles/Styles";

type GraphCardProps = {
    title: string;
    data?: GraphData;
};

const GraphCard = ({ title, data }: GraphCardProps) => (
    <div style={styles.graphCard}>
        <h2>{title}</h2>
        <BarChart
            series={[{ data: data?.data ?? [] }]}
            xAxis={[{
                scaleType: "band", data: data?.labels ?? [], colorMap: {
                    type: 'ordinal',
                    colors: ['#B39DDB', '#FF7043']
                }
            }]}

            height={300}
        />
    </div>
);
export default GraphCard;