package es.iespuertodelacruz.routinefights.shared.soap;

import org.springframework.cache.annotation.Cacheable;

import es.iespuertodelacruz.routinefights.shared.dto.ChartData;
import es.iespuertodelacruz.routinefights.shared.services.GraphService;
import jakarta.jws.WebService;

@WebService(endpointInterface = "es.iespuertodelacruz.routinefights.shared.soap.GraphSOAPInterface")
public class GraphSOAPImpl implements GraphSOAPInterface {
    private GraphService graphService;

    public GraphSOAPImpl(GraphService graphService) {
        this.graphService = graphService;
    }

    @Cacheable("userCreationChart")
    @Override
    public ChartData getUserCreationChart() {
        return graphService.findUserRegistrationsByDate();
    }

    @Cacheable("postCreationChart")
    @Override
    public ChartData getPostCreationChart() {
        return graphService.findPostsCreatedByDate();
    }

    @Cacheable("postPointsChart")
    @Override
    public ChartData getPostPointsChart() {
        return graphService.findPointsAddedSumByDate();
    }

    @Cacheable("userPointsChart")
    @Override
    public ChartData getUserPointsChart() {
        return graphService.findTotalPointsByUser();
    }

}
