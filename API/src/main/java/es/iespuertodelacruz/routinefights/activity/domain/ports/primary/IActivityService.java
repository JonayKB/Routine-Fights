package es.iespuertodelacruz.routinefights.activity.domain.ports.primary;

import es.iespuertodelacruz.routinefights.activity.domain.Activity;

public interface IActivityService {
    Activity save(String name, String description, String image, String timeRate, Integer timesRequiered, String userID, String categoryID);
}
