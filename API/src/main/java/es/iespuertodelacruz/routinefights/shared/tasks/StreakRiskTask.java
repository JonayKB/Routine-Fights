package es.iespuertodelacruz.routinefights.shared.tasks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import es.iespuertodelacruz.routinefights.notification.domain.ports.primary.INotificationService;
import es.iespuertodelacruz.routinefights.notification.domain.ports.secondary.INotificationRepository;
import es.iespuertodelacruz.routinefights.shared.services.InsightsService;
import es.iespuertodelacruz.routinefights.shared.services.InsightsService.StreakAtRisk;
import lombok.extern.slf4j.Slf4j;

/**
 * Reminds users whose streak goes out tonight: once in the evening and a last
 * call before midnight. Both land in the same inbox entry and the same system
 * notification, so the user never sees a pile of reminders.
 */
@Component
@Slf4j
public class StreakRiskTask {

    static final int KEEP_NOTIFICATIONS_DAYS = 60;

    private final InsightsService insightsService;
    private final INotificationService notificationService;
    private final INotificationRepository notificationRepository;

    public StreakRiskTask(InsightsService insightsService, INotificationService notificationService,
            INotificationRepository notificationRepository) {
        this.insightsService = insightsService;
        this.notificationService = notificationService;
        this.notificationRepository = notificationRepository;
    }

    @Scheduled(cron = "0 0 18 * * *")
    public int remindStreaksAtRisk() {
        return remind(false);
    }

    @Scheduled(cron = "0 0 22 * * *")
    public int lastCall() {
        return remind(true);
    }

    int remind(boolean lastCall) {
        List<StreakAtRisk> atRisk = insightsService.streaksAtRisk(LocalDate.now());
        for (StreakAtRisk risk : atRisk) {
            try {
                notificationService.streakAtRisk(risk.email(), risk.activityId(), risk.activity(), risk.streak(),
                        risk.remaining(), risk.timeRate(), lastCall);
            } catch (RuntimeException e) {
                log.warn("Could not send streak reminder: {}", e.getMessage());
            }
        }
        log.info("Streak reminders sent ({}): {}", lastCall ? "last call" : "evening", atRisk.size());
        return atRisk.size();
    }

    /** Old inbox entries are of no use; keep the graph small. */
    @Scheduled(cron = "0 30 4 * * *")
    public long purgeOldNotifications() {
        return notificationRepository.deleteOlderThan(LocalDateTime.now().minusDays(KEEP_NOTIFICATIONS_DAYS));
    }
}
