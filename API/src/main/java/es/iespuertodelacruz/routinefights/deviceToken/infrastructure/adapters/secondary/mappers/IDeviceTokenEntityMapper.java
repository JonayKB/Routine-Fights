package es.iespuertodelacruz.routinefights.deviceToken.infrastructure.adapters.secondary.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.iespuertodelacruz.routinefights.deviceToken.domain.DeviceToken;
import es.iespuertodelacruz.routinefights.deviceToken.infrastructure.adapters.secondary.entities.DeviceTokenEntity;

@Mapper(componentModel = "spring")
public interface IDeviceTokenEntityMapper {
    DeviceToken toDomain(DeviceTokenEntity deviceTokenEntity);

    DeviceTokenEntity toEntity(DeviceToken deviceToken);
}
