package es.iespuertodelacruz.routinefights.badge.domain.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import es.iespuertodelacruz.routinefights.badge.domain.Badge;
import es.iespuertodelacruz.routinefights.badge.domain.ports.secondary.IBadgeRepository;
import es.iespuertodelacruz.routinefights.community_event.domain.CommunityEvent;
import es.iespuertodelacruz.routinefights.community_event.domain.ports.secondary.ICommunityEventRepository;

class BadgeServiceTest {
    private static final String USER_MAIL = "user@example.com";
    private static final String BADGE_ID = "badge123";
    private BadgeService badgeService;
    @Mock
    private IBadgeRepository badgeRepository;
    @Mock
    private ICommunityEventRepository communityEventRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        badgeService = new BadgeService(badgeRepository, communityEventRepository);
    }

    private static final String VALID_IMAGE = "badgeImage.png";
    private static final Integer VALID_LEVEL = 1;
    private static final String VALID_COMMUNITY_EVENT_ID = "event123";
    private static final String INVALID_COMMUNITY_EVENT_ID = "invalidEvent";

    @Test
    void createBadge_withValidData_returnsSavedBadge() {
        CommunityEvent communityEvent = new CommunityEvent();
        Badge badge = new Badge();
        badge.setImage(VALID_IMAGE);
        badge.setLevel(VALID_LEVEL);
        badge.setCommunityEvent(communityEvent);

        when(communityEventRepository.findById(VALID_COMMUNITY_EVENT_ID)).thenReturn(communityEvent);
        when(badgeRepository.save(any(Badge.class))).thenReturn(badge);

        Badge result = badgeService.createBadge(VALID_IMAGE, VALID_LEVEL, VALID_COMMUNITY_EVENT_ID);

        assertNotNull(result);
        assertEquals(VALID_IMAGE, result.getImage());
        assertEquals(VALID_LEVEL, result.getLevel());
        assertEquals(communityEvent, result.getCommunityEvent());
    }

    @Test
    void createBadge_withInvalidCommunityEvent_throwsException() {
        when(communityEventRepository.findById(INVALID_COMMUNITY_EVENT_ID)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            badgeService.createBadge(VALID_IMAGE, VALID_LEVEL, INVALID_COMMUNITY_EVENT_ID);
        });
    }

    @Test
    void findById_withValidId_returnsBadge() {
        Badge badge = new Badge();
        when(badgeRepository.findById(BADGE_ID)).thenReturn(badge);

        Badge result = badgeService.findById(BADGE_ID);

        assertNotNull(result);
        assertEquals(badge, result);
    }

    @Test
    void findByCommunityEvent_withValidCommunityEventId_returnsBadges() {
        List<Badge> badges = List.of(new Badge(), new Badge());
        when(badgeRepository.findByCommunityEvent(VALID_COMMUNITY_EVENT_ID)).thenReturn(badges);

        List<Badge> result = badgeService.findByCommunityEvent(VALID_COMMUNITY_EVENT_ID);

        assertNotNull(result);
        assertEquals(badges.size(), result.size());
        assertEquals(badges, result);
    }

    @Test
    void findByUserEmail_withValidEmail_returnsBadges() {
        List<Badge> badges = List.of(new Badge(), new Badge());
        when(badgeRepository.findByUserEmail(USER_MAIL)).thenReturn(badges);

        List<Badge> result = badgeService.findByUserEmail(USER_MAIL);

        assertNotNull(result);
        assertEquals(badges.size(), result.size());
        assertEquals(badges, result);
    }

    @Test
    void addBadgeToUser_withValidData_returnsTrue() {
        when(badgeRepository.addBadgeToUser(USER_MAIL, BADGE_ID)).thenReturn(true);

        Boolean result = badgeService.addBadgeToUser(USER_MAIL, BADGE_ID);

        assertNotNull(result);
        assertEquals(true, result);
    }

    @Test
    void addBadgeToUser_withValidDataForMultipleUsers_returnsListOfBooleans() {
        List<String> userEmails = List.of("user1@example.com", "user2@example.com");
        List<Boolean> expectedResults = List.of(true, true);
        when(badgeRepository.addBadgeToUser(userEmails, BADGE_ID)).thenReturn(expectedResults);

        List<Boolean> result = badgeService.addBadgeToUser(userEmails, BADGE_ID);

        assertNotNull(result);
        assertEquals(expectedResults.size(), result.size());
        assertEquals(expectedResults, result);
    }
    

}
