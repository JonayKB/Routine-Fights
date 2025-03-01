package es.iespuertodelacruz.routinefights.user.domain.services;

import java.time.LocalDateTime;
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

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User post(String username, String email, String password, String nationality, String phoneNumber,
            String image, String role, boolean verified, String verificationToken, LocalDateTime createdAt,
            LocalDateTime updatedAt, LocalDateTime deletedAt) {
        User user = new User(username, email, password, nationality, phoneNumber, image, role, verified,
                verificationToken, createdAt, updatedAt, deletedAt);
        return userRepository.post(user);
    }

    @Override
    public User put(String id, String username, String email, String password, String nationality, String phoneNumber,
            String image, String role, boolean verified, String verificationToken, LocalDateTime createdAt,
            LocalDateTime updatedAt, LocalDateTime deletedAt) {
        User user = new User(id, username, email, password, nationality, phoneNumber, image, role, verified,
                verificationToken, createdAt, updatedAt, deletedAt, null, null);
        return userRepository.put(user);
    }

    @Override
    public boolean delete(String id) {
        return userRepository.delete(id);
    }
}
