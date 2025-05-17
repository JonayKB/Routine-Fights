package es.iespuertodelacruz.routinefights.activity.domain;

import java.time.LocalDateTime;

import es.iespuertodelacruz.routinefights.activity.commons.ActivityCommons;
import es.iespuertodelacruz.routinefights.user.domain.User;

import java.util.List;
import java.util.Objects;

public class Activity extends ActivityCommons {
    private String id;
    private User creator;
    private List<User> participants;


    public Activity(String id, String name, String description, String image, String timeRate, Integer timesRequiered,
            LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt, User user, List<User> participants) {
        super(name, description, image, timeRate, timesRequiered, createdAt, updatedAt, deletedAt);
        this.creator = user;
        this.id = id;
        this.participants = participants;
    }

    public Activity() {
    }

    public Activity(String id) {
        this.id = id;
    }

    public Activity(String id, User creator, List<User> participants) {
        this.id = id;
        this.creator = creator;
        this.participants = participants;
    }

    public List<User> getParticipants() {
        return this.participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public Activity creator(User creator) {
        setCreator(creator);
        return this;
    }

    public Activity participants(List<User> participants) {
        setParticipants(participants);
        return this;
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

    public User getCreator() {
        return this.creator;
    }

    public void setCreator(User user) {
        this.creator = user;
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
