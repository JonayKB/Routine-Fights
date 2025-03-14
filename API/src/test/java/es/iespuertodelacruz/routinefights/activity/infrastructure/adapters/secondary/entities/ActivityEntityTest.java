package es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.entities;

import org.junit.jupiter.api.Test;

import es.iespuertodelacruz.routinefights.category.infrastructure.adapters.secondary.entities.CategoryEntity;
import es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.secondary.entities.CommunityEventEntity;
import es.iespuertodelacruz.routinefights.post.infrastructure.adapters.secondary.entities.PostEntity;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ActivityEntityTest {

    private static final String ID_1 = "A1";
    private static final String ID_2 = "A2";
    private static final String NAME = "Test Activity";
    private static final String DESCRIPTION = "Activity Description";
    private static final String IMAGE = "http://example.com/activity.png";
    private static final String TIME_RATE = "Hourly";
    private static final Integer TIMES_REQUIRED = 5;
    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2023, 1, 2, 12, 0);
    private static final LocalDateTime UPDATED_AT = LocalDateTime.of(2023, 1, 3, 12, 0);
    private static final LocalDateTime DELETED_AT = LocalDateTime.of(2023, 1, 4, 12, 0);

    @Test
    void testSettersAndGetters() {
        ActivityEntity activity = new ActivityEntity();
        activity.setId(ID_1);
        activity.setName(NAME);
        activity.setDescription(DESCRIPTION);
        activity.setImage(IMAGE);
        activity.setTimeRate(TIME_RATE);
        activity.setTimesRequiered(TIMES_REQUIRED);
        CategoryEntity category = new CategoryEntity();
        activity.setCategory(category);
        List<CommunityEventEntity> communityEvents = new ArrayList<>();
        activity.setCommunityEvent(communityEvents);
        UserEntity user = new UserEntity();
        activity.setUser(user);
        List<PostEntity> posts = new ArrayList<>();
        activity.setPost(posts);
        assertEquals(ID_1, activity.getId());
        assertEquals(NAME, activity.getName());
        assertEquals(DESCRIPTION, activity.getDescription());
        assertEquals(IMAGE, activity.getImage());
        assertEquals(TIME_RATE, activity.getTimeRate());
        assertEquals(TIMES_REQUIRED, activity.getTimesRequiered());
        assertEquals(category, activity.getCategory());
        assertEquals(communityEvents, activity.getCommunityEvent());
        assertEquals(user, activity.getUser());
        assertEquals(posts, activity.getPost());
    }

    @Test
    void testConstructorAndGetters() {
        CategoryEntity category = new CategoryEntity();
        List<CommunityEventEntity> communityEvents = new ArrayList<>();
        UserEntity user = new UserEntity();
        List<PostEntity> posts = new ArrayList<>();
        ActivityEntity activity = new ActivityEntity(ID_1, NAME, DESCRIPTION, IMAGE, TIME_RATE, TIMES_REQUIRED,
                category, communityEvents, user, posts, CREATED_AT, UPDATED_AT, DELETED_AT);
        assertEquals(ID_1, activity.getId());
        assertEquals(NAME, activity.getName());
        assertEquals(DESCRIPTION, activity.getDescription());
        assertEquals(IMAGE, activity.getImage());
        assertEquals(TIME_RATE, activity.getTimeRate());
        assertEquals(TIMES_REQUIRED, activity.getTimesRequiered());
        assertEquals(category, activity.getCategory());
        assertEquals(communityEvents, activity.getCommunityEvent());
        assertEquals(user, activity.getUser());
        assertEquals(posts, activity.getPost());
        assertEquals(CREATED_AT, activity.getCreatedAt());
        assertEquals(UPDATED_AT, activity.getUpdatedAt());
        assertEquals(DELETED_AT, activity.getDeletedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        ActivityEntity activity1 = new ActivityEntity();
        ActivityEntity activity2 = new ActivityEntity();
        assertEquals(activity1, activity2);
        activity1.setId(ID_1);
        activity2.setId(ID_1);
        assertEquals(activity1, activity2);
        assertEquals(activity1.hashCode(), activity2.hashCode());
        activity2.setId(ID_2);
        assertNotEquals(activity1, activity2);
        assertNotEquals(activity1.hashCode(), activity2.hashCode());
        assertNotEquals(activity1, new Object());
        assertEquals(activity1, activity1);
    }

    @Test
    void testToString() {
        ActivityEntity activity = new ActivityEntity();
        activity.setId(ID_1);
        activity.setName(NAME);
        activity.setDescription(DESCRIPTION);
        activity.setImage(IMAGE);
        activity.setTimeRate(TIME_RATE);
        activity.setTimesRequiered(TIMES_REQUIRED);
        String str = activity.toString();
        assertTrue(str.contains(ID_1));
        assertTrue(str.contains(NAME));
        assertTrue(str.contains(DESCRIPTION));
        assertTrue(str.contains(IMAGE));
        assertTrue(str.contains(TIME_RATE));
        assertTrue(str.contains(TIMES_REQUIRED.toString()));
    }
}
