package es.iespuertodelacruz.routinefights.device_token.infrastructure.adapters.secondary.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.iespuertodelacruz.routinefights.device_token.domain.DeviceToken;
import es.iespuertodelacruz.routinefights.device_token.infrastructure.adapters.secondary.entities.DeviceTokenEntity;

@Mapper(componentModel = "spring")
public interface IDeviceTokenEntityMapper {
    // The owner is not mapped back: User -> tokens -> user -> followers... would never end.
    @Mapping(target = "user", ignore = true)
    DeviceToken toDomain(DeviceTokenEntity deviceTokenEntity);

    @Mapping(target = "user", ignore = true)
    DeviceTokenEntity toEntity(DeviceToken deviceToken);
}
