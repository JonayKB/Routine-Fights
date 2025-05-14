package es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.primary.v3.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import es.iespuertodelacruz.routinefights.communityEvent.domain.ports.primary.ICommunityEventService;
import es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.primary.v3.dtos.CommunityEventOutputV3;
import es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.primary.v3.mappers.ICommunityEventOutputV3Mapper;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v3.dtos.UserOutputDTOV3;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v3.mappers.UserOutputV3Mapper;

@Controller
@CrossOrigin
public class CommunityEventV3Controller {
    private ICommunityEventService communityEventService;
    private ICommunityEventOutputV3Mapper communityEventOutputV3Mapper;
    private UserOutputV3Mapper userOutputV3Mapper;

    public CommunityEventV3Controller(ICommunityEventService communityEventService,
            ICommunityEventOutputV3Mapper communityEventOutputV3Mapper, UserOutputV3Mapper userOutputV3Mapper) {
        this.communityEventService = communityEventService;
        this.communityEventOutputV3Mapper = communityEventOutputV3Mapper;
        this.userOutputV3Mapper = userOutputV3Mapper;
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

        return communityEventOutputV3Mapper.toDto(communityEventService.createCommunityEvent(
                name,
                totalRequired,
                LocalDateTime.parse(startDate),
                LocalDateTime.parse(finishDate),
                image,
                activitiesIDs));
    }

    @Secured("ROLE_ADMIN")
    @QueryMapping("getCommunityEventById")
    public CommunityEventOutputV3 getCommunityEventById(
            @Argument String id) {
        return communityEventOutputV3Mapper.toDto(communityEventService.getCommunityEventById(id));
    }

    @Secured("ROLE_ADMIN")
    @QueryMapping("getUsersParticipatingInCommunityEvent")
    public List<UserOutputDTOV3> getUsersParticipatingInCommunityEvent(
            @Argument String id) {
        return userOutputV3Mapper.toDTO(communityEventService.getUsersParticipatingInCommunityEvent(id));
    }

}
