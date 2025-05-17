package es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.secondary.entities;
import org.junit.jupiter.api.Test;

import es.iespuertodelacruz.routinefights.post.infrastructure.adapters.secondary.entities.PostEntity;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

 class CommentEntityTest {

    private static final String COMMENT_ID_1 = "C1";
    private static final String COMMENT_ID_2 = "C2";
    private static final String MESSAGE = "Test comment";
    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2023, 4, 1, 10, 0);
    private static final LocalDateTime UPDATED_AT = LocalDateTime.of(2023, 4, 1, 11, 0);
    private static final LocalDateTime DELETED_AT = LocalDateTime.of(2023, 4, 1, 12, 0);

    private PostEntity createDummyPost() {
        PostEntity post = new PostEntity();
        post.setId("P1");
        return post;
    }

    private UserEntity createDummyUser() {
        UserEntity user = new UserEntity();
        user.setId("U1");
        return user;
    }

    private CommentEntity createDummyNestedComment() {
        CommentEntity comment = new CommentEntity();
        comment.setId("CN1");
        comment.setMessage("Nested comment");
        comment.setCreatedAt(CREATED_AT);
        comment.setUpdatedAt(UPDATED_AT);
        comment.setDeletedAt(DELETED_AT);
        return comment;
    }

    @Test
     void testSettersAndGetters() {
        CommentEntity comment = new CommentEntity();
        comment.setId(COMMENT_ID_1);
        comment.setMessage(MESSAGE);
        comment.setPost(createDummyPost());
        comment.setUser(createDummyUser());
        comment.setReplingComment(createDummyNestedComment());
        comment.setCreatedAt(CREATED_AT);
        comment.setUpdatedAt(UPDATED_AT);
        comment.setDeletedAt(DELETED_AT);

        assertEquals(COMMENT_ID_1, comment.getId());
        assertEquals(MESSAGE, comment.getMessage());
        assertEquals(createDummyPost().toString(), comment.getPost().toString());
        assertEquals(createDummyUser().toString(), comment.getUser().toString());
        assertEquals(createDummyNestedComment().toString(), comment.getReplingComment().toString());
        assertEquals(CREATED_AT, comment.getCreatedAt());
        assertEquals(UPDATED_AT, comment.getUpdatedAt());
        assertEquals(DELETED_AT, comment.getDeletedAt());
    }

    @Test
     void testConstructorAndGetters() {
        PostEntity post = createDummyPost();
        UserEntity user = createDummyUser();
        CommentEntity nestedComment = createDummyNestedComment();
        CommentEntity comment = new CommentEntity(COMMENT_ID_1, MESSAGE, CREATED_AT, UPDATED_AT, DELETED_AT, post, user, nestedComment);

        assertEquals(COMMENT_ID_1, comment.getId());
        assertEquals(MESSAGE, comment.getMessage());
        assertEquals(post.toString(), comment.getPost().toString());
        assertEquals(user.toString(), comment.getUser().toString());
        assertEquals(nestedComment.toString(), comment.getReplingComment().toString());
        assertEquals(CREATED_AT, comment.getCreatedAt());
        assertEquals(UPDATED_AT, comment.getUpdatedAt());
        assertEquals(DELETED_AT, comment.getDeletedAt());
    }

    @Test
     void testEqualsAndHashCode() {
        CommentEntity comment1 = new CommentEntity();
        CommentEntity comment2 = new CommentEntity();
        assertEquals(comment1, comment2);
        comment1.setId(COMMENT_ID_1);
        comment2.setId(COMMENT_ID_1);
        assertEquals(comment1, comment2);
        assertEquals(comment1.hashCode(), comment2.hashCode());
        comment2.setId(COMMENT_ID_2);
        assertNotEquals(comment1, comment2);
        assertNotEquals(comment1.hashCode(), comment2.hashCode());
        assertEquals(comment1, comment1);
    }

    @Test
     void testToString() {
        PostEntity post = createDummyPost();
        UserEntity user = createDummyUser();
        CommentEntity nestedComment = createDummyNestedComment();
        CommentEntity comment = new CommentEntity(COMMENT_ID_1, MESSAGE, CREATED_AT, UPDATED_AT, DELETED_AT, post, user, nestedComment);
        String expected = "{" +
                " id='" + COMMENT_ID_1 + "'" +
                ", message='" + MESSAGE + "'" +
                ", createdAt='" + CREATED_AT.toString() + "'" +
                ", updatedAt='" + UPDATED_AT.toString() + "'" +
                ", deletedAt='" + DELETED_AT.toString() + "'" +
                ", post='" + post.toString() + "'" +
                ", user='" + user.toString() + "'" +
                ", comment='" + nestedComment.toString() + "'" +
                "}";
        assertEquals(expected, comment.toString());
    }
}
