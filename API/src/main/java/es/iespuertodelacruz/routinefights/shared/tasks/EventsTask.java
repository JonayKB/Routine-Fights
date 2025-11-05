package es.iespuertodelacruz.routinefights.shared.tasks;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import es.iespuertodelacruz.routinefights.badge.domain.Badge;
import es.iespuertodelacruz.routinefights.badge.domain.ports.primary.IBadgeService;
import es.iespuertodelacruz.routinefights.community_event.domain.CommunityEvent;
import es.iespuertodelacruz.routinefights.community_event.domain.ports.primary.ICommunityEventService;

@Component
public class EventsTask {
    private ICommunityEventService communityEventService;
    private IBadgeService badgeService;
    Logger logger = Logger.getLogger(EventsTask.class.getName());

    public EventsTask(ICommunityEventService communityEventService, IBadgeService badgeService) {
        this.badgeService = badgeService;
        this.communityEventService = communityEventService;
    }

    @Scheduled(cron = "0 0 3 * * *")
    protected List<String> giveTodayBadges() {
        CommunityEvent communityEvent = communityEventService.getCommunityEventEndsToday();
        List<String> usersParticipated = communityEventService.getUsersParticipatingInCommunityEventEndsToday();

        List<Badge> badges = badgeService.findByCommunityEvent(communityEvent.getId())
                .stream()
                .sorted((b1, b2) -> Integer.compare(b1.getLevel(), b2.getLevel()))
                .toList();

        Integer points = communityEventService.getCommunityEventPointsById(communityEvent.getId());

        for (int i = 0; i < badges.size(); i++) {
            int threshold = (communityEvent.getTotalRequired() / badges.size()) * (i + 1);
            if (points >= threshold) {
                badgeService.addBadgeToUser(
                        usersParticipated,
                        badges.get(i).getId());
                logger.info("Badge " + badges.get(i).getId() + " given to users");
            }
        }

        logger.info("Badges given to users for community event " + communityEvent.getId());
        return usersParticipated;

    }

}
