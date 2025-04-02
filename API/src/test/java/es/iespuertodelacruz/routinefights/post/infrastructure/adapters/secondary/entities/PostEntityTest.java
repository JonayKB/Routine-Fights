package es.iespuertodelacruz.routinefights.post.infrastructure.adapters.secondary.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.entities.ActivityEntity;
import es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.secondary.entities.CommentEntity;
import es.iespuertodelacruz.routinefights.report.infrastructure.adapters.secondary.entities.ReportEntity;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;

import java.util.List;

class PostEntityTest {

    private static final String ID_1 = "1";
    private static final String ID_2 = "2";
    private static final String IMAGE = "http://example.com/image.png";
    private static final Integer STREAK = 5;
    private static final Integer POINTS = 10;
    private static final LocalDateTime FILED_AT = LocalDateTime.of(2023, 1, 1, 12, 0);
    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2023, 1, 2, 12, 0);
    private static final LocalDateTime UPDATED_AT = LocalDateTime.of(2023, 1, 3, 12, 0);
    private static final LocalDateTime DELETED_AT = LocalDateTime.of(2023, 1, 4, 12, 0);

    @Test
    void testSettersAndGetters() {
        PostEntity post = new PostEntity();
        post.setId(ID_1);
        post.setImage(IMAGE);
        post.setStreak(STREAK);
        post.setPointsToAdd(POINTS);
        post.setFiledAt(FILED_AT);
        List<CommentEntity> comments = new ArrayList<>();
        post.setComments(comments);
        List<ReportEntity> reports = new ArrayList<>();
        post.setReports(reports);
        UserEntity user = new UserEntity();
        post.setUser(user);
        ActivityEntity activity = new ActivityEntity();
        post.setActivity(activity);
        List<UserEntity> likedBy = new ArrayList<>();
        post.setLikedBy(likedBy);
        assertEquals(ID_1, post.getId());
        assertEquals(IMAGE, post.getImage());
        assertEquals(STREAK, post.getStreak());
        assertEquals(POINTS, post.getPointsToAdd());
        assertEquals(FILED_AT, post.getFiledAt());
        assertEquals(comments, post.getComments());
        assertEquals(reports, post.getReports());
        assertEquals(user, post.getUser());
        assertEquals(activity, post.getActivity());
        assertEquals(likedBy, post.getLikedBy());
    }

    @Test
    void testConstructorAndGetters() {
        List<CommentEntity> comments = new ArrayList<>();
        List<ReportEntity> reports = new ArrayList<>();
        List<UserEntity> likedBy = new ArrayList<>();
        UserEntity user = new UserEntity();
        ActivityEntity activity = new ActivityEntity();
        PostEntity post = new PostEntity(ID_1, IMAGE, STREAK, POINTS, CREATED_AT, UPDATED_AT, DELETED_AT, FILED_AT,
                comments, reports, user, activity, likedBy);
        assertEquals(ID_1, post.getId());
        assertEquals(IMAGE, post.getImage());
        assertEquals(STREAK, post.getStreak());
        assertEquals(POINTS, post.getPointsToAdd());
        assertEquals(FILED_AT, post.getFiledAt());
        assertEquals(comments, post.getComments());
        assertEquals(reports, post.getReports());
        assertEquals(user, post.getUser());
        assertEquals(activity, post.getActivity());
        assertEquals(likedBy, post.getLikedBy());
        assertEquals(CREATED_AT, post.getCreatedAt());
        assertEquals(UPDATED_AT, post.getUpdatedAt());
        assertEquals(DELETED_AT, post.getDeletedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        PostEntity post1 = new PostEntity();
        PostEntity post2 = new PostEntity();
        assertEquals(post1, post2);
        post1.setId(ID_1);
        post2.setId(ID_1);
        assertEquals(post1, post2);
        assertEquals(post1.hashCode(), post2.hashCode());
        post2.setId(ID_2);
        assertNotEquals(post1, post2);
    }

    @Test
    void testToString() {
        PostEntity post = new PostEntity();
        post.setId(ID_1);
        post.setImage(IMAGE);
        post.setStreak(STREAK);
        post.setPointsToAdd(POINTS);
        post.setFiledAt(FILED_AT);
        String str = post.toString();
        assertTrue(str.contains(ID_1));
        assertTrue(str.contains(IMAGE));
        assertTrue(str.contains(STREAK.toString()));
        assertTrue(str.contains(POINTS.toString()));
        assertTrue(str.contains(FILED_AT.toString()));
    }

    @Test
    void equalsToThis() {
        PostEntity post = new PostEntity();
        assertEquals(post, post);
    }

    @Test
    void equalsNotPost() {
        PostEntity post = new PostEntity();
        assertNotEquals(post, new Object());
    }
    
}
