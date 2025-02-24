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
}
