package es.iespuertodelacruz.routinefights.shared.tasks;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import es.iespuertodelacruz.routinefights.device_token.domain.DeviceToken;
import es.iespuertodelacruz.routinefights.shared.services.InsightsService;
import es.iespuertodelacruz.routinefights.shared.services.InsightsService.StreakAtRisk;
import es.iespuertodelacruz.routinefights.shared.services.NotificationsService;
import lombok.extern.slf4j.Slf4j;

/** Every evening, reminds users whose daily streak will go out at midnight. */
@Component
@Slf4j
public class StreakRiskTask {

    private final InsightsService insightsService;
    private final NotificationsService notificationsService;

    public StreakRiskTask(InsightsService insightsService, NotificationsService notificationsService) {
        this.insightsService = insightsService;
        this.notificationsService = notificationsService;
    }

    @Scheduled(cron = "0 0 19 * * *")
    public int remindStreaksAtRisk() {
        List<StreakAtRisk> atRisk = insightsService.dailyStreaksAtRisk(LocalDate.now());
        for (StreakAtRisk risk : atRisk) {
            try {
                notificationsService.sendTo("streakRiskTitle", "streakRiskBody",
                        new DeviceToken(risk.token(), null, risk.language()),
                        Map.of("activity", risk.activity(), "streak", risk.streak()));
            } catch (RuntimeException e) {
                log.warn("Could not send streak reminder: {}", e.getMessage());
            }
        }
        log.info("Streak reminders sent: {}", atRisk.size());
        return atRisk.size();
    }
}
