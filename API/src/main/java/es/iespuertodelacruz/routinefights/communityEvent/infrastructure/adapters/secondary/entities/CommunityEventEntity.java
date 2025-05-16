package es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.secondary.entities;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.entities.ActivityEntity;
import es.iespuertodelacruz.routinefights.badge.infrastructure.adapters.secondary.entities.BadgeEntity;
import es.iespuertodelacruz.routinefights.communityEvent.commons.CommunityEventCommons;
import es.iespuertodelacruz.routinefights.meeting.infrastructure.adapters.secondary.entities.MeetingEntity;

import java.util.List;
import java.util.Objects;

@Node("CommunityEvent")
/**
 * CommunityEventEntity
 */
public class CommunityEventEntity extends CommunityEventCommons {
    @Id
    @GeneratedValue
    private String id;
    @Relationship(type = "Related", direction = Relationship.Direction.OUTGOING)
    private List<ActivityEntity> activities;
    @Relationship(type = "Associated_With", direction = Relationship.Direction.INCOMING)
    private List<BadgeEntity> badges;
    @Relationship(type = "Part_Of", direction = Relationship.Direction.INCOMING)
    private List<MeetingEntity> meetings;

    public CommunityEventEntity() {
    }

    public CommunityEventEntity(String id, String name, Integer totalRequired, LocalDateTime createdAt,
            LocalDateTime finishDate, List<ActivityEntity> activities, List<BadgeEntity> badges,
            List<MeetingEntity> meetings, LocalDateTime startDate, LocalDateTime updatedAt, LocalDateTime deletedAt,String image) {
        super(name, totalRequired, startDate, finishDate, createdAt, updatedAt, deletedAt,image);
        this.id = id;
        this.activities = activities;
        this.badges = badges;
        this.meetings = meetings;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ActivityEntity> getActivities() {
        return this.activities;
    }

    public void setActivities(List<ActivityEntity> activities) {
        this.activities = activities;
    }

    public List<BadgeEntity> getBadges() {
        return this.badges;
    }

    public void setBadges(List<BadgeEntity> badges) {
        this.badges = badges;
    }

    public List<MeetingEntity> getMeetings() {
        return this.meetings;
    }

    public void setMeetings(List<MeetingEntity> meetings) {
        this.meetings = meetings;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CommunityEventEntity)) {
            return false;
        }
        CommunityEventEntity communityEventEntity = (CommunityEventEntity) o;
        return Objects.equals(id, communityEventEntity.id);
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
                ", totalRequired='" + getTotalRequired() + "'" +
                ", createdAt='" + getCreatedAt() + "'" +
                ", finishDate='" + getFinishDate() + "'" +
                ", activities='" + getActivities() + "'" +
                ", badges='" + getBadges() + "'" +
                ", meetings='" + getMeetings() + "'" +
                "}";
    }

}
