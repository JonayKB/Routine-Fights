package es.iespuertodelacruz.routinefights.post.domain;

import java.time.LocalDateTime;

import es.iespuertodelacruz.routinefights.activity.domain.Activity;
import es.iespuertodelacruz.routinefights.post.commons.PostCommons;
import es.iespuertodelacruz.routinefights.user.domain.User;

import java.util.Objects;

public class Post extends PostCommons {
    private String id;
    private User user;
    private Activity activity;

    public Post(String id, String image, Integer streak, Integer pointsToAdd, LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime deletedAt, LocalDateTime filedAt, User user, Activity activity) {
        super(image, streak, pointsToAdd, createdAt, updatedAt, deletedAt, filedAt);
        this.id = id;
        this.user = user;
        this.activity = activity;
    }

    public Post() {
    }

    public Post(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Post id(String id) {
        setId(id);
        return this;
    }

    public Post(String id, User user) {
        this.id = id;
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post user(User user) {
        setUser(user);
        return this;
    }

    public Activity getActivity() {
        return this.activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Post)) {
            return false;
        }
        Post post = (Post) o;
        return Objects.equals(id, post.id);
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
