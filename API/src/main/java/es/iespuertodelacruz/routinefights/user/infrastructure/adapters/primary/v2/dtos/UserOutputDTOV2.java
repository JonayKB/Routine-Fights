package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.dtos;

public record UserOutputDTOV2(String id, String username, String email, String nationality, String phoneNumber,
                String image, String createdAt, int followers, int following) {
}
