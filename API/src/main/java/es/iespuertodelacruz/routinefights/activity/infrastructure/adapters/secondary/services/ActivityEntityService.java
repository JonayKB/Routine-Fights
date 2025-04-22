package es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.services;

import java.util.List;
import java.util.Optional;


import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.activity.domain.Activity;
import es.iespuertodelacruz.routinefights.activity.domain.ports.secondary.IActivityRepository;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.entities.ActivityEntity;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.mappers.ActivityEntityMapper;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.repositories.IActivityEntityRepository;
import es.iespuertodelacruz.routinefights.activity.infrastructure.exceptions.ActivityNotFoundExcetion;

@Service
public class ActivityEntityService implements IActivityRepository {
    private IActivityEntityRepository activityEntityRepository;
    private ActivityEntityMapper activityEntityMapper;

    public ActivityEntityService(IActivityEntityRepository activityEntityRepository,
            ActivityEntityMapper activityEntityMapper) {
        this.activityEntityRepository = activityEntityRepository;
        this.activityEntityMapper = activityEntityMapper;
    }

    @Override
    public boolean userIsOnActivity(String userID, String activityID) {
        return activityEntityRepository.userIsOnActivity(userID, activityID);
    }

    @Override
    public Activity findById(String id) {
        Optional<ActivityEntity> activityEntity = activityEntityRepository.findById(id);
        if (activityEntity.isPresent()) {
            return activityEntityMapper.toDomain(activityEntity.get());
        }
        throw new ActivityNotFoundExcetion("Activity not found");
    }

    @Override
    public Activity save(Activity activity) {
        ActivityEntity activityEntity = activityEntityMapper.toEntity(activity);
        return activityEntityMapper.toDomain(activityEntityRepository.save(activityEntity));
    }

    @Override
    public List<Activity> getPagination(int page, int perPage) {
        int offset = (page-1) * perPage;

        return activityEntityMapper.toDomain(activityEntityRepository.getPagination(offset, perPage));
    }

    @Override
    public List<Activity> getSubscribedActivities(String userID) {
        return activityEntityMapper.toDomain(activityEntityRepository.getSubscribedActivities(userID));
    }
    @Override
    public List<Activity> getSubscribedActivitiesWithStreak(String userID) {
        return activityEntityMapper.toDomain(activityEntityRepository.getSubscribedActivitiesWithStreak(userID));
    }


    @Override
    public List<Activity> getPaginationNotSubscribed(int page, int perPage, String userID, String activityName) {
        int offset = (page-1) * perPage;

        return activityEntityMapper.toDomain(activityEntityRepository.getPaginationNotSubscribed(offset, perPage, userID, activityName));
    }

    @Override
    public List<Activity> getSubscribedActivities(String userID, String activityName) {
        return activityEntityMapper.toDomain(activityEntityRepository.getSubscribedActivitiesWithStreak(userID, activityName));
    }

    

}
