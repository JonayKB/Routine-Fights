package es.iespuertodelacruz.routinefights.user.domain.ports.primary;

import java.util.List;

import es.iespuertodelacruz.routinefights.user.domain.User;

/**
 * IUserService
 */
public interface IUserService {
    /**
     * Method to find all users
     * 
     * @return List<User> list of users
     */
    public List<User> findAll();

    /**
     * Method to find a user by id
     * 
     * @param id
     * @return User
     */
    public User findById(String id);

    /**
     * Method to save a user
     * 
     * @param username username
     * @param email email
     * @param password password
     * @param nationality nationality
     * @param phoneNumber phoneNumber
     * @param image image
     * @return User
     */
    public User post(String username, String email, String password, String nationality, String phoneNumber, String image);

    /**
     * Method to update a user
     * 
     * @param id id
     * @param username username
     * @param email email
     * @param password password
     * @param nationality nationality
     * @param phoneNumber phoneNumber
     * @param image image
     * @return User
     */
    public User put(String id, String username, String email, String password, String nationality, String phoneNumber, String image);

    /**
     * Method to delete a user
     * 
     * @param id id
     * @return boolean
     */
    public boolean delete(String id);
}
