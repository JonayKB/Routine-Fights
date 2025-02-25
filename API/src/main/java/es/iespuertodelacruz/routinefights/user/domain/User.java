package es.iespuertodelacruz.routinefights.user.domain;

import java.util.Objects;

/**
 * User
 */
public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String nationality;
    private String phoneNumber;

    /**
     * Default constructor
     */
    public User() {
    }

    /**
     * Constructor with username, email, password, nationality and phone number
     * 
     * @param username    The username of the user
     * @param email       The email of the user
     * @param password    The password of the user
     * @param nationality The nationality of the user
     * @param phoneNumber The phone number of the user
     */
    public User(String username, String email, String password, String nationality, String phoneNumber) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.nationality = nationality;
        this.phoneNumber = phoneNumber;
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
     */
    public User(Long id, String username, String email, String password, String nationality, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.nationality = nationality;
        this.phoneNumber = phoneNumber;
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
                "}";
    }
}
