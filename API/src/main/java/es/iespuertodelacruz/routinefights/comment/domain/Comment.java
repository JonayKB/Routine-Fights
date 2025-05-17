package es.iespuertodelacruz.routinefights.comment.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import es.iespuertodelacruz.routinefights.post.domain.Post;
import es.iespuertodelacruz.routinefights.shared.utils.EntitiesTimestamps;
import es.iespuertodelacruz.routinefights.user.domain.User;

public class Comment extends EntitiesTimestamps {
    private String id;
    private String message;
    private Post post;
    private User user;
    private Comment replingComment;

    public Comment() {
    }

    public Comment(String id) {
        this.id = id;
    }

    public Comment(String id, String message, LocalDateTime createdAt, LocalDateTime updatedAt,
            LocalDateTime deletedAt, Post post, User user, Comment comment) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.message = message;
        this.post = post;
        this.user = user;
        this.replingComment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Comment getReplingComment() {
        return replingComment;
    }

    public void setReplingComment(Comment comment) {
        this.replingComment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Comment))
            return false;
        Comment comment1 = (Comment) o;
        return Objects.equals(id, comment1.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                ", deletedAt=" + getDeletedAt() +
                ", post=" + post +
                ", user=" + user +
                ", comment=" + replingComment +
                '}';
    }
}
