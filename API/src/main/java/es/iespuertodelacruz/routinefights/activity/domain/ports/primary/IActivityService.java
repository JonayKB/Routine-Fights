package es.iespuertodelacruz.routinefights.activity.domain.ports.primary;

import java.util.List;


import es.iespuertodelacruz.routinefights.activity.domain.Activity;

public interface IActivityService {
    Activity save(String name, String description, String image, String timeRate, Integer timesRequiered, String userID,
            String categoryID);

    List<Activity> getPagination(int page, int perPage);
    List<Activity> getPaginationNotSubscribed(int page, int perPage, String userID, String activityName);

    List<Activity> getSubscribedActivities(String userID);

    List<Activity> getSubscribedActivitiesWithStreak(String userID);
    List<Activity> getSubscribedActivitiesWithStreak(String userID, String activityName);


}
