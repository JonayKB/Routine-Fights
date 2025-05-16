package es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.primary.v2.controllers;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import es.iespuertodelacruz.routinefights.communityEvent.domain.ports.primary.ICommunityEventService;
import es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.primary.v2.dtos.CommunityEventOutputV2;
import es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.primary.v2.mappers.CommunityEventOutputV2Mapper;

@Controller
@CrossOrigin
public class CommunityEventV2Controller {
    private ICommunityEventService communityEventService;
    private CommunityEventOutputV2Mapper communityEventOutputV2Mapper;

    public CommunityEventV2Controller(ICommunityEventService communityEventService,
            CommunityEventOutputV2Mapper communityEventOutputV2Mapper) {
        this.communityEventService = communityEventService;
        this.communityEventOutputV2Mapper = communityEventOutputV2Mapper;
    }

    @Secured({ "ROLE_USER","ROLE_ADMIN" })
    @QueryMapping("getActiveCommunityEvents")
    public List<CommunityEventOutputV2> getActiveCommunityEvents() {
        return communityEventOutputV2Mapper.toDto(communityEventService.getActiveCommunityEvent());
    }

    @Secured({ "ROLE_USER","ROLE_ADMIN" })
    @QueryMapping("getNearestCommunityEvent")
    public CommunityEventOutputV2 getNearestCommunityEvent() {
        return communityEventOutputV2Mapper.toDto(communityEventService.getNearestCommunityEvent());
    }

    @Secured({ "ROLE_USER","ROLE_ADMIN" })
    @QueryMapping("getCommunityEventPointsById")
    public Integer getCommunityEventPointsById(@Argument String id) {
        return communityEventService.getCommunityEventPointsById(id);
    }
}
