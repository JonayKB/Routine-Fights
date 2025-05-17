package es.iespuertodelacruz.routinefights.activity.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import es.iespuertodelacruz.routinefights.user.domain.User;

 class ActivityTest {

    private static final String CONSTANT_ID = "A1";
    private static final String CONSTANT_ID_2 = "A2";


    @Test
     void testSettersAndGetters() {
        User dummyUser = new User();
        List<User> initialParticipants = new ArrayList<>();
        Activity activity = new Activity();
        activity.setId(CONSTANT_ID);
        assertEquals(CONSTANT_ID, activity.getId());
        activity.id(CONSTANT_ID_2);
        assertEquals(CONSTANT_ID_2, activity.getId());
        activity.setCreator(dummyUser);
        assertEquals(dummyUser, activity.getCreator());
        User anotherUser = new User();
        activity.creator(anotherUser);
        assertEquals(anotherUser, activity.getCreator());
        activity.setParticipants(initialParticipants);
        assertEquals(initialParticipants, activity.getParticipants());
        List<User> updatedParticipants = new ArrayList<>();
        updatedParticipants.add(dummyUser);
        activity.participants(updatedParticipants);
        assertEquals(updatedParticipants, activity.getParticipants());
    }

    @Test
     void testEqualsSameInstance() {
        User dummyUser = new User();
        List<User> participants = Collections.emptyList();
        Activity activity = new Activity(CONSTANT_ID, dummyUser, participants);
        assertEquals(activity, activity);
    }

    @Test
     void testEqualsSameIdDifferentInstances() {
        User dummyUser1 = new User();
        User dummyUser2 = new User();
        List<User> participants = Collections.emptyList();
        Activity activity1 = new Activity(CONSTANT_ID, dummyUser1, participants);
        Activity activity2 = new Activity(CONSTANT_ID, dummyUser2, participants);
        assertEquals(activity1, activity2);
        assertEquals(activity2, activity1);
        assertEquals(activity1.hashCode(), activity2.hashCode());
    }

    @Test
     void testEqualsDifferentId() {
        User dummyUser = new User();
        List<User> participants = Collections.emptyList();
        Activity activity1 = new Activity(CONSTANT_ID, dummyUser, participants);
        Activity activity2 = new Activity(CONSTANT_ID_2, dummyUser, participants);
        assertNotEquals(activity1, activity2);
        assertNotEquals(activity2, activity1);
    }


    @Test
     void testEqualsDifferentType() {
        User dummyUser = new User();
        List<User> participants = Collections.emptyList();
        Activity activity = new Activity(CONSTANT_ID, dummyUser, participants);
        String nonActivity = "Not an Activity";
        assertNotEquals(activity, nonActivity);
    }

    @Test
     void testToString() {
        User dummyUser = new User();
        List<User> participants = Collections.emptyList();
        Activity activity = new Activity(CONSTANT_ID, dummyUser, participants);
        String expected = "{ id='" + CONSTANT_ID + "'}";
        assertEquals(expected, activity.toString());
    }
}

