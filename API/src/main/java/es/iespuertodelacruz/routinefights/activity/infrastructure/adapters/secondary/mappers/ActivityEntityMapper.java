package es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import es.iespuertodelacruz.routinefights.activity.domain.Activity;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.entities.ActivityEntity;

@Mapper(componentModel = "spring")
public interface ActivityEntityMapper {

    Activity toDomain(ActivityEntity activityEntity);

    ActivityEntity toEntity(Activity activity);

    List<Activity> toDomain(List<ActivityEntity> activityEntities);

    List<ActivityEntity> toEntity(List<Activity> activities);

}
