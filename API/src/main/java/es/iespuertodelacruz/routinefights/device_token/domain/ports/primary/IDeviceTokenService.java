package es.iespuertodelacruz.routinefights.device_token.domain.ports.primary;

import es.iespuertodelacruz.routinefights.device_token.domain.DeviceToken;

public interface IDeviceTokenService {

    DeviceToken save(String email, String deviceToken, String language);
    
}
