package es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import es.iespuertodelacruz.routinefights.activity.domain.Activity;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.entities.ActivityEntity;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.mappers.ActivityEntityMapper;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.repositories.IActivityEntityRepository;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.services.ActivityEntityService;
import es.iespuertodelacruz.routinefights.activity.infrastructure.exceptions.ActivityNotFoundExcetion;
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

        Activity activity= activityEntityService.findById(activityId);

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
        ActivityEntity activityEntity = new ActivityEntity();
        when(activityEntityMapper.toEntity(activity)).thenReturn(activityEntity);
        when(activityEntityRepository.save(activityEntity)).thenReturn(activityEntity);
        when(activityEntityMapper.toDomain(activityEntity)).thenReturn(activity);

        Activity result = activityEntityService.save(activity);

        assertNotNull(result);
    }

    @Test
    void getPaginationTestOK() {
        int page = 1;
        int perPage = 10;
        when(activityEntityRepository.getPagination(0, perPage)).thenReturn(new ArrayList<>());

        List<Activity> list =activityEntityService.getPagination(page, perPage);

        assertNotNull(list);

    }

    @Test
    void getSubscribedActivities() {
        String userId = "1L";
        when(activityEntityRepository.getSubscribedActivities(userId)).thenReturn(new ArrayList<>());

        List<Activity> list = activityEntityService.getSubscribedActivities(userId);

        assertNotNull(list);
    }
    
}
