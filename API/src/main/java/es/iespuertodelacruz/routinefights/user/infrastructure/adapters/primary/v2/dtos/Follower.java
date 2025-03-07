package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.dtos;

import java.time.LocalDateTime;

public record Follower(String id, String username, String nationality, String image, LocalDateTime createdAt, int followers,
        int following) {
}
