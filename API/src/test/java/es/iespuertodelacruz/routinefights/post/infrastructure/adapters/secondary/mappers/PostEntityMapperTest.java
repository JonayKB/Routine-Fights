package es.iespuertodelacruz.routinefights.post.infrastructure.adapters.secondary.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import es.iespuertodelacruz.routinefights.activity.domain.Activity;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.entities.ActivityEntity;
import es.iespuertodelacruz.routinefights.post.domain.Post;
import es.iespuertodelacruz.routinefights.post.infrastructure.adapters.secondary.entities.PostEntity;
import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;
@SpringBootTest
class PostEntityMapperTest {

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
    private static final String FOLLOWER_USER_ID = "2L";
    private static final String FOLLOWER_USERNAME = "followerUser";
    private static final String FOLLOWING_USER_ID = "3L";
    private static final String FOLLOWING_USERNAME = "followingUser";
    private static final String PARTICIPANT_USER_ID = "4L";
    private static final String PARTICIPANT_USERNAME = "participantUser";
    private static final String ACTIVITY_ID = "10L";
    private static final String ACTIVITY_NAME = "Test Activity";
    private static final String ACTIVITY_DESCRIPTION = "Activity Description";
    private static final String ACTIVITY_IMAGE = "activityImage";
    private static final String ACTIVITY_TIME_RATE = "daily";
    private static final int ACTIVITY_TIMES_REQUIRED = 3;
    private static final String POST_ID = "100L";
    private static final String POST_IMAGE = "postImage";
    private static final int POST_STREAK = 2;
    private static final int POST_POINTS_TO_ADD = 10;
    private static final String POST2_ID = "101L";

    private final LocalDateTime now = LocalDateTime.now();
    @Autowired
    private PostEntityMapper mapper;

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

    private Post createPost() {
        Post post = new Post();
        post.id(POST_ID);
        post.setCreatedAt(now);
        post.setUpdatedAt(now);
        post.setDeletedAt(null);
        post.setImage(POST_IMAGE);
        post.setStreak(POST_STREAK);
        post.setPointsToAdd(POST_POINTS_TO_ADD);
        post.setFiledAt(now);
        post.user(createUser());
        post.setActivity(createActivity());
        return post;
    }

    @Test
    void testToEntityNullInputs() {
        assertNull(mapper.toEntity((Post) null));
        assertNull(mapper.toEntity((List<Post>) null));
    }

    @Test
    void testToDomainNullInputs() {
        assertNull(mapper.toDomain((PostEntity) null));
        assertNull(mapper.toDomain((List<PostEntity>) null));
    }

    @Test
    void testPostMapping() {
        Post post = createPost();
        PostEntity entity = mapper.toEntity(post);
        assertNotNull(entity);
        assertEquals(post.getId(), entity.getId());
        assertEquals(post.getCreatedAt(), entity.getCreatedAt());
        assertEquals(post.getUpdatedAt(), entity.getUpdatedAt());
        assertEquals(post.getImage(), entity.getImage());
        assertEquals(post.getStreak(), entity.getStreak());
        assertEquals(post.getPointsToAdd(), entity.getPointsToAdd());
        assertEquals(post.getFiledAt(), entity.getFiledAt());
        assertNotNull(entity.getUser());
        assertEquals(post.getUser().getId(), entity.getUser().getId());
        assertEquals(post.getUser().getUsername(), entity.getUser().getUsername());
        assertNotNull(entity.getActivity());
        assertEquals(post.getActivity().getId(), entity.getActivity().getId());
    }

    @Test
    void testPostEntityMapping() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(MAIN_USER_ID);
        userEntity.setUsername(MAIN_USERNAME);
        userEntity.setEmail(MAIN_EMAIL);
        userEntity.setPassword(MAIN_PASSWORD);
        userEntity.setNationality(MAIN_NATIONALITY);
        userEntity.setPhoneNumber(MAIN_PHONE_NUMBER);
        userEntity.setImage(MAIN_USER_IMAGE);
        userEntity.setRole(MAIN_ROLE);
        userEntity.setVerified(MAIN_VERIFIED);
        userEntity.setVerificationToken(MAIN_VERIFICATION_TOKEN);
        userEntity.setCreatedAt(now);
        userEntity.setUpdatedAt(now);
        userEntity.setDeletedAt(null);
        userEntity.setFollowers(new ArrayList<>());
        userEntity.setFollowing(new ArrayList<>());

        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setId(ACTIVITY_ID);
        activityEntity.setName(ACTIVITY_NAME);
        activityEntity.setDescription(ACTIVITY_DESCRIPTION);
        activityEntity.setImage(ACTIVITY_IMAGE);
        activityEntity.setTimeRate(ACTIVITY_TIME_RATE);
        activityEntity.setTimesRequiered(ACTIVITY_TIMES_REQUIRED);
        activityEntity.setCreatedAt(now);
        activityEntity.setUpdatedAt(now);
        activityEntity.setDeletedAt(null);
        activityEntity.setParticipants(new ArrayList<>());
        activityEntity.setCreator(userEntity);

