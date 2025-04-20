package es.iespuertodelacruz.routinefights.shared.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import es.iespuertodelacruz.routinefights.shared.dto.ChartData;
import es.iespuertodelacruz.routinefights.shared.services.GraphService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = GraphController.class)
class GraphControllerTest {

    @MockitoBean
    private GraphService graphService;
    @Autowired
    private GraphController graphController;

    @Test
    void findAllUsersCreatedAtTest() {
        ChartData chartData = new ChartData();
        chartData.addLabel("2023-01-01");
        chartData.addData(1L);

        when(graphService.findUserRegistrationsByDate()).thenReturn(chartData);
        ResponseEntity<?> response = graphController.getUserCreationChart();
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(chartData, response.getBody());
    }

    @Test
    void findAllUsersCreatedAtExceptionTest() {
        when(graphService.findUserRegistrationsByDate()).thenThrow(new RuntimeException("Error"));
        ResponseEntity<?> response = graphController.getUserCreationChart();
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        assertEquals("Error", response.getBody());
    }
    
    @Test
    void findPostsCreatedByDateTest() {
        ChartData chartData = new ChartData();
        chartData.addLabel("2023-01-01");
        chartData.addData(1L);

        when(graphService.findPostsCreatedByDate()).thenReturn(chartData);
        ResponseEntity<?> response = graphController.getPostCreationChart();
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(chartData, response.getBody());
    }
    @Test
    void findPostsCreatedByDateExceptionTest() {
        when(graphService.findPostsCreatedByDate()).thenThrow(new RuntimeException("Error"));
        ResponseEntity<?> response = graphController.getPostCreationChart();
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        assertEquals("Error", response.getBody());
    }
    @Test
    void findPointsAddedSumByDateTest() {
        ChartData chartData = new ChartData();
        chartData.addLabel("2023-01-01");
        chartData.addData(1L);

        when(graphService.findPointsAddedSumByDate()).thenReturn(chartData);
        ResponseEntity<?> response = graphController.getPostPointsChart();
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(chartData, response.getBody());
    }
    @Test
    void findPointsAddedSumByDateExceptionTest() {
        when(graphService.findPointsAddedSumByDate()).thenThrow(new RuntimeException("Error"));
        ResponseEntity<?> response = graphController.getPostPointsChart();
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        assertEquals("Error", response.getBody());
    }
    @Test
    void findActivitiesByTimeRateTest() {
        ChartData chartData = new ChartData();
        chartData.addLabel("2023-01-01");
        chartData.addData(1L);

        when(graphService.findActivitiesByTimeRate()).thenReturn(chartData);
        ResponseEntity<?> response = graphController.getActivityTimeRate();
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(chartData, response.getBody());
    }
    @Test
    void findActivitiesByTimeRateExceptionTest() {
        when(graphService.findActivitiesByTimeRate()).thenThrow(new RuntimeException("Error"));
        ResponseEntity<?> response = graphController.getActivityTimeRate();
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        assertEquals("Error", response.getBody());
    }
    @Test
    void findTotalPointsByUserTest() {
        ChartData chartData = new ChartData();
        chartData.addLabel("2023-01-01");
        chartData.addData(1L);

        when(graphService.findTotalPointsByUser()).thenReturn(chartData);
        ResponseEntity<?> response = graphController.getUsersTotalPoints();
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(chartData, response.getBody());
    }
    @Test
    void findTotalPointsByUserExceptionTest() {
        when(graphService.findTotalPointsByUser()).thenThrow(new RuntimeException("Error"));
        ResponseEntity<?> response = graphController.getUsersTotalPoints();
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        assertEquals("Error", response.getBody());
    }
    
    
}
