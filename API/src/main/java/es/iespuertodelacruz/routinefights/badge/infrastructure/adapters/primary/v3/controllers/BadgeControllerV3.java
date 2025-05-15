package es.iespuertodelacruz.routinefights.badge.infrastructure.adapters.primary.v3.controllers;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import es.iespuertodelacruz.routinefights.badge.domain.ports.primary.IBadgeService;
import es.iespuertodelacruz.routinefights.badge.infrastructure.adapters.primary.v3.dtos.BadgeV3Output;
import es.iespuertodelacruz.routinefights.badge.infrastructure.adapters.primary.v3.mappers.BadgeV3OutputMapper;

@Controller
@CrossOrigin
public class BadgeControllerV3 {
    private IBadgeService badgeService;
    private BadgeV3OutputMapper badgeV3OutputMapper;

    public BadgeControllerV3(IBadgeService badgeService, BadgeV3OutputMapper badgeV3OutputMapper) {
        this.badgeService = badgeService;
        this.badgeV3OutputMapper = badgeV3OutputMapper;
    }

    @Secured("ROLE_ADMIN")
    @MutationMapping("createBadge")
    public BadgeV3Output createBadge(@Argument String image, @Argument Integer level,
            @Argument String communityEventId) {
        return badgeV3OutputMapper.toDTO(
                badgeService.createBadge(image, level, communityEventId));
    }

    @Secured("ROLE_ADMIN")
    @QueryMapping("findBadgeById")
    public BadgeV3Output findById(@Argument String id) {
        return badgeV3OutputMapper.toDTO(badgeService.findById(id));
    }

    
    @Secured("ROLE_ADMIN" )
    @MutationMapping("addBadgeToUser")
    public Boolean addBadgeToUser(@Argument String userEmail, @Argument String badgeId) {
        return badgeService.addBadgeToUser(userEmail, badgeId);
    }
    @Secured("ROLE_ADMIN")
    @MutationMapping("addBadgeToUsers")
    public List<Boolean> addBadgeToUsers(@Argument List<String> userEmail, @Argument String badgeId) {
        return badgeService.addBadgeToUser(userEmail, badgeId);
    }

}
