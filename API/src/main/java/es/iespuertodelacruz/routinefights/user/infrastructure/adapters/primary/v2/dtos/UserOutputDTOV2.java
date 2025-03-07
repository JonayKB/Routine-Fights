package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.dtos;

import java.time.LocalDateTime;

public record UserOutputDTOV2(String id, String username, String email, String nationality, String phoneNumber,
                String image, LocalDateTime createdAt, int followers, int following) {
}
