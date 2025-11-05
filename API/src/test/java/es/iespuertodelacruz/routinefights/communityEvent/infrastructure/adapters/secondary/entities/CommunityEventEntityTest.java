package es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.secondary.entities;

import org.junit.jupiter.api.Test;

import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.entities.ActivityEntity;
import es.iespuertodelacruz.routinefights.badge.infrastructure.adapters.secondary.entities.BadgeEntity;
import es.iespuertodelacruz.routinefights.community_event.infrastructure.adapters.secondary.entities.CommunityEventEntity;
import es.iespuertodelacruz.routinefights.meeting.infrastructure.adapters.secondary.entities.MeetingEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CommunityEventEntityTest {

    private static final String ID_1 = "CE1";
    private static final String ID_2 = "CE2";
    private static final String NAME = "Community Event";
    private static final Integer TOTAL_REQUIRED = 10;
    private static final LocalDateTime FINISH_DATE = LocalDateTime.of(2023, 5, 10, 12, 0);
    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2023, 1, 1, 12, 0);
    private static final List<ActivityEntity> ACTIVITIES = new ArrayList<>();
    private static final List<BadgeEntity> BADGES = new ArrayList<>();
    private static final List<MeetingEntity> MEETINGS = new ArrayList<>();
    private static final String IMAGE = "image";

    @Test
    void testSettersAndGetters() {
        CommunityEventEntity event = new CommunityEventEntity();
        event.setId(ID_1);
        event.setName(NAME);
        event.setTotalRequired(TOTAL_REQUIRED);
        event.setFinishDate(FINISH_DATE);
        event.setActivities(ACTIVITIES);
        event.setBadges(BADGES);
        event.setMeetings(MEETINGS);
        assertEquals(ID_1, event.getId());
        assertEquals(NAME, event.getName());
        assertEquals(TOTAL_REQUIRED, event.getTotalRequired());
        assertEquals(FINISH_DATE, event.getFinishDate());
        assertEquals(ACTIVITIES, event.getActivities());
        assertEquals(BADGES, event.getBadges());
        assertEquals(MEETINGS, event.getMeetings());
    }

    @Test
    void testConstructorAndGetters() {
        CommunityEventEntity event = new CommunityEventEntity(ID_1, NAME, TOTAL_REQUIRED, CREATED_AT, FINISH_DATE,
                ACTIVITIES, BADGES, MEETINGS, CREATED_AT, FINISH_DATE, CREATED_AT, IMAGE);
        assertEquals(ID_1, event.getId());
        assertEquals(NAME, event.getName());
        assertEquals(TOTAL_REQUIRED, event.getTotalRequired());
        assertEquals(FINISH_DATE, event.getFinishDate());
        assertEquals(ACTIVITIES, event.getActivities());
        assertEquals(BADGES, event.getBadges());
        assertEquals(MEETINGS, event.getMeetings());
        assertEquals(CREATED_AT, event.getCreatedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        CommunityEventEntity event1 = new CommunityEventEntity();
        CommunityEventEntity event2 = new CommunityEventEntity();
        assertEquals(event1, event2);
        event1.setId(ID_1);
        event2.setId(ID_1);
        assertEquals(event1, event2);
        assertEquals(event1.hashCode(), event2.hashCode());
        event2.setId(ID_2);
        assertNotEquals(event1, event2);
        assertNotEquals(event1.hashCode(), event2.hashCode());
        assertNotEquals(event1, new Object());
        assertEquals(event1, event1);
    }

    @Test
    void testToString() {
        CommunityEventEntity event = new CommunityEventEntity();
        event.setId(ID_1);
        event.setName(NAME);
        event.setTotalRequired(TOTAL_REQUIRED);
        event.setFinishDate(FINISH_DATE);
        event.setActivities(ACTIVITIES);
        event.setBadges(BADGES);
        event.setMeetings(MEETINGS);
        String str = event.toString();
        assertTrue(str.contains(ID_1));
        assertTrue(str.contains(NAME));
        assertTrue(str.contains(TOTAL_REQUIRED.toString()));
        assertTrue(str.contains(FINISH_DATE.toString()));
    }
}
