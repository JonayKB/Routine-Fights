package es.iespuertodelacruz.routinefights.deviceToken.domain.ports.secondary;

import es.iespuertodelacruz.routinefights.deviceToken.domain.DeviceToken;

public interface IDeviceTokenRepository {
    DeviceToken save(DeviceToken deviceToken);
}
