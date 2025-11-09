package es.iespuertodelacruz.routinefights.device_token.domain.ports.primary;

import es.iespuertodelacruz.routinefights.device_token.domain.DeviceToken;
import es.iespuertodelacruz.routinefights.user.domain.User;

public interface IDeviceTokenService {

    DeviceToken save(String email, String deviceToken, String language);
    
}
