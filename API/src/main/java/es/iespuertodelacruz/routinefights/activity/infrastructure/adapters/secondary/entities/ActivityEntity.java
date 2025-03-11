package es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.entities;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import es.iespuertodelacruz.routinefights.category.infrastructure.adapters.secondary.entities.CategoryEntity;
import es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.secondary.entities.CommunityEventEntity;
import es.iespuertodelacruz.routinefights.post.infrastructure.adapters.secondary.entities.PostEntity;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;

import java.util.List;
import java.util.Objects;

@Node("Activity")
/**
 * ActivityEntity
 */
public class ActivityEntity {
    @Id
    @GeneratedValue()
    private String id;
    private String name;
    private String description;
    private String image;
    private String timeRate;
    private String timesRequiered;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @Relationship(type = "Belongs_To", direction = Relationship.Direction.OUTGOING)
    private CategoryEntity category;

    @Relationship(type = "Related", direction = Relationship.Direction.INCOMING)
    private List<CommunityEventEntity> communityEvent;

    @Relationship(type = "Created", direction = Relationship.Direction.INCOMING)
    private UserEntity user;

    @Relationship(type = "Related_To", direction = Relationship.Direction.INCOMING)
    private List<PostEntity> post;

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

    public ActivityEntity(String id, String name, String description, String image, String timeRate,
            String timesRequiered, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt,
            CategoryEntity category, List<CommunityEventEntity> communityEvent, UserEntity user,
            List<PostEntity> post) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.timeRate = timeRate;
        this.timesRequiered = timesRequiered;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.category = category;
        this.communityEvent = communityEvent;
        this.user = user;
        this.post = post;
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTimeRate() {
        return this.timeRate;
    }

    public void setTimeRate(String timeRate) {
        this.timeRate = timeRate;
    }

    public String getTimesRequiered() {
        return this.timesRequiered;
    }

    public void setTimesRequiered(String timesRequiered) {
        this.timesRequiered = timesRequiered;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return this.deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
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

    public UserEntity getUser() {
        return this.user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public List<PostEntity> getPost() {
        return this.post;
    }

    public void setPost(List<PostEntity> post) {
        this.post = post;
    }

    public ActivityEntity id(String id) {
        setId(id);
        return this;
    }

    public ActivityEntity name(String name) {
        setName(name);
        return this;
    }

    public ActivityEntity description(String description) {
        setDescription(description);
        return this;
    }

    public ActivityEntity image(String image) {
        setImage(image);
        return this;
    }

    public ActivityEntity timeRate(String timeRate) {
        setTimeRate(timeRate);
        return this;
    }

    public ActivityEntity timesRequiered(String timesRequiered) {
        setTimesRequiered(timesRequiered);
        return this;
    }

    public ActivityEntity createdAt(LocalDateTime createdAt) {
        setCreatedAt(createdAt);
        return this;
    }

    public ActivityEntity updatedAt(LocalDateTime updatedAt) {
        setUpdatedAt(updatedAt);
        return this;
    }

    public ActivityEntity deletedAt(LocalDateTime deletedAt) {
        setDeletedAt(deletedAt);
        return this;
    }

    public ActivityEntity category(CategoryEntity category) {
        setCategory(category);
        return this;
    }

    public ActivityEntity communityEvent(List<CommunityEventEntity> communityEvent) {
        setCommunityEvent(communityEvent);
        return this;
    }

    public ActivityEntity user(UserEntity user) {
        setUser(user);
        return this;
    }

    public ActivityEntity post(List<PostEntity> post) {
        setPost(post);
        return this;
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
                ", createdAt='" + getCreatedAt() + "'" +
                ", updatedAt='" + getUpdatedAt() + "'" +
                ", deletedAt='" + getDeletedAt() + "'" +
                ", category='" + getCategory() + "'" +
                ", communityEvent='" + getCommunityEvent() + "'" +
                ", user='" + getUser() + "'" +
                ", post='" + getPost() + "'" +
                "}";
    }

}
