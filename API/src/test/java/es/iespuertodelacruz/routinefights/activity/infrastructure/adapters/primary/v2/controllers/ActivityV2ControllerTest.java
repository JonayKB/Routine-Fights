package es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.primary.v2.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import es.iespuertodelacruz.routinefights.activity.domain.Activity;
import es.iespuertodelacruz.routinefights.activity.domain.ports.primary.IActivityService;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.primary.v2.dtos.ActivityInputV2;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.primary.v2.dtos.ActivityOutputV2;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.primary.v2.dtos.ActivityOutputV2Streak;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.primary.v2.mappers.ActivityOutputV2Mapper;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.primary.v2.mappers.ActivityOutputV2StreakMapper;
import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.domain.ports.primary.IUserService;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions.UserNotFoundException;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.dtos.UserOutputDTOV2;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ActivityControllerV2Test {

    private ActivityControllerV2 activityControllerV2;

    private IActivityService activityService;
    private ActivityOutputV2Mapper activityOutputV2Mapper;
    private ActivityOutputV2StreakMapper activityOutputV2StreakMapper;
    private IUserService userService;

    private ActivityInputV2 activityInputV2;
    private Activity testActivity;
    private ActivityOutputV2 activityOutputV2;
    private User testUser;

    @BeforeEach
    void setUp() {
        activityService = mock(IActivityService.class);
        activityOutputV2Mapper = mock(ActivityOutputV2Mapper.class);
        activityOutputV2StreakMapper = mock(ActivityOutputV2StreakMapper.class);
        userService = mock(IUserService.class);

        activityControllerV2 = new ActivityControllerV2(activityService, activityOutputV2Mapper, userService,
                activityOutputV2StreakMapper);

        activityInputV2 = new ActivityInputV2("Test Activity", "Test Description", "test-image.png", "weekly", 3,
                "100L");
        testUser = new User();
        testUser.setId("1L");
        testUser.setEmail("testUser");

        testActivity = new Activity();

        activityOutputV2 = new ActivityOutputV2(
                "1L",
                "Test Activity",
                "Test Description",
                "test-image.png",
                new UserOutputDTOV2(
                        "testUser.getId()",
                        "testUser.getUsername()",
                        "testUser.getEmail()",
                        "testUser.getNationality()",
                        "testUser.getPhoneNumber()",
                        "testUser.getImage()",
                        LocalDateTime.now(),
                        0,
                        0,
                        false),
                "weekly",
                3,
                LocalDateTime.now());

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createActivitySuccessTest() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userService.findByEmailOnlyBase("testUser")).thenReturn(testUser);
        when(activityService.save(
                activityInputV2.name(),
                activityInputV2.description(),
                activityInputV2.image(),
                activityInputV2.timeRate(),
                activityInputV2.timesRequiered(),
                testUser.getId(),
                activityInputV2.categoryID()))
                .thenReturn(testActivity);
        when(activityOutputV2Mapper.toDTO(testActivity)).thenReturn(activityOutputV2);

        ActivityOutputV2 result = activityControllerV2.createActivity(activityInputV2);
        assertEquals(activityOutputV2, result);
    }

    @Test
    void createActivityUserNotFoundExceptionTest() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userService.findByEmailOnlyBase(anyString())).thenThrow(new UserNotFoundException("Test Exception"));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            activityControllerV2.createActivity(activityInputV2);
        });
        assertEquals("Test Exception", exception.getMessage());
    }

    @Test
    void paginationActivitiesTest() {
        int page = 0;
        int perPage = 10;
        List<Activity> activityList = Arrays.asList(testActivity);
        List<ActivityOutputV2> outputList = Arrays.asList(activityOutputV2);

        when(activityService.getPagination(page, perPage)).thenReturn(activityList);
        when(activityOutputV2Mapper.toDTO(activityList)).thenReturn(outputList);

        List<ActivityOutputV2> result = activityControllerV2.paginationActivities(page, perPage);
        assertEquals(outputList, result);
    }

    @Test
    void getSubscribedActivitiesTest() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        List<Activity> activityList = Arrays.asList(testActivity);
        List<ActivityOutputV2> outputList = Arrays.asList(activityOutputV2);

        when(userService.findByEmailOnlyBase("testUser")).thenReturn(testUser);
        when(activityService.getSubscribedActivities(testUser.getId())).thenReturn(activityList);
        when(activityOutputV2Mapper.toDTO(activityList)).thenReturn(outputList);

        List<ActivityOutputV2> result = activityControllerV2.getSubscribedActivities();
        assertEquals(outputList, result);
    }

    @Test
    void getSubscribedActivitiesWithStreakTest() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        List<Activity> activityList = Arrays.asList(testActivity);
        List<ActivityOutputV2Streak> outputList = Arrays
                .asList(new ActivityOutputV2Streak(null, null, null, null, null, null, null, null,null,null));

        when(userService.findByEmailOnlyBase("testUser")).thenReturn(testUser);
        when(activityService.getSubscribedActivitiesWithStreak(testUser.getId())).thenReturn(activityList);
        when(activityOutputV2StreakMapper.toDTO(activityList)).thenReturn(outputList);

        List<ActivityOutputV2Streak> result = activityControllerV2.getSubscribedActivitiesWithStreaks();
        assertEquals(outputList, result);
    }

    @Test
    void paginationActivitiesNotSubscribedTest() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        int page = 0;
        int perPage = 10;
        List<Activity> activityList = Arrays.asList(testActivity);
        List<ActivityOutputV2> outputList = Arrays.asList(activityOutputV2);

        when(userService.findByEmailOnlyBase("testUser")).thenReturn(testUser);
        when(activityService.getPaginationNotSubscribed(page, perPage, testUser.getId(),"")).thenReturn(activityList);
        when(activityOutputV2Mapper.toDTO(activityList)).thenReturn(outputList);

        List<ActivityOutputV2> result = activityControllerV2.paginationActivitiesNotSubscribed(page, perPage,"");
        assertEquals(outputList, result);
    }
    @Test
    void getSubscribedActivitiesWithStreakByNameTest() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String activityName = "Test Activity";
        List<Activity> activityList = Arrays.asList(testActivity);
        List<ActivityOutputV2Streak> outputList = Arrays
                .asList(new ActivityOutputV2Streak(null, null, null, null, null, null, null, null,null,null));

        when(userService.findByEmailOnlyBase("testUser")).thenReturn(testUser);
        when(activityService.getSubscribedActivitiesWithStreak(testUser.getId(), activityName)).thenReturn(activityList);
        when(activityOutputV2StreakMapper.toDTO(activityList)).thenReturn(outputList);

        List<ActivityOutputV2Streak> result = activityControllerV2.getSubscribedActivitiesWithStreak(activityName);
        assertEquals(outputList, result);
    }
}
