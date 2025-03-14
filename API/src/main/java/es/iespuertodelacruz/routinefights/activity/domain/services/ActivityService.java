package es.iespuertodelacruz.routinefights.activity.domain.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.activity.commons.TimeRateEnum;
import es.iespuertodelacruz.routinefights.activity.domain.Activity;
import es.iespuertodelacruz.routinefights.activity.domain.ports.primary.IActivityService;
import es.iespuertodelacruz.routinefights.activity.domain.ports.secondary.IActivityRepository;
import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.domain.ports.secondary.IUserRepository;

@Service
public class ActivityService implements IActivityService {

    private IActivityRepository activityRepository;
    private IUserRepository userRepository;
    // private ICategoryRepository categoryRepository;

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
        if (timeRate.equals(TimeRateEnum.DAILY.getValue()) || timeRate.equals(TimeRateEnum.WEEKLY.getValue())
                || timeRate.equals(TimeRateEnum.MONTHLY.getValue())
                || timeRate.equals(TimeRateEnum.YEARLY.getValue())) {
            activity.setTimeRate(timeRate);
        } else {
            throw new IllegalArgumentException("Time rate not valid, valid options: daily, weekly, monthly, yearly");
        }
        activity.setTimesRequiered(timesRequiered);
        // TODO add category
        // activity.setCategory(categoryRepository.findById(categoryID));
        User user = userRepository.findById(userID);
        activity.setUser(user);
        activity.setCreatedAt(LocalDateTime.now());

        return activityRepository.save(activity);
    }

}
