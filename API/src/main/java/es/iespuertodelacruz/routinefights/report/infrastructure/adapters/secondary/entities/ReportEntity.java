package es.iespuertodelacruz.routinefights.report.infrastructure.adapters.secondary.entities;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import es.iespuertodelacruz.routinefights.post.infrastructure.adapters.secondary.entities.PostEntity;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;
import java.util.Objects;

@Node("Report")
/**
 * ReportEntity
 */
public class ReportEntity {
    @Id
    @GeneratedValue
    private String id;
    private String message;
    private LocalDateTime createdAt;

    @Relationship(type = "Reported", direction = Relationship.Direction.INCOMING)
    private UserEntity user;

    @Relationship(type = "To_Report", direction = Relationship.Direction.OUTGOING)
    private PostEntity post;

    public ReportEntity() {
    }

    public ReportEntity(String id, String message, LocalDateTime createdAt, UserEntity user, PostEntity post) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
        this.user = user;
        this.post = post;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UserEntity getUser() {
        return this.user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public PostEntity getPost() {
        return this.post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ReportEntity)) {
            return false;
        }
        ReportEntity reportEntity = (ReportEntity) o;
        return Objects.equals(id, reportEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", message='" + getMessage() + "'" +
                ", createdAt='" + getCreatedAt() + "'" +
                ", user='" + getUser() + "'" +
                ", post='" + getPost() + "'" +
                "}";
    }

}
