package es.iespuertodelacruz.routinefights.badge.infrastructure.adapters.primary.v2.controllers;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import es.iespuertodelacruz.routinefights.badge.domain.ports.primary.IBadgeService;
import es.iespuertodelacruz.routinefights.badge.infrastructure.adapters.primary.v2.dtos.BadgeV2Output;
import es.iespuertodelacruz.routinefights.badge.infrastructure.adapters.primary.v2.mappers.BadgeV2OutputMapper;

@Controller
@CrossOrigin
public class BadgeControllerV2 {
    private IBadgeService badgeService;
    private BadgeV2OutputMapper badgeV2OutputMapper;

    public BadgeControllerV2(IBadgeService badgeService, BadgeV2OutputMapper badgeV2OutputMapper) {
        this.badgeService = badgeService;
        this.badgeV2OutputMapper = badgeV2OutputMapper;
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @QueryMapping("getOwnBadges")
    public List<BadgeV2Output> getOwnBadges() {
        return badgeV2OutputMapper.toDTO(badgeService.findByUserEmail(SecurityContextHolder.getContext()
                .getAuthentication().getName()));
    }
    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @QueryMapping("getBadgesByEmail")
    public List<BadgeV2Output> getBadgesByUserEmail(@Argument String email) {
        return badgeV2OutputMapper.toDTO(badgeService.findByUserEmail(email));
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @QueryMapping("findBadgeByCommunityEvent")
    public List<BadgeV2Output> getBadgesCommunityEvent(@Argument String communityEventId) {
        return badgeV2OutputMapper.toDTO(badgeService.findByCommunityEvent(communityEventId));
    }

}
