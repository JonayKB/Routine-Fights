package es.iespuertodelacruz.routinefights.shared.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.shared.dto.ChartData;

@Service
public class GraphService {

    private static final String TOTAL = "total";
    private final Neo4jClient neo4jClient;

    public GraphService(Neo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }

    public ChartData findPostsCreatedByDate() {
        String cypher = """
                MATCH (p:Post)
                RETURN date(p.createdAt) AS createdAt, count(p) AS total
                ORDER BY createdAt
                """;

        return executeQuerry(cypher, "createdAt", TOTAL);
    }

    public ChartData findPointsAddedSumByDate() {
        String cypher = """
                MATCH (p:Post)
                RETURN date(p.createdAt)   AS createdAt,
                       sum(p.pointsToAdd) AS totalPoints
                ORDER BY createdAt
                """;

        return executeQuerry(cypher, "createdAt", "totalPoints");
    }

    public ChartData findActivitiesByTimeRate() {
        String cypher = """
                MATCH (a:Activity)
                RETURN a.timeRate AS rate, count(a) AS total
                ORDER BY rate
                """;

        return executeQuerry(cypher, "rate", TOTAL);
    }

    public ChartData findTotalPointsByUser() {
        String cypher = """
                MATCH (u:User)-[:Posted]->(p:Post)
                RETURN u.username         AS user,
                       sum(p.pointsToAdd) AS totalPoints
                ORDER BY totalPoints DESC
                """;

        return executeQuerry(cypher, "user", "totalPoints");
    }

    public ChartData findUserRegistrationsByDate() {
        String cypher = """
                MATCH (u:User)
                RETURN date(u.createdAt) AS registeredAt, count(u) AS total
                ORDER BY registeredAt
                """;

        return executeQuerry(cypher, "registeredAt", TOTAL);
    }

    ChartData executeQuerry(String cypher, String label, String data) {
        List<String> labels = new ArrayList<>();
        List<Long> dataList = new ArrayList<>();

        Collection<Map<String, Object>> rows = neo4jClient
                .query(cypher)
                .fetch()
                .all();

        for (Map<String, Object> row : rows) {
            String labelValue = String.valueOf(row.get(label));
            Number dataValue = (Number) row.get(data);

            labels.add(labelValue);
            dataList.add(dataValue.longValue());
        }

        return new ChartData(labels, dataList);
    }

}
