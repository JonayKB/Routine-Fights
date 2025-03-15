package es.iespuertodelacruz.routinefights.post.domain;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import es.iespuertodelacruz.routinefights.activity.domain.Activity;
import es.iespuertodelacruz.routinefights.user.domain.User;

import java.time.LocalDateTime;

 class PostTest {

    @Test
     void testPostConstructorAndGetters() {
        String id = "123";
        String image = "image.png";
        int streak = 5;
        int pointsToAdd = 10;
        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        Activity activity = new Activity("activity1");
        Post test = new Post(id,user);

        assertEquals(id, test.getId());
        assertEquals(user, test.getUser());

        Post post = new Post(id, image, streak, pointsToAdd, now, now, null, null, user, activity);

        assertEquals(id, post.getId());
        assertEquals(image, post.getImage());
        assertEquals(streak, post.getStreak());
        assertEquals(pointsToAdd, post.getPointsToAdd());
        assertEquals(now, post.getCreatedAt());
        assertEquals(now, post.getUpdatedAt());
        assertNull(post.getDeletedAt());
        assertNull(post.getFiledAt());
        assertEquals(user, post.getUser());
        assertEquals(activity, post.getActivity());
    }

    @Test
     void testSetters() {
        Post post = new Post();

        String id = "123";
        String image = "image.png";
        int streak = 5;
        int pointsToAdd = 10;
        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        Activity activity = new Activity();

        post.setId(id);
        post.setImage(image);
        post.setStreak(streak);
        post.setPointsToAdd(pointsToAdd);
        post.setCreatedAt(now);
        post.setUpdatedAt(now);
        post.setDeletedAt(null);
        post.setFiledAt(null);
        post.setUser(user);
        post.setActivity(activity);

        assertEquals(id, post.getId());
        assertEquals(image, post.getImage());
        assertEquals(streak, post.getStreak());
        assertEquals(pointsToAdd, post.getPointsToAdd());
        assertEquals(now, post.getCreatedAt());
        assertEquals(now, post.getUpdatedAt());
        assertNull(post.getDeletedAt());
        assertNull(post.getFiledAt());
        assertEquals(user, post.getUser());
        assertEquals(activity, post.getActivity());
    }

    @Test
     void testEqualsAndHashCode() {
        String id1 = "123";
        String id2 = "456";
        Post post1 = new Post(id1);
        Post post2 = new Post(id1);
        Post post3 = new Post(id2);

        assertEquals(post1, post2);
        assertNotEquals(post1, post3);

        assertEquals(post1.hashCode(), post2.hashCode());
        assertNotEquals(post1.hashCode(), post3.hashCode());
    }

    @Test
     void testToString() {
        String id = "123";
        Post post = new Post(id);

        assertEquals("{ id='123'}", post.toString());
    }
}
