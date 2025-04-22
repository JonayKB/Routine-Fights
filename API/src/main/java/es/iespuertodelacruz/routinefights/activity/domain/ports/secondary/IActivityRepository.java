package es.iespuertodelacruz.routinefights.activity.domain.ports.secondary;

import java.util.List;


import es.iespuertodelacruz.routinefights.activity.domain.Activity;

public interface IActivityRepository {
    boolean userIsOnActivity(String userID, String activityID);

    Activity findById(String id);

    Activity save(Activity activity);

    List<Activity> getPagination(int page, int perPage);

    List<Activity> getSubscribedActivities(String userID);

    public List<Activity> getSubscribedActivitiesWithStreak(String userID);

    List<Activity> getPaginationNotSubscribed(int page, int perPage, String userID);
    List<Activity> getSubscribedActivities(String userID, String activityName);
}
