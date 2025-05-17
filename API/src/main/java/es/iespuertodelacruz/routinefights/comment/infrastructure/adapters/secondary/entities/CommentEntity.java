package es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.secondary.entities;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.TargetNode;

import es.iespuertodelacruz.routinefights.post.infrastructure.adapters.secondary.entities.PostEntity;
import es.iespuertodelacruz.routinefights.shared.utils.EntitiesTimestamps;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;
import java.util.Objects;

@Node("Comment")
/**
 * CommentEntity
 */
public class CommentEntity extends EntitiesTimestamps {
    @Id
    @GeneratedValue
    private String id;
    private String message;

    @Relationship(type = "On", direction = Relationship.Direction.OUTGOING)
    private PostEntity post;

    @Relationship(type = "Commented", direction = Relationship.Direction.INCOMING)
    private UserEntity user;

    @Relationship(type = "Replied_To", direction = Relationship.Direction.OUTGOING)
    @TargetNode()
    private CommentEntity replingComment;

    public CommentEntity() {
    }

    public CommentEntity(String id, String message, LocalDateTime createdAt, LocalDateTime updatedAt,
            LocalDateTime deletedAt, PostEntity post, UserEntity user, CommentEntity comment) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.message = message;
        this.post = post;
        this.user = user;
        this.replingComment = comment;
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

    public PostEntity getPost() {
        return this.post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }

    public UserEntity getUser() {
        return this.user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public CommentEntity getReplingComment() {
        return this.replingComment;
    }

    public void setReplingComment(CommentEntity comment) {
        this.replingComment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CommentEntity)) {
            return false;
        }
        CommentEntity commentEntity = (CommentEntity) o;
        return Objects.equals(id, commentEntity.id);
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
                ", updatedAt='" + getUpdatedAt() + "'" +
                ", deletedAt='" + getDeletedAt() + "'" +
                ", post='" + getPost() + "'" +
                ", user='" + getUser() + "'" +
                ", comment='" + getReplingComment() + "'" +
                "}";
    }

}
