package es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.primary.v2.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import es.iespuertodelacruz.routinefights.activity.domain.Activity;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.primary.v2.dtos.ActivityOutputV2;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.mappers.UserOutputV2Mapper;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.mappers.IUserEntityMapper;

@Mapper(componentModel = "spring", uses = { UserOutputV2Mapper.class, IUserEntityMapper.class })
public interface ActivityOutputV2Mapper {

    ActivityOutputV2 toDTO(Activity activity);


    List<ActivityOutputV2> toDTO(List<Activity> activities);

}
