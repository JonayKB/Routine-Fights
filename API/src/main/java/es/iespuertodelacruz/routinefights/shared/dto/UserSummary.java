package es.iespuertodelacruz.routinefights.shared.dto;

/** The few fields needed to show someone: avatar and name. */
public record UserSummary(String email, String username, String image) {
}
