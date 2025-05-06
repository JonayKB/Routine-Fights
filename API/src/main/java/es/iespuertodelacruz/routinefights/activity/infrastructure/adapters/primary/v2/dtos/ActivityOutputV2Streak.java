package es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.primary.v2.dtos;

import java.time.LocalDateTime;

import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.dtos.UserOutputDTOV2;


public record ActivityOutputV2Streak(String id, String name, String description, String image,
                String timeRate, Integer timesRequiered, LocalDateTime createdAt, Integer streak, UserOutputDTOV2 creator, Integer timesRemaining) {

}
