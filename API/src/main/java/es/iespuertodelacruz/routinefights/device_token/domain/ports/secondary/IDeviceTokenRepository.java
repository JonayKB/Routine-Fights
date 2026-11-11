package es.iespuertodelacruz.routinefights.device_token.domain.ports.secondary;

import java.util.List;

import es.iespuertodelacruz.routinefights.device_token.domain.DeviceToken;

public interface IDeviceTokenRepository {
    DeviceToken save(DeviceToken deviceToken);

    List<DeviceToken> findByEmail(String email);

    void delete(String deviceToken);
}
