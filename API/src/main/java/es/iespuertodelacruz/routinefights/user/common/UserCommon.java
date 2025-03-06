package es.iespuertodelacruz.routinefights.user.common;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * UserCommon
 */
public abstract class UserCommon {
    private String username;
    private String email;
    private String password;
    private String nationality;
    private String phoneNumber;
    private String image;
    private String role;
    private boolean verified;
    private String verificationToken;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    /**
     * Default constructor
     */
    protected UserCommon() {
    }

    /**
     * Constructor with all the attributes
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
    protected UserCommon(String username, String email, String password, String nationality, String phoneNumber,
            String image, String role, boolean verified, String verificationToken, LocalDateTime createdAt,
            LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.nationality = nationality;
        this.phoneNumber = phoneNumber;
        this.image = image;
        this.role = role;
        this.verified = verified;
        this.verificationToken = verificationToken;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    /**
     * Constructor for V2
     * 
     * @param username    The username of the user
     * @param email       The email of the user
     * @param password    The password of the user
     * @param nationality The nationality of the user
     * @param phoneNumber The phone number of the user
     * @param image       The image of the user
     */
    protected UserCommon(String username, String email, String password, String nationality, String phoneNumber,
            String image) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.nationality = nationality;
        this.phoneNumber = phoneNumber;
        this.image = image;
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
     * Get the image of the user
     * 
     * @return The image of the user
     */
    public String getImage() {
        return this.image;
    }

    /**
     * Set the image of the user
     * 
     * @param image The image of the user
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Get the role of the user
     * 
     * @return The role of the user
     */
    public String getRole() {
        return this.role;

    }

    /**
     * Set the role of the user
     * 
     * @param role The role of the user
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Get the verification status of the user
     * 
     * @return The verification status of the user
     */
    public boolean getVerified() {
        return this.verified;
    }

    /**
     * Set the verification status of the user
     * 
     * @param verified The verification status of the user
     */
    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    /**
     * Get the verification token of the user
     * 
     * @return The verification token of the user
     */
    public String getVerificationToken() {
        return this.verificationToken;
    }

    /**
     * Set the verification token of the user
     * 
     * @param verificationToken The verification token of the user
     */
    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
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
     * Equals method to compare two UserEntity objects
     * 
     * @param o The object to compare
     * @return True if the objects are equals, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof UserCommon)) {
            return false;
        }
        UserCommon user = (UserCommon) o;
        return Objects.equals(email, user.email);
    }

    /**
     * Hash code method
     * 
     * @return The hash code of the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    /**
     * To string method
     * 
     * @return The string representation of the object
     */
    @Override
    public String toString() {
        return "{" +
                "username='" + getUsername() + "'" +
                ", email='" + getEmail() + "'" +
                ", verified='" + getVerified() + "'" +
                "}";
    }
}