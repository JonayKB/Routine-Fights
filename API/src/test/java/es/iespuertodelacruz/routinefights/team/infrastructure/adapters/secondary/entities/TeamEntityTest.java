package es.iespuertodelacruz.routinefights.team.infrastructure.adapters.secondary.entities;

import org.junit.jupiter.api.Test;

import es.iespuertodelacruz.routinefights.meeting.infrastructure.adapters.secondary.entities.MeetingEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TeamEntityTest {

    private static final String ID_1 = "T1";
    private static final String ID_2 = "T2";
    private static final String NAME = "TeamName";
    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2023, 1, 1, 12, 0);
    private static final LocalDateTime UPDATED_AT = LocalDateTime.of(2023, 1, 2, 12, 0);
    private static final LocalDateTime DELETED_AT = LocalDateTime.of(2023, 1, 3, 12, 0);
    private static final List<MeetingEntity> MEETINGS = new ArrayList<>();
    private static final List<MeetingEntity> CREATOR = new ArrayList<>();

    @Test
    void testSettersAndGetters() {
        TeamEntity team = new TeamEntity();
        team.setId(ID_1);
        team.setName(NAME);
        team.setMeetings(MEETINGS);
        team.setCreator(CREATOR);
        team.setCreatedAt(CREATED_AT);
        team.setDeletedAt(DELETED_AT);
        assertEquals(ID_1, team.getId());
        assertEquals(NAME, team.getName());
        assertEquals(MEETINGS, team.getMeetings());
        assertEquals(CREATOR, team.getCreator());
        assertEquals(CREATED_AT, team.getCreatedAt());
        assertEquals(DELETED_AT, team.getDeletedAt());
    }

    @Test
    void testConstructorAndGetters() {
        TeamEntity team = new TeamEntity(ID_1, NAME, CREATED_AT, UPDATED_AT, DELETED_AT, MEETINGS, CREATOR);
        assertEquals(ID_1, team.getId());
        assertEquals(NAME, team.getName());
        assertEquals(CREATED_AT, team.getCreatedAt());
        assertEquals(UPDATED_AT, team.getUpdatedAt());
        assertEquals(DELETED_AT, team.getDeletedAt());
        assertEquals(MEETINGS, team.getMeetings());
        assertEquals(CREATOR, team.getCreator());
    }

    @Test
    void testFluentMethods() {
        TeamEntity team = new TeamEntity();
        team.id(ID_1)
                .name(NAME)
                .createdAt(CREATED_AT)
                .deletedAt(DELETED_AT)
                .meetings(MEETINGS)
                .creator(CREATOR);
        assertEquals(ID_1, team.getId());
        assertEquals(NAME, team.getName());
        assertEquals(CREATED_AT, team.getCreatedAt());
        assertEquals(DELETED_AT, team.getDeletedAt());
        assertEquals(MEETINGS, team.getMeetings());
        assertEquals(CREATOR, team.getCreator());
    }

    @Test
    void testEqualsAndHashCode() {
        TeamEntity team1 = new TeamEntity();
        TeamEntity team2 = new TeamEntity();
        assertEquals(team1, team2);
        team1.setId(ID_1);
        team2.setId(ID_1);
        assertEquals(team1, team2);
        assertEquals(team1.hashCode(), team2.hashCode());
        team2.setId(ID_2);
        assertNotEquals(team1, team2);
        assertNotEquals(team1.hashCode(), team2.hashCode());
        assertEquals(team1, team1);
        assertNotEquals(team1, new Object());
    }

    @Test
    void testToString() {
        TeamEntity team = new TeamEntity(ID_1, NAME, CREATED_AT, UPDATED_AT, DELETED_AT, MEETINGS, CREATOR);
        String expected = "{" +
                " id='" + ID_1 + "'" +
                ", name='" + NAME + "'" +
                ", createdAt='" + CREATED_AT + "'" +
                ", deletedAt='" + DELETED_AT + "'" +
                ", meetings='" + MEETINGS + "'" +
                ", creator='" + CREATOR + "'" +
                "}";
        assertEquals(expected, team.toString());
    }
}
