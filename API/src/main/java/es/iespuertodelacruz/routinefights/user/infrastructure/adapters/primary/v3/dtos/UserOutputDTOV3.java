package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v3.dtos;

import java.time.LocalDateTime;

/**
 * UserOutputDTOV3
 */
public record UserOutputDTOV3(String id, String username, String email, String nationality, String phoneNumber,
        String image, String role, boolean verified, String verificationToken, LocalDateTime createdAt,
        LocalDateTime updatedAt, LocalDateTime deletedAt) {

}
