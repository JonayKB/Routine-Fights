package es.iespuertodelacruz.routinefights.shared.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import es.iespuertodelacruz.routinefights.notification.domain.ports.primary.INotificationService;
import es.iespuertodelacruz.routinefights.notification.domain.ports.secondary.INotificationRepository;
import es.iespuertodelacruz.routinefights.shared.services.InsightsService;
import es.iespuertodelacruz.routinefights.shared.services.InsightsService.StreakAtRisk;

class StreakRiskTaskTest {

    private final InsightsService insights = mock(InsightsService.class);
    private final INotificationService notifications = mock(INotificationService.class);
    private final INotificationRepository repository = mock(INotificationRepository.class);
    private final StreakRiskTask task = new StreakRiskTask(insights, notifications, repository);

    @Test
    void remindsEveryStreakAndSurvivesFailures() {
        when(insights.streaksAtRisk(any(LocalDate.class))).thenReturn(List.of(
                new StreakAtRisk("a@x.com", "1", "Correr", 5, 1, "daily"),
                new StreakAtRisk("b@x.com", "2", "Leer", 2, 3, "weekly")));
        doThrow(new RuntimeException("fcm down")).when(notifications)
                .streakAtRisk(eq("a@x.com"), anyString(), anyString(), anyLong(), anyLong(), anyString(),
                        anyBoolean());

        assertEquals(2, task.remindStreaksAtRisk());
        verify(notifications).streakAtRisk("b@x.com", "2", "Leer", 2, 3, "weekly", false);
    }

    @Test
    void lastCallIsFlagged() {
        when(insights.streaksAtRisk(any(LocalDate.class))).thenReturn(List.of(
                new StreakAtRisk("a@x.com", "1", "Correr", 5, 1, "daily")));

        assertEquals(1, task.lastCall());
        verify(notifications).streakAtRisk("a@x.com", "1", "Correr", 5, 1, "daily", true);
    }

    @Test
    void purgesOldNotifications() {
        when(repository.deleteOlderThan(any(LocalDateTime.class))).thenReturn(4L);
        assertEquals(4L, task.purgeOldNotifications());
    }
}
