package es.iespuertodelacruz.routinefights.device_token.infrastructure.adapters.secondary.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.iespuertodelacruz.routinefights.device_token.domain.DeviceToken;
import es.iespuertodelacruz.routinefights.device_token.infrastructure.adapters.secondary.entities.DeviceTokenEntity;

@Mapper(componentModel = "spring")
public interface IDeviceTokenEntityMapper {
    DeviceToken toDomain(DeviceTokenEntity deviceTokenEntity);

    @Mapping(target = "user.deviceTokens", ignore = true)
    DeviceTokenEntity toEntity(DeviceToken deviceToken);
}