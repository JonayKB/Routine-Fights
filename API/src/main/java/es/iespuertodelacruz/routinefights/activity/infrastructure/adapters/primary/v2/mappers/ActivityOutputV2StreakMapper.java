package es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.primary.v2.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import es.iespuertodelacruz.routinefights.activity.domain.Activity;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.primary.v2.dtos.ActivityOutputV2Streak;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.mappers.UserOutputV2Mapper;

@Mapper(componentModel = "spring",
        uses = { UserOutputV2Mapper.class })
public interface ActivityOutputV2StreakMapper {

    ActivityOutputV2Streak toDTO(Activity activity);


    List<ActivityOutputV2Streak> toDTO(List<Activity> activities);

}
