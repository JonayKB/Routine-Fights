package es.iespuertodelacruz.routinefights.shared.graphql;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;

import es.iespuertodelacruz.routinefights.shared.dto.ChartData;
import es.iespuertodelacruz.routinefights.shared.services.GraphService;

/** Admin dashboard charts (replaces GET /graphs/**). */
@Controller
public class ChartResolver {

    private final GraphService graphService;

    public ChartResolver(GraphService graphService) {
        this.graphService = graphService;
    }

    @Secured("ROLE_ADMIN")
    @QueryMapping("userCreationChart")
    @Cacheable("userCreationChart")
    public ChartData userCreationChart() {
        return graphService.findUserRegistrationsByDate();
    }

    @Secured("ROLE_ADMIN")
    @QueryMapping("postCreationChart")
    @Cacheable("postCreationChart")
    public ChartData postCreationChart() {
        return graphService.findPostsCreatedByDate();
    }

    @Secured("ROLE_ADMIN")
    @QueryMapping("postPointsChart")
    @Cacheable("postPointsChart")
    public ChartData postPointsChart() {
        return graphService.findPointsAddedSumByDate();
    }

    @Secured("ROLE_ADMIN")
    @QueryMapping("userPointsChart")
    @Cacheable("userPointsChart")
    public ChartData userPointsChart() {
        return graphService.findTotalPointsByUser();
    }

    @Secured("ROLE_ADMIN")
    @QueryMapping("activityTimeRateChart")
    @Cacheable("activityTimeRateChart")
    public ChartData activityTimeRateChart() {
        return graphService.findActivitiesByTimeRate();
    }
}