        PostEntity postEntity = new PostEntity();
        postEntity.setId(POST_ID);
        postEntity.setCreatedAt(now);
        postEntity.setUpdatedAt(now);
        postEntity.setDeletedAt(null);
        postEntity.setImage(POST_IMAGE);
        postEntity.setStreak(POST_STREAK);
        postEntity.setPointsToAdd(POST_POINTS_TO_ADD);
        postEntity.setFiledAt(now);
        postEntity.setUser(userEntity);
        postEntity.setActivity(activityEntity);

        Post post = mapper.toDomain(postEntity);
        assertNotNull(post);
        assertEquals(postEntity.getId(), post.getId());
        assertEquals(postEntity.getCreatedAt(), post.getCreatedAt());
        assertEquals(postEntity.getUpdatedAt(), post.getUpdatedAt());
        assertEquals(postEntity.getImage(), post.getImage());
        assertEquals(postEntity.getStreak(), post.getStreak());
        assertEquals(postEntity.getPointsToAdd(), post.getPointsToAdd());
        assertEquals(postEntity.getFiledAt(), post.getFiledAt());
        assertNotNull(post.getUser());
        assertEquals(postEntity.getUser().getId(), post.getUser().getId());
        assertEquals(postEntity.getUser().getUsername(), post.getUser().getUsername());
        assertNotNull(post.getActivity());
        assertEquals(postEntity.getActivity().getId(), post.getActivity().getId());
    }

    @Test
    void testListMapping() {
        Post post1 = createPost();
        Post post2 = createPost();
        post2.id(POST2_ID);
        List<Post> posts = Arrays.asList(post1, post2);
        List<PostEntity> entities = mapper.toEntity(posts);
        assertNotNull(entities);
        assertEquals(2, entities.size());
        assertEquals(post1.getId(), entities.get(0).getId());
        assertEquals(post2.getId(), entities.get(1).getId());
        List<Post> postsMappedBack = mapper.toDomain(entities);
        assertNotNull(postsMappedBack);
        assertEquals(2, postsMappedBack.size());
        assertEquals(entities.get(0).getId(), postsMappedBack.get(0).getId());
        assertEquals(entities.get(1).getId(), postsMappedBack.get(1).getId());
    }

    @Test
    void testUserMappingWithFollowersAndFollowing() {
        User user = createUser();
        User follower = new User();
        follower.setId(FOLLOWER_USER_ID);
        follower.setUsername(FOLLOWER_USERNAME);
        follower.setCreatedAt(now);
        follower.setUpdatedAt(now);
        follower.setDeletedAt(null);
        follower.setFollowers(new ArrayList<>());
        follower.setFollowing(new ArrayList<>());
        User following = new User();
        following.setId(FOLLOWING_USER_ID);
        following.setUsername(FOLLOWING_USERNAME);
        following.setCreatedAt(now);
        following.setUpdatedAt(now);
        following.setDeletedAt(null);
        following.setFollowers(new ArrayList<>());
        following.setFollowing(new ArrayList<>());
        user.setFollowers(Arrays.asList(follower));
        user.setFollowing(Arrays.asList(following));
        Post post = createPost();
        post.user(user);
        PostEntity entity = mapper.toEntity(post);
        UserEntity userEntity = entity.getUser();
        assertNotNull(userEntity.getFollowers());
        assertNotNull(userEntity.getFollowing());
        assertEquals(1, userEntity.getFollowers().size());
        assertEquals(1, userEntity.getFollowing().size());
        assertEquals(FOLLOWER_USER_ID, userEntity.getFollowers().get(0).getId());
        assertEquals(FOLLOWING_USER_ID, userEntity.getFollowing().get(0).getId());
    }

    @Test
    void testActivityMappingWithParticipants() {
        Activity activity = createActivity();
        User participant = createUser();
        participant.setId(PARTICIPANT_USER_ID);
        participant.setUsername(PARTICIPANT_USERNAME);
        activity.participants(Arrays.asList(participant));
        Post post = createPost();
        post.setActivity(activity);
        PostEntity entity = mapper.toEntity(post);
        ActivityEntity activityEntity = entity.getActivity();
        assertNotNull(activityEntity.getParticipants());
        assertEquals(1, activityEntity.getParticipants().size());
        assertEquals(PARTICIPANT_USER_ID, activityEntity.getParticipants().get(0).getId());
    }

    @Test
    void testPostMappingWithNullNestedObjects() {
        Post post = createPost();
        post.user(null);
        PostEntity entity = mapper.toEntity(post);
        assertNull(entity.getUser());
        post = createPost();
        post.setActivity(null);
        entity = mapper.toEntity(post);
        assertNull(entity.getActivity());
    }
}