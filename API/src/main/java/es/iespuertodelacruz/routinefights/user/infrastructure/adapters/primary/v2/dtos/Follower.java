package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.dtos;

public record Follower(String id, String username, String nationality, String image, String createdAt, int followers,
        int following) {
}
