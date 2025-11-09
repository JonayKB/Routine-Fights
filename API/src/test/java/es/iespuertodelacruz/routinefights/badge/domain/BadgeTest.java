package es.iespuertodelacruz.routinefights.badge.domain;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.iespuertodelacruz.routinefights.community_event.domain.CommunityEvent;
import es.iespuertodelacruz.routinefights.user.domain.User;

class BadgeTests {

    private static final String ID = "badge1";
    private static final String IMAGE = "icon.png";
    private static final Integer LEVEL = 2;

    private Badge badge;
    private List<User> users;
    private CommunityEvent event;

    @BeforeEach
    void setUp() {
        User user = new User();
        users = Collections.singletonList(user);

        event = new CommunityEvent("evt1", "EventName", 1,
                null, null, null, null, null,
                Collections.emptyList(), "img.jpg");

        badge = new Badge(ID, IMAGE, LEVEL, users, event);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(ID, badge.getId());
        badge.setId("other");
        assertEquals("other", badge.getId());

        assertEquals(IMAGE, badge.getImage());
        badge.setImage("new.png");
        assertEquals("new.png", badge.getImage());

        assertEquals(LEVEL, badge.getLevel());
        badge.setLevel(5);
        assertEquals(Integer.valueOf(5), badge.getLevel());

        assertEquals(users, badge.getUsers());
        badge.setUsers(Collections.emptyList());
        assertTrue(badge.getUsers().isEmpty());

        assertEquals(event, badge.getCommunityEvent());
        CommunityEvent another = new CommunityEvent("e2", "Other", 3,
                null, null, null, null, null,
                Collections.emptyList(), "img2.jpg");
        badge.setCommunityEvent(another);
        assertEquals(another, badge.getCommunityEvent());
    }

    @Test
    void testToString() {
        String str = badge.toString();
        assertTrue(str.contains("Badge{"));
        assertTrue(str.contains(ID));
        assertTrue(str.contains(IMAGE));
        assertTrue(str.contains(LEVEL.toString()));
    }

    @Test
    void testEqualsAndHashCode() {
        Badge same = new Badge(ID, IMAGE, LEVEL, users, event);
        assertEquals(badge, same);
        assertEquals(badge.hashCode(), same.hashCode());

        Badge diffId = new Badge("other", IMAGE, LEVEL, users, event);
        assertNotEquals(badge, diffId);
    }
}

