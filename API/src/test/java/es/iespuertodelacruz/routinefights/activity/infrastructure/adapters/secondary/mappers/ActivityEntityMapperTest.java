package es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import es.iespuertodelacruz.routinefights.activity.domain.Activity;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.entities.ActivityEntity;
import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.mappers.IUserEntityMapper;

class ActivityEntityMapperTest {

    private static final String MAIN_USER_ID = "1L";
    private static final String MAIN_USERNAME = "testUser";
    private static final String MAIN_EMAIL = "test@example.com";
    private static final String MAIN_PASSWORD = "password";
    private static final String MAIN_NATIONALITY = "Testland";
    private static final String MAIN_PHONE_NUMBER = "1234567890";
    private static final String MAIN_USER_IMAGE = "userImage";
    private static final String MAIN_ROLE = "USER";
    private static final boolean MAIN_VERIFIED = true;
    private static final String MAIN_VERIFICATION_TOKEN = "token123";

    private static final String ACTIVITY_ID = "10L";
    private static final String ACTIVITY_NAME = "Test Activity";
    private static final String ACTIVITY_DESCRIPTION = "Activity Description";
    private static final String ACTIVITY_IMAGE = "activityImage";
    private static final String ACTIVITY_TIME_RATE = "daily";
    private static final int ACTIVITY_TIMES_REQUIRED = 3;

    private final LocalDateTime now = LocalDateTime.now();

    private final ActivityEntityMapperImpl mapper = new ActivityEntityMapperImpl();

    // Inject dummy IUserEntityMapper using ReflectionTestUtils
    {
        ReflectionTestUtils.setField(mapper, "iUserEntityMapper", new DummyUserEntityMapper());
    }

    // Dummy implementation for IUserEntityMapper
    static class DummyUserEntityMapper implements IUserEntityMapper {

        @Override
        public User toDomain(UserEntity userEntity) {
            if (userEntity == null) {
                return null;
            }
            User user = new User();
            user.setId(userEntity.getId());
            user.setUsername(userEntity.getUsername());
            return user;
        }

        @Override
        public List<User> toDomain(List<UserEntity> userEntities) {
            if (userEntities == null) {
                return null;
            }
            List<User> users = new ArrayList<>();
            for (UserEntity ue : userEntities) {
                users.add(toDomain(ue));
            }
            return users;
        }

        @Override
        public UserEntity toEntity(User user) {
            if (user == null) {
                return null;
            }
            UserEntity ue = new UserEntity();
            ue.setId(user.getId());
            ue.setUsername(user.getUsername());
            return ue;
        }

        @Override
        public List<UserEntity> toEntity(List<User> users) {
            if (users == null) {
                return null;
            }
            List<UserEntity> list = new ArrayList<>();
            for (User u : users) {
                list.add(toEntity(u));
            }
            return list;
        }
    }

    private User createUser() {
        User user = new User();
        user.setId(MAIN_USER_ID);
        user.setUsername(MAIN_USERNAME);
        user.setEmail(MAIN_EMAIL);
        user.setPassword(MAIN_PASSWORD);
        user.setNationality(MAIN_NATIONALITY);
        user.setPhoneNumber(MAIN_PHONE_NUMBER);
        user.setImage(MAIN_USER_IMAGE);
        user.setRole(MAIN_ROLE);
        user.setVerified(MAIN_VERIFIED);
        user.setVerificationToken(MAIN_VERIFICATION_TOKEN);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        user.setDeletedAt(null);
        user.setFollowers(new ArrayList<>());
        user.setFollowing(new ArrayList<>());
        return user;
    }

    private UserEntity createUserEntity() {
        UserEntity ue = new UserEntity();
        ue.setId(MAIN_USER_ID);
        ue.setUsername(MAIN_USERNAME);
        ue.setEmail(MAIN_EMAIL);
        ue.setPassword(MAIN_PASSWORD);
        ue.setNationality(MAIN_NATIONALITY);
        ue.setPhoneNumber(MAIN_PHONE_NUMBER);
        ue.setImage(MAIN_USER_IMAGE);
        ue.setRole(MAIN_ROLE);
        ue.setVerified(MAIN_VERIFIED);
        ue.setVerificationToken(MAIN_VERIFICATION_TOKEN);
        ue.setCreatedAt(now);
        ue.setUpdatedAt(now);
        ue.setDeletedAt(null);
        ue.setFollowers(new ArrayList<>());
        ue.setFollowing(new ArrayList<>());
        return ue;
    }

