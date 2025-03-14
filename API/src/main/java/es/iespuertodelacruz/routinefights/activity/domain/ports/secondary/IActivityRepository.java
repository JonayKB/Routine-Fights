package es.iespuertodelacruz.routinefights.activity.domain.ports.secondary;

import es.iespuertodelacruz.routinefights.activity.domain.Activity;

public interface IActivityRepository {
    boolean userIsOnActivity(String userID, String activityID);

    Activity findById(String id);

    Activity save(Activity activity);
}
