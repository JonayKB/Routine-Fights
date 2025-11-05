package es.iespuertodelacruz.routinefights.meeting.infrastructure.adapters.secondary.entities;

import org.junit.jupiter.api.Test;

import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.entities.ActivityEntity;
import es.iespuertodelacruz.routinefights.badge.infrastructure.adapters.secondary.entities.BadgeEntity;
import es.iespuertodelacruz.routinefights.community_event.infrastructure.adapters.secondary.entities.CommunityEventEntity;
import es.iespuertodelacruz.routinefights.team.infrastructure.adapters.secondary.entities.TeamEntity;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class MeetingEntityTest {

    private static final String MEETING_ID = "M1";
    private static final LocalDateTime MEETING_DATE = LocalDateTime.of(2023, 3, 15, 14, 30);
    private static final String LOCATION = "12.34,56.78";
    private static final List<UserEntity> USERS = new ArrayList<>();

    private static final String TEAM_ID = "T1";
    private static final String TEAM_NAME = "Team One";
    private static final LocalDateTime TEAM_CREATED_AT = LocalDateTime.of(2023, 3, 1, 10, 0);
    private static final LocalDateTime TEAM_DELETED_AT = null;
    private static final List<MeetingEntity> TEAM_MEETINGS = new ArrayList<>();
    private static final UserEntity TEAM_CREATOR = new UserEntity();

    private static final String CE_ID = "CE1";
    private static final String CE_NAME = "Community Event";
    private static final Integer CE_TOTAL_REQUIRED = 50;
    private static final LocalDateTime CE_CREATED_AT = LocalDateTime.of(2023, 2, 20, 9, 0);
    private static final LocalDateTime CE_FINISH_DATE = LocalDateTime.of(2023, 2, 28, 17, 0);
    private static final List<ActivityEntity> CE_ACTIVITIES = new ArrayList<>();
    private static final List<BadgeEntity> CE_BADGES = new ArrayList<>();
    private static final List<MeetingEntity> CE_MEETINGS = new ArrayList<>();
    private static final String IMAGE = "image";

    private TeamEntity createTeam() {
        TeamEntity team = new TeamEntity();
        team.setId(TEAM_ID);
        team.setName(TEAM_NAME);
        team.setCreatedAt(TEAM_CREATED_AT);
        team.setDeletedAt(TEAM_DELETED_AT);
        team.setMeetings(TEAM_MEETINGS);
        team.setCreator(TEAM_CREATOR);
        return team;
    }

    private CommunityEventEntity createCommunityEvent() {
        return new CommunityEventEntity(
                CE_ID,
                CE_NAME,
                CE_TOTAL_REQUIRED,
                CE_CREATED_AT,
                CE_FINISH_DATE,
                CE_ACTIVITIES,
                CE_BADGES,
                CE_MEETINGS,
                CE_CREATED_AT,
                CE_FINISH_DATE,
                CE_CREATED_AT, IMAGE);
    }

    @Test
    void testSettersAndGetters() {
        MeetingEntity meeting = new MeetingEntity();
        meeting.setId(MEETING_ID);
        meeting.setDate(MEETING_DATE);
        meeting.setLocation(LOCATION);
        meeting.setUsers(USERS);
        meeting.setTeam(createTeam());
        meeting.setCommunityEvent(createCommunityEvent());
        assertEquals(MEETING_ID, meeting.getId());
        assertEquals(MEETING_DATE, meeting.getDate());
        assertEquals(LOCATION, meeting.getLocation());
        assertEquals(USERS, meeting.getUsers());
        assertEquals(createTeam().toString(), meeting.getTeam().toString());
        assertEquals(createCommunityEvent().toString(), meeting.getCommunityEvent().toString());
    }

    @Test
    void testConstructorAndGetters() {
        MeetingEntity meeting = new MeetingEntity(
                MEETING_ID,
                MEETING_DATE,
                LOCATION,
                USERS,
                createTeam(),
                createCommunityEvent());
        assertEquals(MEETING_ID, meeting.getId());
        assertEquals(MEETING_DATE, meeting.getDate());
        assertEquals(LOCATION, meeting.getLocation());
        assertEquals(USERS, meeting.getUsers());
        assertEquals(createTeam().toString(), meeting.getTeam().toString());
        assertEquals(createCommunityEvent().toString(), meeting.getCommunityEvent().toString());
    }

    @Test
    void testEqualsAndHashCode() {
        MeetingEntity meeting1 = new MeetingEntity();
        MeetingEntity meeting2 = new MeetingEntity();
        assertEquals(meeting1, meeting2);
        meeting1.setId(MEETING_ID);
        meeting2.setId(MEETING_ID);
        assertEquals(meeting1, meeting2);
        assertEquals(meeting1.hashCode(), meeting2.hashCode());
        meeting2.setId("M2");
        assertNotEquals(meeting1, meeting2);
        assertNotEquals(meeting1.hashCode(), meeting2.hashCode());
        assertEquals(meeting1, meeting1);
        assertNotEquals(meeting1, new Object());
    }

    @Test
    void testToString() {
        TeamEntity team = createTeam();
        CommunityEventEntity communityEvent = createCommunityEvent();
        MeetingEntity meeting = new MeetingEntity(
                MEETING_ID,
                MEETING_DATE,
                LOCATION,
                USERS,
                team,
                communityEvent);
        String expected = "{" +
                " id='" + MEETING_ID + "'" +
                ", date='" + MEETING_DATE.toString() + "'" +
                ", location='" + LOCATION + "'" +
                ", users='" + USERS.toString() + "'" +
                ", team='" + team.toString() + "'" +
                ", communityEvent='" + communityEvent.toString() + "'" +
                "}";
        assertEquals(expected, meeting.toString());
    }
}
