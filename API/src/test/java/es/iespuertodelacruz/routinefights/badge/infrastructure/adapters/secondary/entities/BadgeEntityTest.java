package es.iespuertodelacruz.routinefights.badge.infrastructure.adapters.secondary.entities;

import org.junit.jupiter.api.Test;

import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.entities.ActivityEntity;
import es.iespuertodelacruz.routinefights.community_event.infrastructure.adapters.secondary.entities.CommunityEventEntity;
import es.iespuertodelacruz.routinefights.meeting.infrastructure.adapters.secondary.entities.MeetingEntity;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

 class BadgeEntityTest {

    private static final String BADGE_ID_1 = "B1";
    private static final String BADGE_ID_2 = "B2";
    private static final String IMAGE = "http://example.com/badge.png";
    private static final Integer LEVEL = 3;
    private static final List<UserEntity> USERS = new ArrayList<>();

    private static final String CE_ID = "CE1";
    private static final String CE_NAME = "Event";
    private static final Integer CE_TOTAL_REQUIRED = 100;
    private static final LocalDateTime CE_CREATED_AT = LocalDateTime.of(2023, 2, 1, 12, 0);
    private static final LocalDateTime CE_FINISH_DATE = LocalDateTime.of(2023, 2, 10, 12, 0);
    private static final List<ActivityEntity> CE_ACTIVITIES = new ArrayList<>();
    private static final List<BadgeEntity> CE_BADGES = new ArrayList<>();
    private static final List<MeetingEntity> CE_MEETINGS = new ArrayList<>();

    @Test
     void testSettersAndGetters() {
        BadgeEntity badge = new BadgeEntity();
        badge.setId(BADGE_ID_1);
        badge.setImage(IMAGE);
        badge.setLevel(LEVEL);
        badge.setUsers(USERS);
        CommunityEventEntity communityEvent = new CommunityEventEntity(CE_ID, CE_NAME, CE_TOTAL_REQUIRED, CE_CREATED_AT, CE_FINISH_DATE, CE_ACTIVITIES, CE_BADGES, CE_MEETINGS, CE_CREATED_AT, CE_FINISH_DATE, CE_CREATED_AT, IMAGE);
        badge.setCommunityEvent(communityEvent);
        assertEquals(BADGE_ID_1, badge.getId());
        assertEquals(IMAGE, badge.getImage());
        assertEquals(LEVEL, badge.getLevel());
        assertEquals(USERS, badge.getUsers());
        assertEquals(communityEvent, badge.getCommunityEvent());
    }

    @Test
     void testConstructorAndGetters() {
        CommunityEventEntity communityEvent = new CommunityEventEntity(CE_ID, CE_NAME, CE_TOTAL_REQUIRED, CE_CREATED_AT, CE_FINISH_DATE, CE_ACTIVITIES, CE_BADGES, CE_MEETINGS, CE_CREATED_AT, CE_FINISH_DATE, CE_CREATED_AT, IMAGE);
        BadgeEntity badge = new BadgeEntity(BADGE_ID_1, IMAGE, LEVEL, USERS, communityEvent);
        assertEquals(BADGE_ID_1, badge.getId());
        assertEquals(IMAGE, badge.getImage());
        assertEquals(LEVEL, badge.getLevel());
        assertEquals(USERS, badge.getUsers());
        assertEquals(communityEvent, badge.getCommunityEvent());
    }

    @Test
     void testFluentMethods() {
        CommunityEventEntity communityEvent = new CommunityEventEntity(CE_ID, CE_NAME, CE_TOTAL_REQUIRED, CE_CREATED_AT, CE_FINISH_DATE, CE_ACTIVITIES, CE_BADGES, CE_MEETINGS, CE_CREATED_AT, CE_FINISH_DATE, CE_CREATED_AT, IMAGE);
        BadgeEntity badge = new BadgeEntity();
        badge.id(BADGE_ID_1)
             .image(IMAGE)
             .level(LEVEL)
             .users(USERS)
             .communityEvent(communityEvent);
        assertEquals(BADGE_ID_1, badge.getId());
        assertEquals(IMAGE, badge.getImage());
        assertEquals(LEVEL, badge.getLevel());
        assertEquals(USERS, badge.getUsers());
        assertEquals(communityEvent, badge.getCommunityEvent());
    }

    @Test
     void testEqualsAndHashCode() {
        BadgeEntity badge1 = new BadgeEntity();
        BadgeEntity badge2 = new BadgeEntity();
        assertEquals(badge1, badge2);
        badge1.setId(BADGE_ID_1);
        badge2.setId(BADGE_ID_1);
        assertEquals(badge1, badge2);
        assertEquals(badge1.hashCode(), badge2.hashCode());
        badge2.setId(BADGE_ID_2);
        assertNotEquals(badge1, badge2);
        assertNotEquals(badge1.hashCode(), badge2.hashCode());
        assertEquals(badge1, badge1);
        assertNotEquals(badge1, new Object());
    }

    @Test
     void testToString() {
        CommunityEventEntity communityEvent = new CommunityEventEntity(CE_ID, CE_NAME, CE_TOTAL_REQUIRED, CE_CREATED_AT, CE_FINISH_DATE, CE_ACTIVITIES, CE_BADGES, CE_MEETINGS, CE_CREATED_AT, CE_FINISH_DATE, CE_CREATED_AT, IMAGE);
        BadgeEntity badge = new BadgeEntity(BADGE_ID_1, IMAGE, LEVEL, USERS, communityEvent);
        String expected = "{" +
                          " id='" + BADGE_ID_1 + "'" +
                          ", image='" + IMAGE + "'" +
                          ", level='" + LEVEL + "'" +
                          ", user='" + USERS + "'" +
                          ", communityEvent='" + communityEvent.toString() + "'" +
                          "}";
        assertEquals(expected, badge.toString());
    }
}
