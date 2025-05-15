package es.iespuertodelacruz.routinefights.badge.infrastructure.adapters.primary.v2.dtos;

import es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.primary.v2.dtos.CommunityEventOutputV2;

public record BadgeV2Output(String id,String image, Integer level, CommunityEventOutputV2 communityEvent) {

}
