
package es.iespuertodelacruz.routinefights.shared.soap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import es.iespuertodelacruz.routinefights.shared.dto.ChartData;
import es.iespuertodelacruz.routinefights.shared.services.GraphService;

public class GraphSOAPImplTest {

    private static final ChartData MOCK_CHART_DATA = new ChartData();
    private GraphSOAPImpl graphSOAPImpl;

    @Mock
    private GraphService graphService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        graphSOAPImpl = new GraphSOAPImpl(graphService);
        
        
    }

    @Test
    public void testGetUserCreationChart() {
        when(graphService.findUserRegistrationsByDate()).thenReturn(MOCK_CHART_DATA);

        ChartData result = graphSOAPImpl.getUserCreationChart();

        assertNotNull(result);
        assertEquals(MOCK_CHART_DATA, result);
    }

    @Test
    public void testGetPostCreationChart() {
        when(graphService.findPostsCreatedByDate()).thenReturn(MOCK_CHART_DATA);

        ChartData result = graphSOAPImpl.getPostCreationChart();

        assertNotNull(result);
        assertEquals(MOCK_CHART_DATA, result);
    }

    @Test
    public void testGetPostPointsChart() {
        when(graphService.findPointsAddedSumByDate()).thenReturn(MOCK_CHART_DATA);

        ChartData result = graphSOAPImpl.getPostPointsChart();

        assertNotNull(result);
        assertEquals(MOCK_CHART_DATA, result);
    }

    @Test
    public void testGetUserPointsChart() {
        when(graphService.findTotalPointsByUser()).thenReturn(MOCK_CHART_DATA);

        ChartData result = graphSOAPImpl.getUserPointsChart();

        assertNotNull(result);
        assertEquals(MOCK_CHART_DATA, result);
    }
}
