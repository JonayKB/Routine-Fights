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

    private final Neo4jClient neo4jClient;

    public GraphService(Neo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }

    public ChartData findAllUsersCreatedAt() {
        String cypher = """
                MATCH (u:User)
                RETURN date(u.createdAt) AS createdAt, count(u) AS total
                ORDER BY createdAt
                """;

        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();

        Collection<Map<String, Object>> rows = neo4jClient
                .query(cypher)
                .fetch()
                .all();

        for (Map<String, Object> row : rows) {
            LocalDate createdAt = (LocalDate) row.get("createdAt");
            Number total = (Number) row.get("total");

            labels.add(createdAt.toString());
            data.add(total.longValue());
        }

        return new ChartData(labels, data);
    }

}
