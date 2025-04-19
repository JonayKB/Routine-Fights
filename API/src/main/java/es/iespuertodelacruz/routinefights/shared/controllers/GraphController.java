package es.iespuertodelacruz.routinefights.shared.controllers;

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
    public ResponseEntity<?> getUserCreationChart() {
        try {
            return ResponseEntity.ok(graphService.findAllUsersCreatedAt());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
}
