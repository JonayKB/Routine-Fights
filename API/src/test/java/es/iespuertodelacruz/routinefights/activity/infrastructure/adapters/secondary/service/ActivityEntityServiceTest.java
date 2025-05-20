package es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import es.iespuertodelacruz.routinefights.activity.domain.Activity;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.entities.ActivityEntity;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.mappers.ActivityEntityMapper;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.repositories.IActivityEntityRepository;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.services.ActivityEntityService;
import es.iespuertodelacruz.routinefights.activity.infrastructure.exceptions.ActivityNotFoundExcetion;
import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;

@SpringBootTest
class ActivityEntityServiceTest {
    @Mock
    private IActivityEntityRepository activityEntityRepository;

    @Mock
    private ActivityEntityMapper activityEntityMapper;

    @InjectMocks
    private ActivityEntityService activityEntityService;

    @Test
    void userIsOnActivityTestOK() {
        String userId = "1L";
        String activityId = "2L";
        when(activityEntityRepository.userIsOnActivity(userId, activityId)).thenReturn(true);

        boolean result = activityEntityService.userIsOnActivity(userId, activityId);

        assertTrue(result);
    }

    @Test
    void findByIdTestOK() {
        String activityId = "1L";
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setId(activityId);
        when(activityEntityRepository.findById(activityId)).thenReturn(Optional.of(activityEntity));
        when(activityEntityMapper.toDomain(activityEntity)).thenReturn(new Activity());

        Activity activity = activityEntityService.findById(activityId);

        assertNotNull(activity);
    }

    @Test
    void findByIdTestException() {
        String activityId = "1L";
        when(activityEntityRepository.findById(activityId)).thenReturn(Optional.empty());

        assertThrows(ActivityNotFoundExcetion.class, () -> activityEntityService.findById(activityId));
    }

    @Test
    void saveTestOK() {
        Activity activity = new Activity();
        activity.setCreator(new User());
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setCreator(new UserEntity());
        when(activityEntityMapper.toEntity(activity)).thenReturn(activityEntity);
        when(activityEntityRepository.create(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(activityEntity);
        when(activityEntityMapper.toDomain(activityEntity)).thenReturn(activity);

        Activity result = activityEntityService.save(activity);

        assertNotNull(result);
    }

    @Test
    void getPaginationTestOK() {
        int page = 1;
        int perPage = 10;
        when(activityEntityRepository.getPagination(0, perPage)).thenReturn(new ArrayList<>());

        List<Activity> list = activityEntityService.getPagination(page, perPage);

        assertNotNull(list);

    }

    @Test
    void getSubscribedActivities() {
        String userId = "1L";
        ActivityEntity activityEntity = new ActivityEntity();

        when(activityEntityRepository.getSubscribedActivities(userId)).thenReturn(List.of(activityEntity));
        when(activityEntityRepository.getTimesRemaining(any(), any(), any(), any())).thenReturn(1);

        List<Activity> list = activityEntityService.getSubscribedActivities(userId);

        assertNotNull(list);
    }

    @Test
    void getSubscribedActivitiesWithStreak() {
        String userId = "1L";
        ActivityEntity activityEntity = new ActivityEntity();
        when(activityEntityRepository.getSubscribedActivitiesWithStreak(userId)).thenReturn(List.of(activityEntity));
        when(activityEntityMapper.toDomain(activityEntity)).thenReturn(new Activity());

        List<Activity> list = activityEntityService.getSubscribedActivitiesWithStreak(userId);

        assertNotNull(list);
    }

    @Test
    void getPaginationNotSubscribed() {
        int page = 1;
        int perPage = 10;
        String userId = "1L";
        when(activityEntityRepository.getPaginationNotSubscribed(0, perPage, userId, "")).thenReturn(new ArrayList<>());

        List<Activity> list = activityEntityService.getPaginationNotSubscribed(page, perPage, userId, "");

        assertNotNull(list);
    }

    @Test
    void getSubscribedActivitiesWithStreakByName() {
        String userId = "1L";
        String activityName = "activityName";
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setTimeRate("daily");

        when(activityEntityRepository.getSubscribedActivitiesWithStreak(userId, activityName))
                .thenReturn(List.of(activityEntity));
        when(activityEntityRepository.getTimesRemaining(any(), any(), any(), any())).thenReturn(1);
        when(activityEntityMapper.toDomain(activityEntity)).thenReturn(new Activity());

        List<Activity> list = activityEntityService.getSubscribedActivities(userId, activityName);

        assertNotNull(list);
    }

    @Test
    void findAllActivities() {
        ActivityEntity activityEntity = new ActivityEntity();
        List<ActivityEntity> activityEntities = new ArrayList<>();
        activityEntities.add(activityEntity);
        when(activityEntityRepository.findAll()).thenReturn(activityEntities);
        when(activityEntityMapper.toDomain(activityEntity)).thenReturn(new Activity());

        List<Activity> list = activityEntityService.findAll();

        assertNotNull(list);
    }

}
