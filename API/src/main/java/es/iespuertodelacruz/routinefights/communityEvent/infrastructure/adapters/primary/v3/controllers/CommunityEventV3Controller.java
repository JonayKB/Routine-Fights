package es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.primary.v3.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import es.iespuertodelacruz.routinefights.communityEvent.domain.ports.primary.ICommunityEventService;

@Controller
@CrossOrigin
public class CommunityEventV3Controller {
    private ICommunityEventService communityEventService;

    public CommunityEventV3Controller(ICommunityEventService communityEventService) {
        this.communityEventService = communityEventService;
    }

    @MutationMapping("createCommunityEvent")
    public Boolean createCommunityEvent(
            @Argument String name,
            @Argument Integer totalRequired,
            @Argument String startDate,
            @Argument String finishDate,
            @Argument("activitiesIDs") List<String> activitiesIDs) {

        communityEventService.createCommunityEvent(
                name,
                totalRequired,
                LocalDateTime.parse(startDate),
                LocalDateTime.parse(finishDate),
                activitiesIDs);
        return true;
    }

}
