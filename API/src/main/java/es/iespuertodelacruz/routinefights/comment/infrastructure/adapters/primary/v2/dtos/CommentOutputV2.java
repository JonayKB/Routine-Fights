package es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.primary.v2.dtos;

import java.time.LocalDateTime;

import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.dtos.UserOutputDTOV2;

public record CommentOutputV2(String id, String message, LocalDateTime createdAt, UserOutputDTOV2 user) {
    
}
