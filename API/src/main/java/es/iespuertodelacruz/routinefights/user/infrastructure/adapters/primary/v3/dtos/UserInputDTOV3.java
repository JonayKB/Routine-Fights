package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v3.dtos;

import java.time.LocalDateTime;

/**
 * UserInputDTOV3
 */
public record UserInputDTOV3(String id, String username, String email, String password, String nationality,
                String phoneNumber, String image, String role, boolean verified, String verificationToken,
                LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
}
