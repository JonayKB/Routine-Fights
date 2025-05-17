package es.iespuertodelacruz.routinefights.shared.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.Neo4jClient.RecordFetchSpec;
import org.springframework.data.neo4j.core.Neo4jClient.UnboundRunnableSpec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
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
    void testFindAllUsersCreatedAt() {

        Collection<Map<String, Object>> mockResult = List.of(
                Map.of("registeredAt", LocalDate.of(2023, 1, 1), "total", 5),
                Map.of("registeredAt", LocalDate.of(2023, 1, 2), "total", 10));

        when(neo4jClient.query(anyString())).thenReturn(unboundRunnableSpec);
        when(unboundRunnableSpec.fetch()).thenReturn(recordFetchSpec);
        when(recordFetchSpec.all()).thenReturn(mockResult);

        ChartData chartData = graphService.findUserRegistrationsByDate();

        assertEquals(List.of("2023-01-01", "2023-01-02"), chartData.getLabels());
        assertEquals(List.of(5L, 10L), chartData.getData());
    }
    @Test
    void testFindPostsCreatedByDate() {
        Collection<Map<String, Object>> mockResult = List.of(
                Map.of("createdAt", LocalDate.of(2023, 1, 1), "total", 5),
                Map.of("createdAt", LocalDate.of(2023, 1, 2), "total", 10));

        when(neo4jClient.query(anyString())).thenReturn(unboundRunnableSpec);
        when(unboundRunnableSpec.fetch()).thenReturn(recordFetchSpec);
        when(recordFetchSpec.all()).thenReturn(mockResult);

        ChartData chartData = graphService.findPostsCreatedByDate();

        assertEquals(List.of("2023-01-01", "2023-01-02"), chartData.getLabels());
        assertEquals(List.of(5L, 10L), chartData.getData());
    }
    @Test
    void testFindPointsAddedSumByDate() {
        Collection<Map<String, Object>> mockResult = List.of(
                Map.of("createdAt", LocalDate.of(2023, 1, 1), "totalPoints", 5),
                Map.of("createdAt", LocalDate.of(2023, 1, 2), "totalPoints", 10));

        when(neo4jClient.query(anyString())).thenReturn(unboundRunnableSpec);
        when(unboundRunnableSpec.fetch()).thenReturn(recordFetchSpec);
        when(recordFetchSpec.all()).thenReturn(mockResult);

        ChartData chartData = graphService.findPointsAddedSumByDate();

        assertEquals(List.of("2023-01-01", "2023-01-02"), chartData.getLabels());
        assertEquals(List.of(5L, 10L), chartData.getData());
    }
    @Test
    void testFindActivitiesByTimeRate() {
        Collection<Map<String, Object>> mockResult = List.of(
                Map.of("rate", 1, "total", 5),
                Map.of("rate", 2, "total", 10));

        when(neo4jClient.query(anyString())).thenReturn(unboundRunnableSpec);
        when(unboundRunnableSpec.fetch()).thenReturn(recordFetchSpec);
        when(recordFetchSpec.all()).thenReturn(mockResult);

        ChartData chartData = graphService.findActivitiesByTimeRate();

        assertEquals(List.of("1", "2"), chartData.getLabels());
        assertEquals(List.of(5L, 10L), chartData.getData());
    }
    @Test
    void testFindTotalPointsByUser() {
        Collection<Map<String, Object>> mockResult = List.of(
                Map.of("user", "user1", "totalPoints", 5),
                Map.of("user", "user2", "totalPoints", 10));

        when(neo4jClient.query(anyString())).thenReturn(unboundRunnableSpec);
        when(unboundRunnableSpec.fetch()).thenReturn(recordFetchSpec);
        when(recordFetchSpec.all()).thenReturn(mockResult);

        ChartData chartData = graphService.findTotalPointsByUser();

        assertEquals(List.of("user1", "user2"), chartData.getLabels());
        assertEquals(List.of(5L, 10L), chartData.getData());
    }
    @Test
    void testExecuteQuerry() {
        Collection<Map<String, Object>> mockResult = List.of(
                Map.of("label", "label1", "data", 5),
                Map.of("label", "label2", "data", 10));

        when(neo4jClient.query(anyString())).thenReturn(unboundRunnableSpec);
        when(unboundRunnableSpec.fetch()).thenReturn(recordFetchSpec);
        when(recordFetchSpec.all()).thenReturn(mockResult);

        ChartData chartData = graphService.executeQuerry("MATCH (n) RETURN n", "label", "data");

        assertEquals(List.of("label1", "label2"), chartData.getLabels());
        assertEquals(List.of(5L, 10L), chartData.getData());
    }

    
}
