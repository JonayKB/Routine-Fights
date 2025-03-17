package es.iespuertodelacruz.routinefights.activity.domain.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.activity.commons.TimeRates;
import es.iespuertodelacruz.routinefights.activity.domain.Activity;
import es.iespuertodelacruz.routinefights.activity.domain.ports.primary.IActivityService;
import es.iespuertodelacruz.routinefights.activity.domain.ports.secondary.IActivityRepository;
import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.domain.ports.secondary.IUserRepository;

@Service
public class ActivityService implements IActivityService {

    private IActivityRepository activityRepository;
    private IUserRepository userRepository;

    public ActivityService(IActivityRepository activityRepository, IUserRepository userRepository) {
        this.userRepository = userRepository;
        this.activityRepository = activityRepository;
    }

    @Override
    public Activity save(String name, String description, String image, String timeRate, Integer timesRequiered,
            String userID, String categoryID) {
        Activity activity = new Activity();
        activity.setName(name);
        activity.setDescription(description);
        activity.setImage(image);
        if (timeRate.equals(TimeRates.DAILY) || timeRate.equals(TimeRates.WEEKLY) || timeRate.equals(TimeRates.MONTHLY)
                || timeRate.equals(TimeRates.YEARLY)) {
            activity.setTimeRate(timeRate);
        } else {
            throw new IllegalArgumentException("Time rate not valid, valid options: daily, weekly, monthly, yearly");
        }
        activity.setTimesRequiered(timesRequiered);

        User user = userRepository.findById(userID);
        activity.setCreator(user);
        activity.setCreatedAt(LocalDateTime.now());

        return activityRepository.save(activity);
    }

    @Override
    public List<Activity> getPagination(int page, int perPage) {
        return activityRepository.getPagination(page, perPage);
    }

    @Override
    public List<Activity> getSubscribedActivities(String userID) {
        return activityRepository.getSubscribedActivities(userID);
    }

}
