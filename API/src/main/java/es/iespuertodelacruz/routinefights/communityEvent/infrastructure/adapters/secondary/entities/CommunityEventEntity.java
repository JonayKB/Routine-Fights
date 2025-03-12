package es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.secondary.entities;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.entities.ActivityEntity;
import es.iespuertodelacruz.routinefights.badge.infrastructure.adapters.secondary.entities.BadgeEntity;
import es.iespuertodelacruz.routinefights.meeting.infrastructure.adapters.secondary.entities.MeetingEntity;
import es.iespuertodelacruz.routinefights.shared.utils.EntitiesTimestamps;

import java.util.List;
import java.util.Objects;

@Node("CommunityEvent")
/**
 * CommunityEventEntity
 */
public class CommunityEventEntity extends EntitiesTimestamps {
    @Id
    @GeneratedValue
    private String id;
    private String name;
    private Integer totalRequired;
    private LocalDateTime finishDate;
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
            List<MeetingEntity> meetings) {
        super(createdAt, null, null);
        this.id = id;
        this.name = name;
        this.totalRequired = totalRequired;
        this.finishDate = finishDate;
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalRequired() {
        return this.totalRequired;
    }

    public void setTotalRequired(Integer totalRequired) {
        this.totalRequired = totalRequired;
    }

    
    public LocalDateTime getFinishDate() {
        return this.finishDate;
    }

    public void setFinishDate(LocalDateTime finishDate) {
        this.finishDate = finishDate;
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
