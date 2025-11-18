package es.iespuertodelacruz.routinefights.device_token.infratruscture.adapters.secondary.entities;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import es.iespuertodelacruz.routinefights.device_token.infrastructure.adapters.secondary.entities.DeviceTokenEntity;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;


class DeviceTokenEntityTest {

    private static final String LANGUAGE = "en-US";
    private static final String TOKEN = "sampleToken";
    private static final UserEntity USER = new UserEntity();


    @Test 
    void testDeviceTokenEntity() {
        DeviceTokenEntity entity = new DeviceTokenEntity();
        entity.setToken(TOKEN);
        entity.setLanguage(LANGUAGE);
        entity.setUser(USER);

        assertEquals(TOKEN, entity.getToken());
        assertEquals(LANGUAGE, entity.getLanguage());
        assertEquals(USER, entity.getUser());

        DeviceTokenEntity entity2 = new DeviceTokenEntity(TOKEN, LANGUAGE, USER);
        assertEquals(TOKEN, entity2.getToken());
        assertEquals(LANGUAGE, entity2.getLanguage());
        assertEquals(USER, entity2.getUser());

    }


}
