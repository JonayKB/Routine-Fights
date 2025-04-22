package es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import es.iespuertodelacruz.routinefights.activity.domain.Activity;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.entities.ActivityEntity;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.mappers.IUserEntityMapper;

@Mapper(componentModel = "spring", uses = { IUserEntityMapper.class })
public interface ActivityEntityMapper {

    Activity toDomain(ActivityEntity activityEntity);

    ActivityEntity toEntity(Activity activity);

    List<Activity> toDomain(List<ActivityEntity> activityEntities);

    List<ActivityEntity> toEntity(List<Activity> activities);

}
