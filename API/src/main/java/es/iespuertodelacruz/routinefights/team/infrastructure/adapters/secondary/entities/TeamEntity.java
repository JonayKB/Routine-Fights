package es.iespuertodelacruz.routinefights.team.infrastructure.adapters.secondary.entities;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import es.iespuertodelacruz.routinefights.meeting.infrastructure.adapters.secondary.entities.MeetingEntity;
import es.iespuertodelacruz.routinefights.shared.utils.EntitiesTimestamps;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;

import java.util.List;
import java.util.Objects;

@Node("Team")
/**
 * TeamEntity
 */
public class TeamEntity extends EntitiesTimestamps {
    @Id
    @GeneratedValue
    private String id;
    private String name;

    @Relationship(type = "Has_Meeting", direction = Relationship.Direction.OUTGOING)
    private List<MeetingEntity> meetings;

    @Relationship(type = "Owns", direction = Relationship.Direction.OUTGOING)
    private UserEntity creator;

    public TeamEntity() {
    }

    public TeamEntity(String id, String name, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt,
            List<MeetingEntity> meetings, UserEntity creator) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.name = name;
        this.meetings = meetings;
        this.creator = creator;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MeetingEntity> getMeetings() {
        return this.meetings;
    }

    public void setMeetings(List<MeetingEntity> meetings) {
        this.meetings = meetings;
    }

    public UserEntity getCreator() {
        return this.creator;
    }

    public void setCreator(UserEntity creator) {
        this.creator = creator;
    }

    public TeamEntity id(String id) {
        setId(id);
        return this;
    }

    public TeamEntity name(String name) {
        setName(name);
        return this;
    }

    public TeamEntity createdAt(LocalDateTime createdAt) {
        setCreatedAt(createdAt);
        return this;
    }

    public TeamEntity deletedAt(LocalDateTime deletedAt) {
        setDeletedAt(deletedAt);
        return this;
    }

    public TeamEntity meetings(List<MeetingEntity> meetings) {
        setMeetings(meetings);
        return this;
    }

    public TeamEntity creator(UserEntity creator) {
        setCreator(creator);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof TeamEntity)) {
            return false;
        }
        TeamEntity teamEntity = (TeamEntity) o;
        return Objects.equals(id, teamEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", name='" + getName() + "'" +
                ", createdAt='" + getCreatedAt() + "'" +
                ", deletedAt='" + getDeletedAt() + "'" +
                ", meetings='" + getMeetings() + "'" +
                ", creator='" + getCreator() + "'" +
                "}";
    }

}
