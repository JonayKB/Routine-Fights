package es.iespuertodelacruz.routinefights.shared.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.Neo4jClient.RecordFetchSpec;
import org.springframework.data.neo4j.core.Neo4jClient.UnboundRunnableSpec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import es.iespuertodelacruz.routinefights.shared.dto.ChartData;

class GraphServiceTest {
    private GraphService graphService;
    @Mock
    private Neo4jClient neo4jClient;
    @Mock
    private UnboundRunnableSpec unboundRunnableSpec;
    @Mock
    private RecordFetchSpec<Map<String, Object>> recordFetchSpec;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        graphService = new GraphService(neo4jClient);
    }

    @Test
    void testFindAllUsersCreatedAtWithValidData() {
        String cypher = """
                MATCH (u:User)
                RETURN date(u.createdAt) AS createdAt, count(u) AS total
                ORDER BY createdAt
                """;

        Collection<Map<String, Object>> mockResult = List.of(
                Map.of("createdAt", LocalDate.of(2023, 1, 1), "total", 5),
                Map.of("createdAt", LocalDate.of(2023, 1, 2), "total", 10));

        when(neo4jClient.query(cypher)).thenReturn(unboundRunnableSpec);
        when(unboundRunnableSpec.fetch()).thenReturn(recordFetchSpec);
        when(recordFetchSpec.all()).thenReturn(mockResult);

        ChartData chartData = graphService.findAllUsersCreatedAt();

        assertEquals(List.of("2023-01-01", "2023-01-02"), chartData.getLabels());
        assertEquals(List.of(5L, 10L), chartData.getData());
    }

    @Test
    void testFindAllUsersCreatedAtWithNoData() {
        String cypher = """
                MATCH (u:User)
                RETURN date(u.createdAt) AS createdAt, count(u) AS total
                ORDER BY createdAt
                """;
        when(neo4jClient.query(cypher)).thenReturn(unboundRunnableSpec);
        when(unboundRunnableSpec.fetch()).thenReturn(recordFetchSpec);
        when(recordFetchSpec.all()).thenReturn(List.of());

        ChartData chartData = graphService.findAllUsersCreatedAt();

        assertEquals(List.of(), chartData.getLabels());
        assertEquals(List.of(), chartData.getData());
    }

    
}
