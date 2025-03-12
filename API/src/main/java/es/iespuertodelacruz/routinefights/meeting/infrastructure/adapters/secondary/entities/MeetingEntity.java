package es.iespuertodelacruz.routinefights.meeting.infrastructure.adapters.secondary.entities;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.secondary.entities.CommunityEventEntity;
import es.iespuertodelacruz.routinefights.team.infrastructure.adapters.secondary.entities.TeamEntity;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;
import java.util.Objects;

@Node("Meeting")
/**
 * MeetingEntity
 */
public class MeetingEntity {
    @Id
    @GeneratedValue
    private String id;
    private LocalDateTime date;
    // CSV coords
    private String location;

    @Relationship(type = "Attended", direction = Relationship.Direction.INCOMING)
    private List<UserEntity> users;

    @Relationship(type = "Has_Meeting", direction = Relationship.Direction.INCOMING)
    private TeamEntity team;

    @Relationship(type = "Part_Of", direction = Relationship.Direction.OUTGOING)
    private CommunityEventEntity communityEvent;

    public MeetingEntity() {
    }

    public MeetingEntity(String id, LocalDateTime date, String location, List<UserEntity> users, TeamEntity team,
            CommunityEventEntity communityEvent) {
        this.id = id;
        this.date = date;
        this.location = location;
        this.users = users;
        this.team = team;
        this.communityEvent = communityEvent;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<UserEntity> getUsers() {
        return this.users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }

    public TeamEntity getTeam() {
        return this.team;
    }

    public void setTeam(TeamEntity team) {
        this.team = team;
    }

    public CommunityEventEntity getCommunityEvent() {
        return this.communityEvent;
    }

    public void setCommunityEvent(CommunityEventEntity communityEvent) {
        this.communityEvent = communityEvent;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof MeetingEntity)) {
            return false;
        }
        MeetingEntity meetingEntity = (MeetingEntity) o;
        return Objects.equals(id, meetingEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", date='" + getDate() + "'" +
                ", location='" + getLocation() + "'" +
                ", users='" + getUsers() + "'" +
                ", team='" + getTeam() + "'" +
                ", communityEvent='" + getCommunityEvent() + "'" +
                "}";
    }

}
