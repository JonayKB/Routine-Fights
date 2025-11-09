package es.iespuertodelacruz.routinefights.community_event.infrastructure.adapters.primary.v3.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import es.iespuertodelacruz.routinefights.community_event.domain.ports.primary.ICommunityEventService;
import es.iespuertodelacruz.routinefights.community_event.infrastructure.adapters.primary.v3.dtos.CommunityEventOutputV3;
import es.iespuertodelacruz.routinefights.community_event.infrastructure.adapters.primary.v3.mappers.ICommunityEventOutputV3Mapper;
import es.iespuertodelacruz.routinefights.shared.services.NotificationsService;

@Controller
@CrossOrigin
public class CommunityEventV3Controller {
    private ICommunityEventService communityEventService;
    private ICommunityEventOutputV3Mapper communityEventOutputV3Mapper;
    private NotificationsService notificationsService;

    public CommunityEventV3Controller(ICommunityEventService communityEventService,
            ICommunityEventOutputV3Mapper communityEventOutputV3Mapper,
            NotificationsService notificationsService) {
        this.communityEventService = communityEventService;
        this.communityEventOutputV3Mapper = communityEventOutputV3Mapper;
        this.notificationsService = notificationsService;
    }

    @Secured("ROLE_ADMIN")
    @MutationMapping("createCommunityEvent")
    public CommunityEventOutputV3 createCommunityEvent(
            @Argument String name,
            @Argument Integer totalRequired,
            @Argument String startDate,
            @Argument String finishDate,
            @Argument String image,
            @Argument("activitiesIDs") List<String> activitiesIDs) {

        CommunityEventOutputV3 communityEventOutputV3 = communityEventOutputV3Mapper
                .toDto(communityEventService.createCommunityEvent(
                        name,
                        totalRequired,
                        LocalDateTime.parse(startDate),
                        LocalDateTime.parse(finishDate),
                        image,
                        activitiesIDs));
        notificationsService.sendToAllUsers(
                "newEventTitle",
                "newEventBody",
                Map.of("eventName", name));

        return communityEventOutputV3;
    }

    @Secured("ROLE_ADMIN")
    @QueryMapping("getCommunityEventById")
    public CommunityEventOutputV3 getCommunityEventById(
            @Argument String id) {
        return communityEventOutputV3Mapper.toDto(communityEventService.getCommunityEventById(id));
    }

    @Secured("ROLE_ADMIN")
    @QueryMapping("getUsersParticipatingInCommunityEvent")
    public List<String> getUsersParticipatingInCommunityEvent(
            @Argument String id) {
        return communityEventService.getUsersParticipatingInCommunityEvent(id);
    }

    @Secured("ROLE_ADMIN")
    @QueryMapping("getAllCommunityEvents")
    public List<CommunityEventOutputV3> getAllCommunityEvents() {
        return communityEventOutputV3Mapper.toDto(communityEventService.findAll());
    }

}
