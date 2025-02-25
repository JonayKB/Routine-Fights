package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Objects;

/**
 * UserEntity
 */
@Node("User")
public class UserEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String email;
    private String password;
    private String nationality;
    private String phoneNumber;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @Relationship(type = "FOLLOWED_BY", direction = Relationship.Direction.INCOMING)
    private List<UserEntity> followers;

    @Relationship(type = "FOLLOWED_BY", direction = Relationship.Direction.OUTGOING)
    private List<UserEntity> following;

    /**
     * Default constructor
     */
    public UserEntity() {
    }

    /**
     * Constructor with all the attributes
     * 
     * @param id          The id of the user
     * @param username    The username of the user
     * @param email       The email of the user
     * @param password    The password of the user
     * @param nationality The nationality of the user
     * @param phoneNumber The phone number of the user
     * @param createdAt   The date when the user was created
     * @param updatedAt   The date when the user was updated
     * @param deletedAt   The date when the user was deleted
     * @param followers   The list of followers of the user
     * @param following   The list of users that the user is following
     */
    public UserEntity(Long id, String username, String email, String password, String nationality, String phoneNumber,
            LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt, List<UserEntity> followers,
            List<UserEntity> following) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.nationality = nationality;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.followers = followers;
        this.following = following;
    }

    /**
     * Get the id of the user
     * 
     * @return The id of the user
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Set the id of the user
     * 
     * @param id The id of the user
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the username of the user
     * 
     * @return The username of the user
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Set the username of the user
     * 
     * @param username The username of the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the email of the user
     * 
     * @return The email of the user
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Set the email of the user
     * 
     * @param email The email of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the password of the user
     * 
     * @return The password of the user
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Set the password of the user
     * 
     * @param password The password of the user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the nationality of the user
     * 
     * @return The nationality of the user
     */
    public String getNationality() {
        return this.nationality;
    }

    /**
     * Set the nationality of the user
     * 
     * @param nationality The nationality of the user
     */
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    /**
     * Get the phone number of the user
     * 
     * @return The phone number of the user
     */
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    /**
     * Set the phone number of the user
     * 
     * @param phoneNumber The phone number of the user
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Get the date when the user was created
     * 
     * @return The date when the user was created
     */
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Set the date when the user was created
     * 
     * @param createdAt The date when the user was created
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Get the date when the user was updated
     * 
     * @return The date when the user was updated
     */
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * Set the date when the user was updated
     * 
     * @param updatedAt The date when the user was updated
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Get the date when the user was deleted
     * 
     * @return The date when the user was deleted
     */
    public LocalDateTime getDeletedAt() {
        return this.deletedAt;
    }

    /**
     * Set the date when the user was deleted
     * 
     * @param deletedAt The date when the user was deleted
     */
    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
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
                "}";
    }
}
