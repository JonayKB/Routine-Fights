package es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.primary.v2.dtos;

import java.time.LocalDateTime;


public record ActivityOutputV2Streak(String id, String name, String description, String image,
                String timeRate, Integer timesRequiered, LocalDateTime createdAt, Integer streak) {

}
