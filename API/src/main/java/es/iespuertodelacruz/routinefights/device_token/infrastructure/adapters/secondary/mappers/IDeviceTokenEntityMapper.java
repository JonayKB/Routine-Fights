package es.iespuertodelacruz.routinefights.device_token.infrastructure.adapters.secondary.mappers;

import org.mapstruct.Mapper;

import es.iespuertodelacruz.routinefights.device_token.domain.DeviceToken;
import es.iespuertodelacruz.routinefights.device_token.infrastructure.adapters.secondary.entities.DeviceTokenEntity;

@Mapper(componentModel = "spring")
public interface IDeviceTokenEntityMapper {
    DeviceToken toDomain(DeviceTokenEntity deviceTokenEntity);

    DeviceTokenEntity toEntity(DeviceToken deviceToken);
}
