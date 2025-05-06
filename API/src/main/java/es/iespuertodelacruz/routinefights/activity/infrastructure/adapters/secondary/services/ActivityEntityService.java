package es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.activity.commons.TimeRates;
import es.iespuertodelacruz.routinefights.activity.domain.Activity;
import es.iespuertodelacruz.routinefights.activity.domain.ports.secondary.IActivityRepository;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.entities.ActivityEntity;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.mappers.ActivityEntityMapper;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.repositories.IActivityEntityRepository;
import es.iespuertodelacruz.routinefights.activity.infrastructure.exceptions.ActivityNotFoundExcetion;
import es.iespuertodelacruz.routinefights.post.exceptions.UploadPostException;

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
        int offset = (page - 1) * perPage;

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
        int offset = (page - 1) * perPage;

        return activityEntityMapper
                .toDomain(activityEntityRepository.getPaginationNotSubscribed(offset, perPage, userID, activityName));
    }

    @Override
    public List<Activity> getSubscribedActivities(String userID, String activityName) {
        List<ActivityEntity> subscribedActivities = activityEntityRepository.getSubscribedActivitiesWithStreak(userID,
                activityName);
        subscribedActivities.forEach(activity -> {
            LocalDateTime startOfPeriod;
            LocalDateTime endOfPeriod;
            switch (activity.getTimeRate()) {
                case TimeRates.DAILY:
                    startOfPeriod = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
                            .minusDays(1);
                    endOfPeriod = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999)
                            .minusDays(1);
                    break;
                case TimeRates.WEEKLY:
                    startOfPeriod = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
                            .minusWeeks(1).with(java.time.DayOfWeek.MONDAY);
                    endOfPeriod = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999)
                            .minusWeeks(1).with(java.time.DayOfWeek.SUNDAY);
                    break;
                case TimeRates.MONTHLY:
                    startOfPeriod = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
                            .minusMonths(1).withDayOfMonth(1);
                    endOfPeriod = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999)
                            .minusMonths(1)
                            .withDayOfMonth(LocalDateTime.now().minusMonths(1).toLocalDate().lengthOfMonth());
                    break;
                case TimeRates.YEARLY:
                    startOfPeriod = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
                            .minusYears(1).withDayOfYear(1);
                    endOfPeriod = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999)
                            .minusYears(1)
                            .withDayOfYear(LocalDateTime.now().minusYears(1).toLocalDate().lengthOfYear());
                    break;
                default:
                    throw new UploadPostException("Invalid time rate");
            }
            activity.setTimesRemaining(activityEntityRepository.getTimesRemaining(activity.getId(), startOfPeriod, endOfPeriod));
        });
        return activityEntityMapper.toDomain(subscribedActivities);
    }

}
