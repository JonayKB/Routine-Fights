package es.iespuertodelacruz.routinefights.activity.domain;

import java.time.LocalDateTime;

import es.iespuertodelacruz.routinefights.activity.commons.ActivityCommons;
import es.iespuertodelacruz.routinefights.user.domain.User;

import java.util.Objects;

public class Activity extends ActivityCommons {
    private String id;
    private User user;

    public Activity(String id, String name, String description, String image, String timeRate, Integer timesRequiered,
            LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt, User user) {
        super(name, description, image, timeRate, timesRequiered, createdAt, updatedAt, deletedAt);
        this.user = user;
        this.id = id;
    }

    public Activity() {
    }

    public Activity(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Activity id(String id) {
        setId(id);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Activity)) {
            return false;
        }
        Activity activity = (Activity) o;
        return Objects.equals(id, activity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                "}";
    }

}
