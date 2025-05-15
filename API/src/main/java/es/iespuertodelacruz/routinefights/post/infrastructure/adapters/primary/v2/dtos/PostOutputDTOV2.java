package es.iespuertodelacruz.routinefights.post.infrastructure.adapters.primary.v2.dtos;

import java.time.LocalDateTime;

import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.dtos.UserOutputDTOV2;

public record PostOutputDTOV2(String id, String image, Integer streak, LocalDateTime filedAt, LocalDateTime createdAt,
        UserOutputDTOV2 user, Integer likes, Integer comments, Boolean isLiked) {

}
