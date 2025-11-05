package es.iespuertodelacruz.routinefights.device_token.domain.ports.secondary;

import es.iespuertodelacruz.routinefights.device_token.domain.DeviceToken;

public interface IDeviceTokenRepository {
    DeviceToken save(DeviceToken deviceToken);
}
