package es.iespuertodelacruz.routinefights.activity.domain.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.*;
import org.mockito.*;

import es.iespuertodelacruz.routinefights.activity.domain.Activity;
import es.iespuertodelacruz.routinefights.activity.domain.ports.secondary.IActivityRepository;
import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.domain.ports.secondary.IUserRepository;

class ActivityServiceTest {

    private static final String VALID_TIME_RATE_DAILY = "daily";
    private static final String INVALID_TIME_RATE = "hourly";
    private static final String INVALID_TIME_RATE_EXCEPTION = "Time rate not valid, valid options: daily, weekly, monthly, yearly";

    @Mock
    private IActivityRepository activityRepository;

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private ActivityService activityService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testSaveWithValidTimeRate() {
        String name = "Morning Run";
        String description = "Daily running activity";
        String image = "run.png";
        String timeRate = VALID_TIME_RATE_DAILY;
        Integer timesRequired = 1;
        String userId = "user123";
        String categoryId = "cat456";

        User dummyUser = new User();
        when(userRepository.findById(userId)).thenReturn(dummyUser);

        Activity expectedActivity = new Activity();
        expectedActivity.setName(name);
        expectedActivity.setDescription(description);
        expectedActivity.setImage(image);
        expectedActivity.setTimeRate(timeRate);
        expectedActivity.setTimesRequiered(timesRequired);
        expectedActivity.setCreator(dummyUser);
        expectedActivity.setCreatedAt(LocalDateTime.now());
        when(activityRepository.save(any(Activity.class))).thenReturn(expectedActivity);

        Activity result = activityService.save(name, description, image, timeRate, timesRequired, userId, categoryId);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(description, result.getDescription());
        assertEquals(image, result.getImage());
        assertEquals(timeRate, result.getTimeRate());
        assertEquals(timesRequired, result.getTimesRequiered());
        verify(userRepository, times(1)).findById(userId);
        verify(activityRepository, times(1)).save(any(Activity.class));
    }

    @Test
    void testSaveWithInvalidTimeRate() {
        String name = "Evening Yoga";
        String description = "Evening session";
        String image = "yoga.png";
        String timeRate = INVALID_TIME_RATE;
        Integer timesRequired = 1;
        String userId = "user123";
        String categoryId = "cat456";

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> activityService.save(name, description, image, timeRate, timesRequired, userId, categoryId));
        assertEquals(INVALID_TIME_RATE_EXCEPTION, thrown.getMessage());
        verify(userRepository, never()).findById(anyString());
        verify(activityRepository, never()).save(any(Activity.class));
    }

    @Test
    void testGetPagination() {
        int page = 1;
        int perPage = 10;
        List<Activity> activities = new ArrayList<>();
        activities.add(new Activity());
        activities.add(new Activity());

        when(activityRepository.getPagination(page, perPage)).thenReturn(activities);

        List<Activity> result = activityService.getPagination(page, perPage);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(activityRepository, times(1)).getPagination(page, perPage);
    }

    @Test
    void testGetSubscribedActivities(){
        String userId = "user123";
        List<Activity> activities = new ArrayList<>();
        activities.add(new Activity());
        activities.add(new Activity());

        when(activityRepository.getSubscribedActivities(userId)).thenReturn(activities);

        List<Activity> result = activityService.getSubscribedActivities(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(activityRepository, times(1)).getSubscribedActivities(userId);
    }
}
