package es.iespuertodelacruz.routinefights.post.infrastructure.adapters.secondary.entities;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.entities.ActivityEntity;
import es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.secondary.entities.CommentEntity;
import es.iespuertodelacruz.routinefights.report.infrastructure.adapters.secondary.entities.ReportEntity;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;
import java.util.Objects;

@Node("Post")
/**
 * PostEntity
 */
public class PostEntity {
    @Id
    @GeneratedValue
    private String id;
    private String image;
    private Integer streak;
    private String pointsToAdd;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private LocalDateTime filedAt;

    @Relationship(type = "On", direction = Relationship.Direction.INCOMING)
    private List<CommentEntity> comments;

    @Relationship(type = "To_Report", direction = Relationship.Direction.INCOMING)
    private List<ReportEntity> reports;

    @Relationship(type = "Posted", direction = Relationship.Direction.INCOMING)
    private UserEntity user;

    @Relationship(type = "Related-To", direction = Relationship.Direction.OUTGOING)
    private ActivityEntity activity;

    @Relationship(type = "Liked", direction = Relationship.Direction.INCOMING)
    private List<UserEntity> likedBy;

    public PostEntity() {
    }

    public PostEntity(String id, String image, Integer streak, String pointsToAdd, LocalDateTime createdAt,
            LocalDateTime updatedAt, LocalDateTime deletedAt, LocalDateTime filedAt, List<CommentEntity> comments,
            List<ReportEntity> reports, UserEntity user, ActivityEntity activity, List<UserEntity> likedBy) {
        this.id = id;
        this.image = image;
        this.streak = streak;
        this.pointsToAdd = pointsToAdd;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.filedAt = filedAt;
        this.comments = comments;
        this.reports = reports;
        this.user = user;
        this.activity = activity;
        this.likedBy = likedBy;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getStreak() {
        return this.streak;
    }

    public void setStreak(Integer streak) {
        this.streak = streak;
    }

    public String getPointsToAdd() {
        return this.pointsToAdd;
    }

    public void setPointsToAdd(String pointsToAdd) {
        this.pointsToAdd = pointsToAdd;
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

    public LocalDateTime getFiledAt() {
        return this.filedAt;
    }

    public void setFiledAt(LocalDateTime filedAt) {
        this.filedAt = filedAt;
    }

    public List<CommentEntity> getComments() {
        return this.comments;
    }

    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }

    public List<ReportEntity> getReports() {
        return this.reports;
    }

    public void setReports(List<ReportEntity> reports) {
        this.reports = reports;
    }

    public UserEntity getUser() {
        return this.user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public ActivityEntity getActivity() {
        return this.activity;
    }

    public void setActivity(ActivityEntity activity) {
        this.activity = activity;
    }

    public PostEntity id(String id) {
        setId(id);
        return this;
    }

    public PostEntity image(String image) {
        setImage(image);
        return this;
    }

    public PostEntity streak(Integer streak) {
        setStreak(streak);
        return this;
    }

    public PostEntity pointsToAdd(String pointsToAdd) {
        setPointsToAdd(pointsToAdd);
        return this;
    }

    public PostEntity createdAt(LocalDateTime createdAt) {
        setCreatedAt(createdAt);
        return this;
    }

    public PostEntity updatedAt(LocalDateTime updatedAt) {
        setUpdatedAt(updatedAt);
        return this;
    }

    public PostEntity deletedAt(LocalDateTime deletedAt) {
        setDeletedAt(deletedAt);
        return this;
    }

    public PostEntity filedAt(LocalDateTime filedAt) {
        setFiledAt(filedAt);
        return this;
    }

    public PostEntity comments(List<CommentEntity> comments) {
        setComments(comments);
        return this;
    }

    public PostEntity reports(List<ReportEntity> reports) {
        setReports(reports);
        return this;
    }

    public PostEntity user(UserEntity user) {
        setUser(user);
        return this;
    }

    public PostEntity activity(ActivityEntity activity) {
        setActivity(activity);
        return this;
    }

    public List<UserEntity> getLikedBy() {
        return this.likedBy;
    }

    public void setLikedBy(List<UserEntity> likedBy) {
        this.likedBy = likedBy;
    }

    public PostEntity likedBy(List<UserEntity> likedBy) {
        setLikedBy(likedBy);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PostEntity)) {
            return false;
        }
        PostEntity postEntity = (PostEntity) o;
        return Objects.equals(id, postEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", image='" + getImage() + "'" +
                ", streak='" + getStreak() + "'" +
                ", pointsToAdd='" + getPointsToAdd() + "'" +
                ", createdAt='" + getCreatedAt() + "'" +
                ", updatedAt='" + getUpdatedAt() + "'" +
                ", deletedAt='" + getDeletedAt() + "'" +
                ", filedAt='" + getFiledAt() + "'" +
                ", comments='" + getComments() + "'" +
                ", reports='" + getReports() + "'" +
                ", user='" + getUser() + "'" +
                ", activity='" + getActivity() + "'" +
                "}";
    }

}
