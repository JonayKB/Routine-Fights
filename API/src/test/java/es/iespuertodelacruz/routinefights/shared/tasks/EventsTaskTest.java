package es.iespuertodelacruz.routinefights.shared.tasks;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import es.iespuertodelacruz.routinefights.badge.domain.Badge;
import es.iespuertodelacruz.routinefights.badge.domain.ports.primary.IBadgeService;
import es.iespuertodelacruz.routinefights.communityEvent.domain.CommunityEvent;
import es.iespuertodelacruz.routinefights.communityEvent.domain.ports.primary.ICommunityEventService;
import es.iespuertodelacruz.routinefights.user.domain.User;

class EventsTaskTest {

    private EventsTask eventsTask;
    @Mock
    private ICommunityEventService communityEventService;
    @Mock
    private IBadgeService badgeService;
    private CommunityEvent communityEvent;
    private List<User> usersParticipated;
    private List<Badge> badges;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        eventsTask = new EventsTask(communityEventService, badgeService);
        communityEvent = new CommunityEvent();
        communityEvent.setTotalRequired(100);
        User user = new User();
        user.setId("user1");
        usersParticipated = List.of(user);
        badges = List.of(new Badge("id1", null, 1, usersParticipated, communityEvent),
                new Badge("id2", null, 2, usersParticipated, communityEvent),
                new Badge("id3", null, 3, usersParticipated, communityEvent));
    }

    @Test
    void giveTodayBadgesTestCompleted() {
        when(communityEventService.getCommunityEventEndsToday()).thenReturn(communityEvent);
        when(communityEventService.getUsersParticipatingInCommunityEventEndsToday())
                .thenReturn(usersParticipated.stream().map(User::getId).toList());
        when(badgeService.findByCommunityEvent(communityEvent.getId())).thenReturn(badges);
        when(communityEventService.getCommunityEventPointsById(communityEvent.getId())).thenReturn(100);
        List<String> participatedUserActual = eventsTask.giveTodayBadges();
        assertEquals(usersParticipated.get(0).getId(), participatedUserActual.get(0));

    }

}
