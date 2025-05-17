package es.iespuertodelacruz.routinefights.user.domain.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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

    @Override
    public List<User> findFollowedUsersByEmail(String email, String usernameFilter) {
        return userRepository.findFollowedUsersByEmail(email, usernameFilter);
    }

    @Override
    public List<User> findFollowersByEmail(String email, String usernameFilter) {
        return userRepository.findFollowersByEmail(email, usernameFilter);
    }

    @Override
    public boolean followUser(String frEmail, String fdEmail) {
        return userRepository.followUser(frEmail, fdEmail);
    }

    @Override
    public boolean unfollowUser(String frEmail, String fdEmail) {
        return userRepository.unfollowUser(frEmail, fdEmail);
    }

    @Override
    public Set<String> findAllImages() {
        return userRepository.findAllImages();
    }

    @Override
    public boolean restore(String id) {
        return userRepository.restore(id);
    }

    @Override
    public User update(String id, String username, String email, String password, String nationality,
            String phoneNumber, String image) {
        User user = new User(id, username, email, password, nationality, phoneNumber, image);
        return userRepository.update(user);
    }

    @Override
    public boolean softDelete(String id) {
        return userRepository.softDelete(id);
    }

    @Override
    public List<User> findByUsername(String regex) {
        return userRepository.findByUsername(regex);
    }

    @Override
    public boolean subscribeActivity(String userEmail, String activityID) {
        return userRepository.subscribeActivity(userEmail, activityID);
    }

    @Override
    public boolean unSubscribeActivity(String userEmail, String activityID) {
        return userRepository.unSubscribeActivity(userEmail, activityID);
    }

    @Override
    public User findByEmailOnlyBase(String email) {
        return userRepository.findByEmailOnlyBase(email);
    }

    @Override
    public List<User> getPaginationByName(int page,int perPage, String userName, String userID) {
        int offset = (page - 1) * perPage;
        return userRepository.getPaginationByName(offset, perPage, userName, userID);
    }

    @Override
    public Boolean likePost(String userId, String postId) {
        return userRepository.likePost(userId, postId);
    }

    @Override
    public Boolean unLikePost(String userId, String postId) {
        return userRepository.unLikePost(userId, postId);
    }
    
}
