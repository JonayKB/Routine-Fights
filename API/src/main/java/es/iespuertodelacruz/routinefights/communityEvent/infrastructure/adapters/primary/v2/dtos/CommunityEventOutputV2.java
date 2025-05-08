package es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.primary.v2.dtos;

import java.time.LocalDateTime;

public record CommunityEventOutputV2(String id, String name,
        Integer totalRequired, LocalDateTime startDate,
        LocalDateTime finishDate,String image) {

}
