package es.iespuertodelacruz.routinefights.deviceToken.domain.ports.primary;

import es.iespuertodelacruz.routinefights.deviceToken.domain.DeviceToken;

public interface IDeviceTokenService {

    DeviceToken save(String email, String deviceToken, String language);
    
}
