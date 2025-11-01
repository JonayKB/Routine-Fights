package es.iespuertodelacruz.routinefights.deviceToken.domain.services;

import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.deviceToken.domain.DeviceToken;
import es.iespuertodelacruz.routinefights.deviceToken.domain.ports.primary.IDeviceTokenService;
import es.iespuertodelacruz.routinefights.deviceToken.domain.ports.secondary.IDeviceTokenRepository;
import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.domain.ports.primary.IUserService;

@Service
public class DeviceTokenService implements IDeviceTokenService {

    private IDeviceTokenRepository deviceTokenRepository;
    private IUserService userService;

    @Override
    public DeviceToken save(String email, String deviceToken, String language) {
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        DeviceToken token = new DeviceToken();
        token.setUser(user);
        token.setToken(deviceToken);
        token.setLanguage(language);

        return deviceTokenRepository.save(token);
    }
}
