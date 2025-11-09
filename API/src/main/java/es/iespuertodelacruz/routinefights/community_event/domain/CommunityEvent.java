package es.iespuertodelacruz.routinefights.community_event.domain;

import java.time.LocalDateTime;
import java.util.List;

import es.iespuertodelacruz.routinefights.activity.domain.Activity;
import es.iespuertodelacruz.routinefights.community_event.commons.CommunityEventCommons;

public class CommunityEvent extends CommunityEventCommons {
    private String id;
    private List<Activity> activities;
    // private List<Badge> badges;
    // private List<Meeting> meetings;

    public CommunityEvent() {
        super();
    }

    public CommunityEvent(String id, String name, Integer totalRequired, LocalDateTime startDate,
            LocalDateTime finishDate,
            LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt,List<Activity> activities,String image) {
        super(name, totalRequired, startDate, finishDate, createdAt, updatedAt, deletedAt, image);
        this.id = id;
        this.activities = activities;
    }


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Activity> getActivities() {
        return this.activities;
    }
    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

}
