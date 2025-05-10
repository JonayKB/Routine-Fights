package es.iespuertodelacruz.routinefights.badge.domain;

import java.util.List;

import es.iespuertodelacruz.routinefights.badge.commons.BadgeCommons;
import es.iespuertodelacruz.routinefights.communityEvent.domain.CommunityEvent;
import es.iespuertodelacruz.routinefights.user.domain.User;

public class Badge extends BadgeCommons {
    private String id;
    private List<User> users;
    private CommunityEvent communityEvent;

    public Badge() {
    }

    public Badge(String id, String image, Integer level, List<User> users, CommunityEvent communityEvent) {
        super(image, level);
        this.id = id;
        this.users = users;
        this.communityEvent = communityEvent;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<User> getUsers() {
        return this.users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public CommunityEvent getCommunityEvent() {
        return this.communityEvent;
    }

    public void setCommunityEvent(CommunityEvent communityEvent) {
        this.communityEvent = communityEvent;
    }

    @Override
    public String toString() {
        return "Badge{" +
                "id='" + id + '\'' +
                ", image='" + getImage() + '\'' +
                ", level='" + getLevel() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Badge badge))
            return false;
        if (!super.equals(o))
            return false;
        return id.equals(badge.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
