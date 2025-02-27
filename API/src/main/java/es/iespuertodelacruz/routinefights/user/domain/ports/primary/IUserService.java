package es.iespuertodelacruz.routinefights.user.domain.ports.primary;

import java.util.List;

import org.neo4j.cypherdsl.core.Use;

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
     * Method to find a user by id
     * 
     * @param id
     * @return User
     */
    public User findById(String id);

    /**
     * Method to save a user
     * 
     * @param username    username
     * @param email       email
     * @param password    password
     * @param nationality nationality
     * @param phoneNumber phoneNumber
     * @param image       image
     * @param role        role
     * @param verified    verified
     * @return User
     */
    public User post(String username, String email, String password, String nationality, String phoneNumber,
            String image, String role, boolean verified, String verificationToken);

    /**
     * Method to update a user
     * 
     * @param id          id
     * @param username    username
     * @param email       email
     * @param password    password
     * @param nationality nationality
     * @param phoneNumber phoneNumber
     * @param image       image
     * @param role        role
     * @param verified    verified
     * @return User
     */
    public User put(String id, String username, String email, String password, String nationality, String phoneNumber,
            String image, String role, boolean verified, String verificationToken);

    /**
     * Method to delete a user
     * 
     * @param id id
     * @return boolean
     */
    public boolean delete(String id);
}
