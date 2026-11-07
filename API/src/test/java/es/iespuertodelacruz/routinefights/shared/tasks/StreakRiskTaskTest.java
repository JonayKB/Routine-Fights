package es.iespuertodelacruz.routinefights.shared.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import es.iespuertodelacruz.routinefights.device_token.domain.DeviceToken;
import es.iespuertodelacruz.routinefights.shared.services.InsightsService;
import es.iespuertodelacruz.routinefights.shared.services.InsightsService.StreakAtRisk;
import es.iespuertodelacruz.routinefights.shared.services.NotificationsService;

class StreakRiskTaskTest {

    @Test
    void sendsOneReminderPerDeviceAndSurvivesFailures() {
        InsightsService insights = mock(InsightsService.class);
        NotificationsService notifications = mock(NotificationsService.class);
        when(insights.dailyStreaksAtRisk(any(LocalDate.class))).thenReturn(List.of(
                new StreakAtRisk("t1", "es-ES", "Correr", 5L),
                new StreakAtRisk("t2", "en-US", "Leer", 2L)));
        when(notifications.sendTo(eq("streakRiskTitle"), eq("streakRiskBody"), any(DeviceToken.class), anyMap()))
                .thenThrow(new RuntimeException("fcm down"))
                .thenReturn("ok");

        assertEquals(2, new StreakRiskTask(insights, notifications).remindStreaksAtRisk());
        verify(notifications, times(2)).sendTo(eq("streakRiskTitle"), eq("streakRiskBody"), any(DeviceToken.class),
                anyMap());
    }
}
