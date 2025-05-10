package es.iespuertodelacruz.routinefights.badge.infrastructure.adapters.secondary.entities;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import es.iespuertodelacruz.routinefights.badge.commons.BadgeCommons;
import es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.secondary.entities.CommunityEventEntity;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;

import java.util.List;
import java.util.Objects;

@Node("Badge")
/**
 * BadgeEntity
 */
public class BadgeEntity extends BadgeCommons {
    @Id
    @GeneratedValue
    private String id;
    @Relationship(type = "Has_Badge", direction = Relationship.Direction.INCOMING)
    private List<UserEntity> users;

    @Relationship(type = "Associated_With", direction = Relationship.Direction.OUTGOING)
    private CommunityEventEntity communityEvent;

    public BadgeEntity() {
    }

    public BadgeEntity(String id, String image, Integer level, List<UserEntity> users,
            CommunityEventEntity communityEvent) {
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

    public List<UserEntity> getUser() {
        return this.users;
    }

    public void setUser(List<UserEntity> users) {
        this.users = users;
    }

    public CommunityEventEntity getCommunityEvent() {
        return this.communityEvent;
    }

    public void setCommunityEvent(CommunityEventEntity communityEvent) {
        this.communityEvent = communityEvent;
    }

    public BadgeEntity id(String id) {
        setId(id);
        return this;
    }


    public BadgeEntity user(List<UserEntity> user) {
        setUser(user);
        return this;
    }

    public BadgeEntity communityEvent(CommunityEventEntity communityEvent) {
        setCommunityEvent(communityEvent);
        return this;
    }
    public BadgeEntity image(String image) {
        setImage(image);
        return this;
    }
    public BadgeEntity level(Integer level) {
        setLevel(level);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof BadgeEntity)) {
            return false;
        }
        BadgeEntity badgeEntity = (BadgeEntity) o;
        return Objects.equals(id, badgeEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", image='" + getImage() + "'" +
                ", level='" + getLevel() + "'" +
                ", user='" + getUser() + "'" +
                ", communityEvent='" + getCommunityEvent() + "'" +
                "}";
    }

}
