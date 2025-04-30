package es.iespuertodelacruz.routinefights.comment.domain;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import es.iespuertodelacruz.routinefights.post.domain.Post;
import es.iespuertodelacruz.routinefights.user.domain.User;

class CommentTest {

    @Test
    void testConstructorAndGetters() {
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        LocalDateTime deletedAt = LocalDateTime.now();
        Post post = new Post();
        User user = new User();
        Comment replyingComment = new Comment("replyId");

        Comment comment = new Comment("id", "message", createdAt, updatedAt, deletedAt, post, user, replyingComment);

        assertEquals("id", comment.getId());
        assertEquals("message", comment.getMessage());
        assertEquals(createdAt, comment.getCreatedAt());
        assertEquals(updatedAt, comment.getUpdatedAt());
        assertEquals(deletedAt, comment.getDeletedAt());
        assertEquals(post, comment.getPost());
        assertEquals(user, comment.getUser());
        assertEquals(replyingComment, comment.getReplingComment());
    }

    @Test
    void testSetters() {
        Comment comment = new Comment();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        LocalDateTime deletedAt = LocalDateTime.now();
        Post post = new Post();
        User user = new User();
        Comment replyingComment = new Comment("replyId");

        comment.setId("id");
        comment.setMessage("message");
        comment.setCreatedAt(createdAt);
        comment.setUpdatedAt(updatedAt);
        comment.setDeletedAt(deletedAt);
        comment.setPost(post);
        comment.setUser(user);
        comment.setReplingComment(replyingComment);

        assertEquals("id", comment.getId());
        assertEquals("message", comment.getMessage());
        assertEquals(createdAt, comment.getCreatedAt());
        assertEquals(updatedAt, comment.getUpdatedAt());
        assertEquals(deletedAt, comment.getDeletedAt());
        assertEquals(post, comment.getPost());
        assertEquals(user, comment.getUser());
        assertEquals(replyingComment, comment.getReplingComment());
    }

    @Test
    void testEqualsAndHashCode() {
        Comment comment1 = new Comment("id");
        Comment comment2 = new Comment("id");
        Comment comment3 = new Comment("differentId");

        assertEquals(comment1, comment2);
        assertNotEquals(comment1, comment3);
        assertEquals(comment1.hashCode(), comment2.hashCode());
        assertNotEquals(comment1.hashCode(), comment3.hashCode());
    }

    @Test
    void testToString() {
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        LocalDateTime deletedAt = LocalDateTime.now();
        Post post = new Post();
        User user = new User();
        user.setFollowers(new ArrayList<>());
        user.setFollowing(new ArrayList<>());
        Comment replyingComment = new Comment("replyId");

        Comment comment = new Comment("id", "message", createdAt, updatedAt, deletedAt, post, user, replyingComment);

        String expected = "Comment{id='id', message='message', createdAt=" + createdAt +
                ", updatedAt=" + updatedAt + ", deletedAt=" + deletedAt +
                ", post=" + post + ", user=" + user + ", comment=" + replyingComment + "}";
        assertEquals(expected, comment.toString());
    }
}
