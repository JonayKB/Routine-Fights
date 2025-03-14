package es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.primary.v2.dtos;

import java.time.LocalDateTime;

import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.dtos.UserOutputDTOV2;

public record ActivityOutputV2(String id, String name, String description, String image, UserOutputDTOV2 creator,
                String timeRate, Integer timesRequiered, LocalDateTime createdAt) {

}
