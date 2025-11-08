package es.iespuertodelacruz.routinefights.device_token.domain;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import es.iespuertodelacruz.routinefights.user.domain.User;

class DeviceTokenTest {

    private static final String LANGUAGE = "en_US";
    private static final String TOKEN = "sampleToken";
    private static final User USER = new User();

    @Test
    void testDeviceToken() {
        DeviceToken deviceToken = new DeviceToken();
        deviceToken.setToken(TOKEN);
        deviceToken.setLanguage(LANGUAGE);
        deviceToken.setUser(USER);

        assertEquals(TOKEN, deviceToken.getToken());
        assertEquals(LANGUAGE, deviceToken.getLanguage());
        assertEquals(USER, deviceToken.getUser());

        DeviceToken deviceToken2 = new DeviceToken(TOKEN, USER, LANGUAGE);
        assertEquals(TOKEN, deviceToken2.getToken());
        assertEquals(LANGUAGE, deviceToken2.getLanguage());
        assertEquals(USER, deviceToken2.getUser());

    }
}
