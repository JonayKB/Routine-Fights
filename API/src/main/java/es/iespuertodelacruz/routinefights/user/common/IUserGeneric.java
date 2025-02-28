package es.iespuertodelacruz.routinefights.user.common;

import java.util.List;

import es.iespuertodelacruz.routinefights.user.domain.User;

/**
 * IUserGeneric
 */
public interface IUserGeneric {
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
     * Method to find a user by email
     * 
     * @param email email
     * @return User
     */
    public User findByEmail(String email);

    /**
     * Method to check if a user exists by email
     * 
     * @param email email
     * @return Boolean
     */
    public Boolean existsByEmail(String email);

    /**
     * Method to delete a user
     * 
     * @param id id
     * @return boolean
     */
    public boolean delete(String id);
}
