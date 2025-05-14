package es.iespuertodelacruz.routinefights.badge.infrastructure.adapters.primary.v3.dtos;

import es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.primary.v3.dtos.CommunityEventOutputV3;

public record BadgeV3Output(String id, String image, Integer level, CommunityEventOutputV3 communityEvent) {

}
