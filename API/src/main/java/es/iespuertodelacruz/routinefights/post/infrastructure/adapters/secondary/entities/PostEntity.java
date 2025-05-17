package es.iespuertodelacruz.routinefights.post.infrastructure.adapters.secondary.entities;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.entities.ActivityEntity;
import es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.secondary.entities.CommentEntity;
import es.iespuertodelacruz.routinefights.post.commons.PostCommons;
import es.iespuertodelacruz.routinefights.report.infrastructure.adapters.secondary.entities.ReportEntity;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;
import java.util.Objects;

@Node("Post")
/**
 * PostEntity
 */
public class PostEntity extends PostCommons {
    @Id
    @GeneratedValue
    private String id;

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

    public PostEntity(String id, String image, Integer streak, Integer pointsToAdd, LocalDateTime createdAt,
            LocalDateTime updatedAt, LocalDateTime deletedAt, LocalDateTime filedAt, List<CommentEntity> comments,
            List<ReportEntity> reports, UserEntity user, ActivityEntity activity, List<UserEntity> likedBy) {
        super(image, streak, pointsToAdd, createdAt, updatedAt, deletedAt, filedAt);
        this.id = id;
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

    public List<UserEntity> getLikedBy() {
        return this.likedBy;
    }

    public void setLikedBy(List<UserEntity> likedBy) {
        this.likedBy = likedBy;
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
