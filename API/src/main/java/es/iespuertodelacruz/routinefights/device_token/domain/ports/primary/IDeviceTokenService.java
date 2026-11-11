package es.iespuertodelacruz.routinefights.device_token.domain.ports.primary;

import java.util.List;
import java.util.Map;

import es.iespuertodelacruz.routinefights.device_token.domain.DeviceToken;

public interface IDeviceTokenService {
    DeviceToken save(String email, String deviceToken, String language);

    /** Devices registered for push notifications by the user. */
    List<DeviceToken> findByEmail(String email);

    /** Forgets a device (logout, or a token FCM no longer accepts). */
    void delete(String deviceToken);

    /** Registered devices per language ("es-ES" → 12). */
    Map<String, Long> countByLanguage();
}
