package es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.primary.v2.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import es.iespuertodelacruz.routinefights.activity.domain.Activity;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.primary.v2.dtos.ActivityOutputV2Streak;

@Mapper(componentModel = "spring")
public interface ActivityOutputV2StreakMapper {

    ActivityOutputV2Streak toDTO(Activity activity);


    List<ActivityOutputV2Streak> toDTO(List<Activity> activities);

}