    private Activity createActivity() {
        Activity activity = new Activity();
        activity.setId(ACTIVITY_ID);
        activity.setName(ACTIVITY_NAME);
        activity.setDescription(ACTIVITY_DESCRIPTION);
        activity.setImage(ACTIVITY_IMAGE);
        activity.setTimeRate(ACTIVITY_TIME_RATE);
        activity.setTimesRequiered(ACTIVITY_TIMES_REQUIRED);
        activity.setCreatedAt(now);
        activity.setUpdatedAt(now);
        activity.setDeletedAt(null);
        activity.participants(new ArrayList<>());
        activity.setCreator(createUser());
        return activity;
    }

    private ActivityEntity createActivityEntity() {
        ActivityEntity entity = new ActivityEntity();
        entity.setId(ACTIVITY_ID);
        entity.setName(ACTIVITY_NAME);
        entity.setDescription(ACTIVITY_DESCRIPTION);
        entity.setImage(ACTIVITY_IMAGE);
        entity.setTimeRate(ACTIVITY_TIME_RATE);
        entity.setTimesRequiered(ACTIVITY_TIMES_REQUIRED);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        entity.setDeletedAt(null);
        entity.setParticipants(new ArrayList<>());
        entity.setCreator(createUserEntity());
        return entity;
    }

    @Test
    void testToDomain_NullInputs() {
        assertNull(mapper.toDomain((ActivityEntity) null));
        assertNull(mapper.toDomain((List<ActivityEntity>) null));
    }

    @Test
    void testToEntity_NullInputs() {
        assertNull(mapper.toEntity((Activity) null));
        assertNull(mapper.toEntity((List<Activity>) null));
    }

    @Test
    void testActivityMapping() {
        Activity activity = createActivity();
        ActivityEntity entity = mapper.toEntity(activity);
        assertNotNull(entity);
        assertEquals(activity.getId(), entity.getId());
        assertEquals(activity.getName(), entity.getName());
        assertEquals(activity.getDescription(), entity.getDescription());
        assertEquals(activity.getImage(), entity.getImage());
        assertEquals(activity.getTimeRate(), entity.getTimeRate());
        assertEquals(activity.getTimesRequiered(), entity.getTimesRequiered());
        assertEquals(activity.getCreatedAt(), entity.getCreatedAt());
        assertEquals(activity.getUpdatedAt(), entity.getUpdatedAt());
        assertEquals(activity.getDeletedAt(), entity.getDeletedAt());
        // Check nested mapping via dummy mapper
        assertNotNull(entity.getCreator());
        assertEquals(activity.getCreator().getId(), entity.getCreator().getId());
        // participants list (even if empty)
        assertNotNull(entity.getParticipants());
    }

    @Test
    void testActivityEntityMapping() {
        ActivityEntity entity = createActivityEntity();
        Activity activity = mapper.toDomain(entity);
        assertNotNull(activity);
        assertEquals(entity.getId(), activity.getId());
        assertEquals(entity.getName(), activity.getName());
        assertEquals(entity.getDescription(), activity.getDescription());
        assertEquals(entity.getImage(), activity.getImage());
        assertEquals(entity.getTimeRate(), activity.getTimeRate());
        assertEquals(entity.getTimesRequiered(), activity.getTimesRequiered());
        assertEquals(entity.getCreatedAt(), activity.getCreatedAt());
        assertEquals(entity.getUpdatedAt(), activity.getUpdatedAt());
        assertEquals(entity.getDeletedAt(), activity.getDeletedAt());
        // Check nested mapping via dummy mapper
        assertNotNull(activity.getCreator());
        assertEquals(entity.getCreator().getId(), activity.getCreator().getId());
        // participants list (even if empty)
        assertNotNull(activity.getParticipants());
    }

    @Test
    void testListMapping() {
        Activity activity1 = createActivity();
        Activity activity2 = createActivity();
        activity2.setId("20L");
        List<Activity> activities = Arrays.asList(activity1, activity2);
        List<ActivityEntity> entities = mapper.toEntity(activities);
        assertNotNull(entities);
        assertEquals(2, entities.size());
        assertEquals(activity1.getId(), entities.get(0).getId());
        assertEquals(activity2.getId(), entities.get(1).getId());

        List<Activity> activitiesMappedBack = mapper.toDomain(entities);
        assertNotNull(activitiesMappedBack);
        assertEquals(2, activitiesMappedBack.size());
        assertEquals(entities.get(0).getId(), activitiesMappedBack.get(0).getId());
        assertEquals(entities.get(1).getId(), activitiesMappedBack.get(1).getId());
    }
}
