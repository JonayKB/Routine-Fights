package es.iespuertodelacruz.routinefights.user.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import es.iespuertodelacruz.routinefights.user.common.UserCommon;

/**
 * User
 */
public class User extends UserCommon {
    private String id;
    private List<User> followers;
    private List<User> following;

    /**
     * Default constructor
     */
    public User() {
    }

    /**
     * Constructor with values
     * 
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
     */
    public User(String username, String email, String password, String nationality, String phoneNumber, String image,
            String role, boolean verified, String verificationToken, LocalDateTime createdAt, LocalDateTime updatedAt,
            LocalDateTime deletedAt) {
        super(username, email, password, nationality, phoneNumber, image, role, verified, verificationToken, createdAt,
                updatedAt, deletedAt);
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
     * @param followers         The number of followers of the user
     * @param following         The number of following of the user
     */
    public User(String id, String username, String email, String password, String nationality, String phoneNumber,
            String image, String role, boolean verified, String verificationToken, LocalDateTime createdAt,
            LocalDateTime updatedAt, LocalDateTime deletedAt, List<User> followers, List<User> following) {
        super(username, email, password, nationality, phoneNumber, image, role, verified, verificationToken, createdAt,
                updatedAt, deletedAt);
        this.id = id;
        this.followers = followers;
        this.following = following;
    }

    public User(String id, String username, String email, String password, String nationality,
            String phoneNumber, String image) {
        super(username, email, password, nationality, phoneNumber, image);
        this.id = id;
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
     * Get the number of followers of the user
     * 
     * @return The number of followers of the user
     */
    public List<User> getFollowers() {
        return this.followers;
    }

    /**
     * Set the number of followers of the user
     * 
     * @param followers The number of followers of the user
     */
    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    /**
     * Get the number of following of the user
     * 
     * @return The number of following of the user
     */
    public List<User> getFollowing() {
        return this.following;
    }

    /**
     * Set the number of following of the user
     * 
     * @param following The number of following of the user
     */
    public void setFollowing(List<User> following) {
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
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id);
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
                ", followers='" + getFollowers().size() + "'" +
                ", following='" + getFollowing().size() + "'" +
                "}";
    }
}
