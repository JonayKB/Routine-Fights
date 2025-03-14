package es.iespuertodelacruz.routinefights.activity.domain.ports.secondary;

import java.util.List;

import org.springframework.data.domain.Page;

import es.iespuertodelacruz.routinefights.activity.domain.Activity;

public interface IActivityRepository {
    boolean userIsOnActivity(String userID, String activityID);

    Activity findById(String id);

    Activity save(Activity activity);

    List<Activity> getPagination(int page, int perPage);
}
