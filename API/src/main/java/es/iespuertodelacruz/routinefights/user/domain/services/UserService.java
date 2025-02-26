package es.iespuertodelacruz.routinefights.user.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.domain.ports.primary.IUserService;
import es.iespuertodelacruz.routinefights.user.domain.ports.secondary.IUserRepository;

/**
 * UserService
 */
@Service
public class UserService implements IUserService {
    private IUserRepository userRepository;

    /**
     * Getter for userRepository
     * 
     * @return IUserRepository
     */
    public IUserRepository getUserRepository() {
        return this.userRepository;
    }

    /**
     * Setter for userRepository
     * 
     * @param userRepository
     */
    @Autowired
    public void setUserRepository(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Method to find all users
     * 
     * @return List<User> list of users
     */
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Method to find a user by id
     * 
     * @param id id of the user
     * @return User
     */
    @Override
    public User findById(String id) {
        return userRepository.findById(id);
    }

    /**
     * Method to save a user
     * 
     * @param username username
     * @param email email
     * @param password password
     * @param nationality nationality
     * @param phoneNumber phoneNumber
     * @return User
     */
    @Override
    public User post(String username, String email, String password, String nationality, String phoneNumber) {
        User user = new User(username, email, password, nationality, phoneNumber);
        return userRepository.post(user);
    }

    /**
     * Method to update a user
     * 
     * @param id id
     * @param username username
     * @param email email
     * @param password password
     * @param nationality nationality
     * @param phoneNumber phoneNumber
     * @return User
     */
    @Override
    public User put(String id, String username, String email, String password, String nationality, String phoneNumber) {
        User user = new User(id, username, email, password, nationality, phoneNumber);
        return userRepository.put(user);
    }

    /**
     * Method to delete a user
     * 
     * @param id id
     * @return boolean
     */
    @Override
    public boolean delete(String id) {
        return userRepository.delete(id);
    }
}
