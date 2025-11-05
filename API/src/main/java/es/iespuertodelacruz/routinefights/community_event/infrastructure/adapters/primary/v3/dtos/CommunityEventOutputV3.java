package es.iespuertodelacruz.routinefights.community_event.infrastructure.adapters.primary.v3.dtos;

import java.time.LocalDateTime;

public record CommunityEventOutputV3(String id, String name,
        Integer totalRequired, LocalDateTime startDate,
        LocalDateTime finishDate, LocalDateTime createdAt,
        LocalDateTime updatedAt, LocalDateTime deletedAt, String image) {
}
