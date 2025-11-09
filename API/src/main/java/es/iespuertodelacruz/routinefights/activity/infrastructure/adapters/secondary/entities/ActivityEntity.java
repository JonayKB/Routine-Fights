package es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.entities;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import es.iespuertodelacruz.routinefights.activity.commons.ActivityCommons;
import es.iespuertodelacruz.routinefights.category.infrastructure.adapters.secondary.entities.CategoryEntity;
import es.iespuertodelacruz.routinefights.community_event.infrastructure.adapters.secondary.entities.CommunityEventEntity;
import es.iespuertodelacruz.routinefights.post.infrastructure.adapters.secondary.entities.PostEntity;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;

import java.util.List;
import java.util.Objects;

@Node("Activity")
/**
 * ActivityEntity
 */
public class ActivityEntity extends ActivityCommons {
    @Id
    @GeneratedValue()
    private String id;

    @Relationship(type = "Belongs_To", direction = Relationship.Direction.OUTGOING)
    private CategoryEntity category;

    @Relationship(type = "Related", direction = Relationship.Direction.INCOMING)
    private List<CommunityEventEntity> communityEvent;

    @Relationship(type = "Created", direction = Relationship.Direction.INCOMING)
    private UserEntity creator;

    @Relationship(type = "Participated", direction = Relationship.Direction.INCOMING)
    private List<UserEntity> participants;

    @Relationship(type = "Related_To", direction = Relationship.Direction.INCOMING)
    private List<PostEntity> post;

    public ActivityEntity(String id, String name, String description, String image, String timeRate,
            Integer timesRequiered, CategoryEntity category, List<CommunityEventEntity> communityEvent, UserEntity user,
            List<PostEntity> post, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt, List<UserEntity> participants) {
        super(name, description, image, timeRate, timesRequiered, createdAt, updatedAt, deletedAt);
        this.id = id;

        this.category = category;
        this.communityEvent = communityEvent;
        this.creator = user;
        this.post = post;
        this.participants = participants;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CategoryEntity getCategory() {
        return this.category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public List<CommunityEventEntity> getCommunityEvent() {
        return this.communityEvent;
    }

    public void setCommunityEvent(List<CommunityEventEntity> communityEvent) {
        this.communityEvent = communityEvent;
    }

    public ActivityEntity(String id, CategoryEntity category, List<CommunityEventEntity> communityEvent, UserEntity creator, List<UserEntity> participants, List<PostEntity> post) {
        this.id = id;
        this.category = category;
        this.communityEvent = communityEvent;
        this.creator = creator;
        this.participants = participants;
        this.post = post;
    }

    public List<UserEntity> getParticipants() {
        return this.participants;
    }

    public void setParticipants(List<UserEntity> participants) {
        this.participants = participants;
    }



    public UserEntity getCreator() {
        return this.creator;
    }

    public void setCreator(UserEntity user) {
        this.creator = user;
    }

    public List<PostEntity> getPost() {
        return this.post;
    }

    public void setPost(List<PostEntity> post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", name='" + getName() + "'" +
                ", description='" + getDescription() + "'" +
                ", image='" + getImage() + "'" +
                ", timeRate='" + getTimeRate() + "'" +
                ", timesRequiered='" + getTimesRequiered() + "'" +
                ", category='" + getCategory() + "'" +
                ", communityEvent='" + getCommunityEvent() + "'" +
                ", user='" + getCreator() + "'" +
                ", post='" + getPost() + "'" +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ActivityEntity)) {
            return false;
        }
        ActivityEntity activityEntity = (ActivityEntity) o;
        return Objects.equals(id, activityEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public ActivityEntity() {
    }

}
