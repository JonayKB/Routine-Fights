package es.iespuertodelacruz.routinefights.shared.graphql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import es.iespuertodelacruz.routinefights.device_token.domain.DeviceToken;
import es.iespuertodelacruz.routinefights.device_token.domain.ports.primary.IDeviceTokenService;
import es.iespuertodelacruz.routinefights.shared.graphql.NotificationResolver.PushTextInput;
import es.iespuertodelacruz.routinefights.shared.services.NotificationsService;
import es.iespuertodelacruz.routinefights.shared.services.NotificationsService.PushText;

class AdminPushResolverTest {

    private final NotificationsService push = mock(NotificationsService.class);
    private final IDeviceTokenService devices = mock(IDeviceTokenService.class);
    private final NotificationResolver resolver = new NotificationResolver(push, devices);

    @Test
    void broadcastSendsEachLanguageAndAFallback() {
        when(push.broadcast(anyMap(), any())).thenReturn("es=1; en=2");

        assertEquals("es=1; en=2", resolver.broadcastNotification(List.of(
                new PushTextInput("es-ES", " Hola ", "Cuerpo"),
                new PushTextInput("default", "Hello", null),
                new PushTextInput("fr", " ", "ignored"))));

        verify(push).broadcast(Map.of("es", new PushText("Hola", "Cuerpo")), new PushText("Hello", ""));
    }

    @Test
    void broadcastNeedsTextAndAWorkingFirebase() {
        assertThrows(IllegalArgumentException.class, () -> resolver.broadcastNotification(List.of()));
        assertThrows(IllegalArgumentException.class, () -> resolver.broadcastNotification(null));
        assertThrows(IllegalArgumentException.class,
                () -> resolver.broadcastNotification(List.of(new PushTextInput("es", "x".repeat(121), ""))));
        when(push.broadcast(anyMap(), any())).thenReturn(null);
        assertThrows(IllegalArgumentException.class,
                () -> resolver.broadcastNotification(List.of(new PushTextInput(null, "Hi", ""))));
    }

    @Test
    void notifyUserReachesTheirDevices() {
        List<DeviceToken> tokens = List.of(new DeviceToken("t", null, "es-ES"));
        when(devices.findByEmail("ana@x.com")).thenReturn(tokens);
        when(push.pushText(tokens, "Hi", "there")).thenReturn(1);

        assertEquals(1, resolver.notifyUser("ana@x.com", "Hi", "there"));
        when(devices.findByEmail("nobody@x.com")).thenReturn(List.of());
        assertThrows(IllegalArgumentException.class, () -> resolver.notifyUser("nobody@x.com", "Hi", ""));
        assertThrows(IllegalArgumentException.class, () -> resolver.notifyUser("ana@x.com", "", ""));
    }

    @Test
    void stats() {
        Map<String, Long> counts = new LinkedHashMap<>();
        counts.put("es-ES", 3L);
        when(devices.countByLanguage()).thenReturn(counts);
        assertEquals(List.of(new NotificationResolver.LanguageCount("es-ES", 3)), resolver.pushDevices());
        when(push.supportedPrefixes()).thenReturn(List.of("en", "es"));
        assertEquals(List.of("en", "es"), resolver.pushLanguages());
        assertNull(NotificationResolver.validate(null, "x"));
        assertEquals(new PushText("a", ""), NotificationResolver.validate("a", null));
        verify(push).supportedPrefixes();
        verify(devices).countByLanguage();
    }
}
