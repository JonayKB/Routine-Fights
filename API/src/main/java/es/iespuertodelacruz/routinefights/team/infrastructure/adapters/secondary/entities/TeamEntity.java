package es.iespuertodelacruz.routinefights.team.infrastructure.adapters.secondary.entities;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import es.iespuertodelacruz.routinefights.meeting.infrastructure.adapters.secondary.entities.MeetingEntity;

import java.util.List;
import java.util.Objects;

@Node("Team")
/**
 * TeamEntity
 */
public class TeamEntity {
    @Id
    @GeneratedValue
    private String id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    @Relationship(type = "Has_Meeting", direction = Relationship.Direction.OUTGOING)
    private List<MeetingEntity> meetings;

    @Relationship(type = "Belongs_To", direction = Relationship.Direction.INCOMING)
    private List<MeetingEntity> creator;


    public TeamEntity() {
    }

    public TeamEntity(String id, String name, LocalDateTime createdAt, LocalDateTime deletedAt, List<MeetingEntity> meetings, List<MeetingEntity> creator) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
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

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getDeletedAt() {
        return this.deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public List<MeetingEntity> getMeetings() {
        return this.meetings;
    }

    public void setMeetings(List<MeetingEntity> meetings) {
        this.meetings = meetings;
    }

    public List<MeetingEntity> getCreator() {
        return this.creator;
    }

    public void setCreator(List<MeetingEntity> creator) {
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

    public TeamEntity creator(List<MeetingEntity> creator) {
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
        return Objects.equals(id, teamEntity.id) ;
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
