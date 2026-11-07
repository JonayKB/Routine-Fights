package es.iespuertodelacruz.routinefights.device_token.infrastructure.adapters.secondary.services;

import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.device_token.domain.DeviceToken;
import es.iespuertodelacruz.routinefights.device_token.domain.ports.secondary.IDeviceTokenRepository;
import es.iespuertodelacruz.routinefights.device_token.infrastructure.adapters.secondary.entities.DeviceTokenEntity;
import es.iespuertodelacruz.routinefights.device_token.infrastructure.adapters.secondary.mappers.IDeviceTokenEntityMapper;
import es.iespuertodelacruz.routinefights.device_token.infrastructure.adapters.secondary.repositories.IDeviceTokenEntityRepository;

@Service
public class DeviceTokenEntityService implements IDeviceTokenRepository {
    private IDeviceTokenEntityRepository deviceTokenEntityRepository;
    private IDeviceTokenEntityMapper deviceTokenEntityMapper;

    public DeviceTokenEntityService(IDeviceTokenEntityRepository deviceTokenEntityRepository,
            IDeviceTokenEntityMapper deviceTokenEntityMapper) {
        this.deviceTokenEntityRepository = deviceTokenEntityRepository;
        this.deviceTokenEntityMapper = deviceTokenEntityMapper;
    }

    @Override
    public DeviceToken save(DeviceToken deviceToken) {
        if (deviceToken.getUser() == null || deviceToken.getUser().getEmail() == null) {
            DeviceTokenEntity entity = deviceTokenEntityMapper.toEntity(deviceToken);
            return deviceTokenEntityMapper.toDomain(deviceTokenEntityRepository.save(entity));
        }
        // Only the token is returned: mapping the linked node back would walk the
        // user <-> device cycle.
        deviceTokenEntityRepository.saveForUser(deviceToken.getUser().getEmail(), deviceToken.getToken(),
                deviceToken.getLanguage());
        return deviceToken;
    }

}
