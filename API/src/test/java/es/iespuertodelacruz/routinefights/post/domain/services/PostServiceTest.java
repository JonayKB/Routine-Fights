package es.iespuertodelacruz.routinefights.post.domain.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import es.iespuertodelacruz.routinefights.activity.domain.Activity;
import es.iespuertodelacruz.routinefights.activity.domain.ports.secondary.IActivityRepository;
import es.iespuertodelacruz.routinefights.post.domain.Post;
import es.iespuertodelacruz.routinefights.post.domain.ports.secondary.IPostRepository;
import es.iespuertodelacruz.routinefights.post.exceptions.UploadPostException;
import es.iespuertodelacruz.routinefights.user.domain.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import es.iespuertodelacruz.routinefights.activity.commons.TimeRates;

class PostServiceTest {

        private static final List<Post> EMPTY_LIST = Collections.emptyList();
        private static final String ACTIVITY1 = "activity1";
        private static final String ACTIVITY_NOT_FOUND_MSG = "Activity not found";
        private static final String USER_NOT_ON_ACTIVITY_MSG = "User is not on activity";
        private static final String ACTIVITY_COMPLETED_MSG = "You have already completed this activity";
        @Mock
        private IPostRepository postRepository;
        @Mock
        private IActivityRepository activityRepository;
        @InjectMocks
        private PostService postService;

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
        }

        @Test
        void testGetPagination() {
                LocalDateTime lastDate = LocalDateTime.of(2025, 3, 15, 0, 0);
                int limit = 10;
                List<Post> expectedPosts = Arrays.asList(new Post(), new Post());
                Mockito.when(postRepository.getPagination(lastDate, limit)).thenReturn(expectedPosts);
                List<Post> result = postService.getPagination(lastDate, limit);
                assertSame(expectedPosts, result);
        }

        @Test
        void testUploadPost_ActivityNotFound() {
                String activityID = ACTIVITY1;
                User user = new User();
                user.setId("1L");
                Mockito.when(activityRepository.findById(activityID)).thenReturn(null);
                UploadPostException ex = assertThrows(UploadPostException.class,
                                () -> postService.uploadPost("image.png", user, activityID));
                assertEquals(ACTIVITY_NOT_FOUND_MSG, ex.getMessage());
        }

        @Test
        void testUploadPost_UserNotOnActivity() {
                String activityID = ACTIVITY1;
                User user = new User();
                user.setId("1L");
                Activity activity = new Activity();
                activity.setTimeRate(TimeRates.DAILY);
                activity.setTimesRequiered(3);
                Mockito.when(activityRepository.findById(activityID)).thenReturn(activity);
                Mockito.when(activityRepository.userIsOnActivity(user.getId(), activityID)).thenReturn(false);
                UploadPostException ex = assertThrows(UploadPostException.class,
                                () -> postService.uploadPost("image.png", user, activityID));
                assertEquals(USER_NOT_ON_ACTIVITY_MSG, ex.getMessage());
        }

        @Test
        void testUploadPost_AlreadyCompleted() {
                String activityID = ACTIVITY1;
                User user = new User();
                user.setId("1L");
                Activity activity = new Activity();
                activity.setTimeRate(TimeRates.DAILY);
                activity.setTimesRequiered(3);
                Mockito.when(activityRepository.findById(activityID)).thenReturn(activity);
                Mockito.when(activityRepository.userIsOnActivity(user.getId(), activityID)).thenReturn(true);
                List<Post> actualPosts = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                        Post p = new Post();
                        p.setStreak(i + 1);
                        actualPosts.add(p);
                }
                Mockito.when(postRepository.getRangeByActivity(eq(activityID), eq(user.getId()), any(), any()))
                                .thenReturn(actualPosts);
                UploadPostException ex = assertThrows(UploadPostException.class,
                                () -> postService.uploadPost("image.png", user, activityID));
                assertEquals(ACTIVITY_COMPLETED_MSG, ex.getMessage());
        }

        @Test
        void testUploadPost_ActualPeriodEmpty_LastPeriodComplete() {
                String activityID = ACTIVITY1;
                User user = new User();
                user.setId("1L");
                Activity activity = new Activity();
                activity.setTimeRate(TimeRates.DAILY);
                activity.setTimesRequiered(3);
                Mockito.when(activityRepository.findById(activityID)).thenReturn(activity);
                Mockito.when(activityRepository.userIsOnActivity(user.getId(), activityID)).thenReturn(true);
                List<Post> lastPeriodPosts = new ArrayList<>();
                Post p1 = new Post();
                p1.setStreak(3);
                Post p2 = new Post();
                p2.setStreak(4);
                Post p3 = new Post();
                p3.setStreak(5);
                lastPeriodPosts.add(p1);
                lastPeriodPosts.add(p2);
                lastPeriodPosts.add(p3);
                Mockito.when(postRepository.getRangeByActivity(eq(activityID), eq(user.getId()), any(), any()))
                                .thenReturn(EMPTY_LIST, lastPeriodPosts);
                Mockito.when(postRepository.save(any(Post.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));
                Post result = postService.uploadPost("image.png", user, activityID);
                assertNotNull(result);
                assertEquals(6, result.getStreak());
                assertEquals(100 / activity.getTimesRequiered(), result.getPointsToAdd());
        }

        @Test
        void testUploadPost_ActualPeriodEmpty_LastPeriodIncomplete() {
                String activityID = ACTIVITY1;
                User user = new User();
                user.setId("1L");
                Activity activity = new Activity();
                activity.setTimeRate(TimeRates.DAILY);
                activity.setTimesRequiered(3);
                Mockito.when(activityRepository.findById(activityID)).thenReturn(activity);
                Mockito.when(activityRepository.userIsOnActivity(user.getId(), activityID)).thenReturn(true);
                List<Post> lastPeriodPosts = new ArrayList<>();
                Post p1 = new Post();
                p1.setStreak(2);
                lastPeriodPosts.add(p1);
                Mockito.when(postRepository.getRangeByActivity(eq(activityID), eq(user.getId()), any(), any()))
                                .thenReturn(EMPTY_LIST, lastPeriodPosts);
                Mockito.when(postRepository.save(any(Post.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));
                Post result = postService.uploadPost("image.png", user, activityID);
                assertNotNull(result);
                assertEquals(1, result.getStreak());
                assertEquals(100 / activity.getTimesRequiered(), result.getPointsToAdd());
        }

        @Test
        void testUploadPost_ActualPeriodNotEmpty() {
                String activityID = ACTIVITY1;
                User user = new User();
                user.setId("1L");
                Activity activity = new Activity();
                activity.setTimeRate(TimeRates.DAILY);
                activity.setTimesRequiered(3);
                Mockito.when(activityRepository.findById(activityID)).thenReturn(activity);
                Mockito.when(activityRepository.userIsOnActivity(user.getId(), activityID)).thenReturn(true);
                List<Post> actualPosts = new ArrayList<>();
                Post p = new Post();
                p.setStreak(4);
                actualPosts.add(p);
                Mockito.when(postRepository.getRangeByActivity(eq(activityID), eq(user.getId()), any(), any()))
                                .thenReturn(actualPosts);
                Mockito.when(postRepository.save(any(Post.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));
                Post result = postService.uploadPost("image.png", user, activityID);
                assertNotNull(result);
                assertEquals(4, result.getStreak());
                assertEquals(100 / activity.getTimesRequiered(), result.getPointsToAdd());
        }

        @Test
        void testUploadPost_PointsToAddWeekly() {
                String activityID = "activityWeekly";
                User user = new User();
                user.setId("1L");
                Activity activity = new Activity();
                activity.setTimeRate(TimeRates.WEEKLY);
                activity.setTimesRequiered(3);
                Mockito.when(activityRepository.findById(activityID)).thenReturn(activity);
                Mockito.when(activityRepository.userIsOnActivity(user.getId(), activityID)).thenReturn(true);
                Mockito.when(postRepository.getRangeByActivity(eq(activityID), eq(user.getId()), any(), any()))
                                .thenReturn(EMPTY_LIST, EMPTY_LIST);
                Mockito.when(postRepository.save(any(Post.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));
                Post result = postService.uploadPost("imageWeekly.png", user, activityID);
                assertNotNull(result);
                assertEquals(1, result.getStreak());
                assertEquals(700 / activity.getTimesRequiered(), result.getPointsToAdd());
        }

        @Test
        void testUploadPost_PointsToAddMonthly() {
                String activityID = "activityMonthly";
                User user = new User();
                user.setId("1L");
                Activity activity = new Activity();
                activity.setTimeRate(TimeRates.MONTHLY);
                activity.setTimesRequiered(3);
                Mockito.when(activityRepository.findById(activityID)).thenReturn(activity);
                Mockito.when(activityRepository.userIsOnActivity(user.getId(), activityID)).thenReturn(true);
                Mockito.when(postRepository.getRangeByActivity(eq(activityID), eq(user.getId()), any(), any()))
                                .thenReturn(EMPTY_LIST, EMPTY_LIST);
                Mockito.when(postRepository.save(any(Post.class)))

                                .thenAnswer(invocation -> invocation.getArgument(0));
                Post result = postService.uploadPost("imageMonthly.png", user, activityID);
                assertNotNull(result);
                assertEquals(1, result.getStreak());
                assertEquals(3000 / activity.getTimesRequiered(), result.getPointsToAdd());
        }

        @Test
        void testUploadPost_PointsToAddYearly() {
                String activityID = "activityYearly";
                User user = new User();
                user.setId("1L");
                Activity activity = new Activity();
                activity.setTimeRate(TimeRates.YEARLY);
                activity.setTimesRequiered(3);
                Mockito.when(activityRepository.findById(activityID)).thenReturn(activity);
                Mockito.when(activityRepository.userIsOnActivity(user.getId(), activityID)).thenReturn(true);
                Mockito.when(postRepository.getRangeByActivity(eq(activityID), eq(user.getId()), any(), any()))
                                .thenReturn(EMPTY_LIST, EMPTY_LIST);
                Mockito.when(postRepository.save(any(Post.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));
                Post result = postService.uploadPost("imageYearly.png", user, activityID);
                assertNotNull(result);
                assertEquals(1, result.getStreak());
                assertEquals(36500 / activity.getTimesRequiered(), result.getPointsToAdd());
        }

        @Test
        void testUploadPost_InvalidTimeRate(){
                String activityID = "activityInvalidTimeRate";
                User user = new User();
                user.setId("1L");
                Activity activity = new Activity();
                activity.setTimeRate("PEPE");
                activity.setTimesRequiered(3);
                Mockito.when(activityRepository.findById(activityID)).thenReturn(activity);
                Mockito.when(activityRepository.userIsOnActivity(user.getId(), activityID)).thenReturn(true);
                Mockito.when(postRepository.getRangeByActivity(eq(activityID), eq(user.getId()), any(), any()))
                                .thenReturn(EMPTY_LIST, EMPTY_LIST);
                Mockito.when(postRepository.save(any(Post.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));
                UploadPostException ex = assertThrows(UploadPostException.class,
                                                () -> postService.uploadPost("imageInvalidTimeRate.png", user, activityID));
                assertEquals("Invalid time rate", ex.getMessage());
        }

}