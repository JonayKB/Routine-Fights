package es.iespuertodelacruz.routinefights.notification.infrastructure.adapters.primary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import es.iespuertodelacruz.routinefights.device_token.domain.ports.primary.IDeviceTokenService;
import es.iespuertodelacruz.routinefights.notification.domain.ports.primary.INotificationService;

class NotificationResolverV2Test {

    private static final String ME = "me@x.com";
    private final INotificationService service = mock(INotificationService.class);
    private final IDeviceTokenService devices = mock(IDeviceTokenService.class);
    private final NotificationResolverV2 resolver = new NotificationResolverV2(service, devices);

    @BeforeEach
    void login() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(ME, null));
    }

    @AfterEach
    void logout() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void inbox() {
        when(service.findMine(ME, 0, 20)).thenReturn(List.of());
        assertTrue(resolver.myNotifications(null, null).isEmpty());
        resolver.myNotifications(2, 5);
        verify(service).findMine(ME, 2, 5);
        when(service.countUnread(ME)).thenReturn(7);
        assertEquals(7, resolver.unreadNotificationsCount());
        assertTrue(resolver.markNotificationsRead(null));
        verify(service).markAllRead(ME);
        assertTrue(resolver.markNotificationsRead("n1"));
        verify(service).markRead(ME, "n1");
    }

    @Test
    void devices() {
        assertTrue(resolver.registerDevice("tok", null));
        verify(devices).save(ME, "tok", "en-US");
        assertTrue(resolver.registerDevice("tok", "es-ES"));
        verify(devices).save(ME, "tok", "es-ES");
        assertThrows(IllegalArgumentException.class, () -> resolver.registerDevice(" ", "es-ES"));
        assertTrue(resolver.unregisterDevice("tok"));
        verify(devices).delete("tok");
    }
}
