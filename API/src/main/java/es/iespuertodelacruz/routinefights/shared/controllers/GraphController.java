package es.iespuertodelacruz.routinefights.shared.controllers;


import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.iespuertodelacruz.routinefights.shared.services.GraphService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/graphs")
@CrossOrigin
@Tag(name = "GRAPH", description = "Get graphs")
public class GraphController {

    private final GraphService graphService;

    public GraphController(GraphService graphService) {
        this.graphService = graphService;
    }

    @GetMapping("/users/creation")
    @Secured("ROLE_ADMIN")
    @Cacheable(value = "userCreationChart")
    public ResponseEntity<?> getUserCreationChart() {
        try {
            return ResponseEntity.ok(graphService.findUserRegistrationsByDate());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/posts/creation")
    @Secured("ROLE_ADMIN")
    @Cacheable(value = "postCreationChart")
    public ResponseEntity<?> getPostCreationChart() {
        try {
            return ResponseEntity.ok(graphService.findPostsCreatedByDate());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/posts/points")
    @Secured("ROLE_ADMIN")
    @Cacheable(value = "postPointsChart")
    public ResponseEntity<?> getPostPointsChart() {
        try {
            return ResponseEntity.ok(graphService.findPointsAddedSumByDate());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/users/points")
    @Secured("ROLE_ADMIN")
    @Cacheable(value = "userPointsChart")
    public ResponseEntity<?> getUsersTotalPoints() {
        try {
            return ResponseEntity.ok(graphService.findTotalPointsByUser());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/activities/timerate")
    @Secured("ROLE_ADMIN")
    @Cacheable(value = "activityTimeRateChart")
    public ResponseEntity<?> getActivityTimeRate() {
        try {
            return ResponseEntity.ok(graphService.findActivitiesByTimeRate());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    

}
