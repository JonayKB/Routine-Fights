package es.iespuertodelacruz.routinefights.communityEvent.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.iespuertodelacruz.routinefights.activity.domain.Activity;
import es.iespuertodelacruz.routinefights.community_event.commons.CommunityEventCommons;
import es.iespuertodelacruz.routinefights.community_event.domain.CommunityEvent;

class CommunityEventTests {

    private CommunityEvent event;
    private CommunityEventCommons commons;
    private LocalDateTime now;
    private LocalDateTime later;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.of(2025, 5, 16, 10, 0);
        later = now.plusDays(1);
        List<Activity> activities = Arrays.asList(
                new Activity("A1", "Activity One", null, null, null, null, later, later, later, null, null),
                new Activity("A2", "Activity Two", null, null, null, null, later, later, later, null, null));
        event = new CommunityEvent(
                "evt1", "Test Event", 5,
                now, later,
                now.minusDays(1), now, null,
                activities, "image.png");
        commons = new CommunityEventCommons("Common Name", 3, now, later);
    }

    @Test
    void testCommunityEventGettersAndSetters() {
        assertEquals("evt1", event.getId());
        event.setId("evt2");
        assertEquals("evt2", event.getId());

        assertNotNull(event.getActivities());
        assertEquals(2, event.getActivities().size());
        List<Activity> newActs = Arrays.asList(new Activity("B1", "New Act", null, null, null, null, later, later, later, null, null));
        event.setActivities(newActs);
        assertEquals(1, event.getActivities().size());
        assertEquals("B1", event.getActivities().get(0).getId());
    }

    @Test
    void testCommunityEventCommonsFluentSetters() {
        CommunityEventCommons fluent = new CommunityEventCommons()
                .name("Fluent Name")
                .totalRequired(10)
                .startDate(now)
                .finishDate(later);

        assertEquals("Fluent Name", fluent.getName());
        assertEquals(10, fluent.getTotalRequired());
        assertEquals(now, fluent.getStartDate());
        assertEquals(later, fluent.getFinishDate());
    }

    @Test
    void testToString() {
        String str = commons.toString();
        assertTrue(str.contains("Common Name"));
        assertTrue(str.contains("3"));
        assertTrue(str.contains(now.toString()));
        assertTrue(str.contains(later.toString()));
    }

    @Test
    void testCommonsGettersAndSetters() {
        assertEquals("Common Name", commons.getName());
        commons.setName("Other Name");
        assertEquals("Other Name", commons.getName());

        assertEquals(3, commons.getTotalRequired());
        commons.setTotalRequired(7);
        assertEquals(7, commons.getTotalRequired());

        assertEquals(now, commons.getStartDate());
        LocalDateTime newStart = now.plusHours(2);
        commons.setStartDate(newStart);
        assertEquals(newStart, commons.getStartDate());

        assertEquals(later, commons.getFinishDate());
        LocalDateTime newFinish = later.plusHours(3);
        commons.setFinishDate(newFinish);
        assertEquals(newFinish, commons.getFinishDate());
    }

    @Test
    void testImageProperty() {
        CommunityEventCommons withImage = new CommunityEventCommons(
                "Name", 1, now, later,
                now, now, null, "pic.jpg");
        assertEquals("pic.jpg", withImage.getImage());
        withImage.setImage("newpic.png");
        assertEquals("newpic.png", withImage.getImage());
    }
}
