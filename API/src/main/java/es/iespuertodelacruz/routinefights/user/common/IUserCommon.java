package es.iespuertodelacruz.routinefights.user.common;

import java.util.List;

import es.iespuertodelacruz.routinefights.shared.services.ICRUD;


/**
 * IUserCommon
 */
public interface IUserCommon<T> extends ICRUD<T> {
    /**
     * Method to find a user by email
     * 
     * @param email email
     * @return User
     */
    public T findByEmail(String email);

    /**
     * Method to check if a user exists by email
     * 
     * @param email email
     * @return Boolean
     */
    public Boolean existsByEmail(String email);

    /**
     * Method to find the followed users by user's email
     * 
     * @return List<User>
     */
    public List<T> findFollowedUsersByEmail(String email);

    /**
     * Method to find the followers by user's email
     * 
     * @return List<User>
     */
    public List<T> findFollowersByEmail(String email);

    /**
     * Method to follow a user
     * 
     * @param frEmail follower's email
     * @param fdEmail followed's email
     * @return Boolean
     */
    public boolean followUser(String frEmail, String fdEmail);

    /**
     * Method to unfollow a user
     * 
     * @param frEmail follower's email
     * @param fdEmail followed's email
     * @return Boolean
     */
    public boolean unfollowUser(String frEmail, String fdEmail);

    /**
     * Method to find user's images
     * 
     * @return List<String>
     */
    public List<String> findAllImages();
}
