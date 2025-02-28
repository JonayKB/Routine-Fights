package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import es.iespuertodelacruz.routinefights.user.common.UserCommon;

import java.util.Objects;

/**
 * UserEntity
 */
@Node("User")
public class UserEntity extends UserCommon {
    @Id
    @GeneratedValue
    private String id;

    @Relationship(type = "FOLLOWS", direction = Relationship.Direction.INCOMING)
    private List<UserEntity> followers;

    @Relationship(type = "FOLLOWS", direction = Relationship.Direction.OUTGOING)
    private List<UserEntity> following;

    /**
     * Default constructor
     */
    public UserEntity() {
    }

    /**
     * Constructor with all the attributes
     * 
     * @param id                The id of the user
     * @param username          The username of the user
     * @param email             The email of the user
     * @param password          The password of the user
     * @param nationality       The nationality of the user
     * @param phoneNumber       The phone number of the user
     * @param image             The image of the user
     * @param role              The role of the user
     * @param verified          The verification status of the user
     * @param verificationToken The verification token of the user
     * @param createdAt         The date when the user was created
     * @param updatedAt         The date when the user was updated
     * @param deletedAt         The date when the user was deleted
     * @param followers         The list of followers of the user
     * @param following         The list of users that the user is following
     */
    public UserEntity(String id, String username, String email, String password, String nationality, String phoneNumber,
            String image, String role, boolean verified, String verificationToken, LocalDateTime createdAt,
            LocalDateTime updatedAt, LocalDateTime deletedAt, List<UserEntity> followers,
            List<UserEntity> following) {
        super(username, email, password, nationality, phoneNumber, image, role, verified, verificationToken, createdAt,
                updatedAt, deletedAt);
        this.id = id;
        this.followers = followers;
        this.following = following;
    }

    /**
     * Get the id of the user
     * 
     * @return The id of the user
     */
    public String getId() {
        return this.id;
    }

    /**
     * Set the id of the user
     * 
     * @param id The id of the user
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the list of followers of the user
     * 
     * @return The list of followers of the user
     */
    public List<UserEntity> getFollowers() {
        return this.followers;
    }

    /**
     * Set the list of followers of the user
     * 
     * @param followers The list of followers of the user
     */
    public void setFollowers(List<UserEntity> followers) {
        this.followers = followers;
    }

    /**
     * Get the list of users that the user is following
     * 
     * @return The list of users that the user is following
     */
    public List<UserEntity> getFollowing() {
        return this.following;
    }

    /**
     * Set the list of users that the user is following
     * 
     * @param following The list of users that the user is following
     */
    public void setFollowing(List<UserEntity> following) {
        this.following = following;
    }

    /**
     * Equals method to compare two UserEntity objects
     * 
     * @param o The object to compare
     * @return True if the objects are equals, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof UserEntity)) {
            return false;
        }
        UserEntity userEntity = (UserEntity) o;
        return Objects.equals(id, userEntity.id);
    }

    /**
     * Hash code method
     * 
     * @return The hash code of the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * To string method
     * 
     * @return The string representation of the object
     */
    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", username='" + getUsername() + "'" +
                ", email='" + getEmail() + "'" +
                ", verified='" + getVerified() + "'" +
                "}";
    }
}
