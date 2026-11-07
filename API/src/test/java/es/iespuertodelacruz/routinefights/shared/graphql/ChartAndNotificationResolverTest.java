package es.iespuertodelacruz.routinefights.shared.graphql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import es.iespuertodelacruz.routinefights.shared.dto.ChartData;
import es.iespuertodelacruz.routinefights.shared.graphql.NotificationResolver.KeyValue;
import es.iespuertodelacruz.routinefights.shared.services.GraphService;
import es.iespuertodelacruz.routinefights.shared.services.NotificationsService;

class ChartAndNotificationResolverTest {

    @Test
    void chartsDelegateToGraphService() {
        GraphService graphService = mock(GraphService.class);
        ChartData data = new ChartData(List.of("a"), List.of(1L));
        when(graphService.findUserRegistrationsByDate()).thenReturn(data);
        when(graphService.findPostsCreatedByDate()).thenReturn(data);
        when(graphService.findPointsAddedSumByDate()).thenReturn(data);
        when(graphService.findTotalPointsByUser()).thenReturn(data);
        when(graphService.findActivitiesByTimeRate()).thenReturn(data);
        ChartResolver resolver = new ChartResolver(graphService);
        assertSame(data, resolver.userCreationChart());
        assertSame(data, resolver.postCreationChart());
        assertSame(data, resolver.postPointsChart());
        assertSame(data, resolver.userPointsChart());
        assertSame(data, resolver.activityTimeRateChart());
    }

    @Test
    void toMapHandlesNullAndValues() {
        assertTrue(NotificationResolver.toMap(null).isEmpty());
        assertEquals(Map.of("name", "Ana"), NotificationResolver.toMap(List.of(new KeyValue("name", "Ana"))));
    }

    @Test
    void sendToDeviceBuildsDeviceToken() {
        NotificationsService service = mock(NotificationsService.class);
        when(service.sendTo(eq("t"), eq("b"),
                argThat((es.iespuertodelacruz.routinefights.device_token.domain.DeviceToken d) -> "tok"
                        .equals(d.getToken()) && "es".equals(d.getLanguage())),
                eq(Map.of()))).thenReturn("ok");
        when(service.sendToAllUsers("t", "b", Map.of())).thenReturn("all");
        NotificationResolver resolver = new NotificationResolver(service);
        assertEquals("ok", resolver.sendNotificationToDevice("t", "b", "tok", "es", null));
        assertEquals("all", resolver.sendNotificationToAll("t", "b", null));
    }
}
