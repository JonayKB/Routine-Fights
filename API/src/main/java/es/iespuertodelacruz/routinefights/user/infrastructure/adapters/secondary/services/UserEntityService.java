package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.domain.ports.secondary.IUserRepository;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions.UserDeleteException;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions.UserNotFoundException;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions.UserSaveException;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions.UserUpdateException;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.mappers.IUserEntityMapper;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.repositories.IUserEntityRepository;

/**
 * UserEntityService
 */
@Service
public class UserEntityService implements IUserRepository {
    private static final String INVALID_PHONENUMBER = "Invalid phone number, should be: +[country code][phone number], example: +34612345678";
    private static final String INVALID_EMAIL = "Invalid email, should be: [email]@[domain].[tld], example: jonaykb@gmail.com";
    private static final String DATA_REQUIRED = "Data required";
    private static final String ERROR_UPDATING_USER = "Error updating user";
    private static final String USER_NOT_FOUND = "User not found";

    private IUserEntityRepository userRepository;
    private IUserEntityMapper userEntityMapper;
    private PasswordEncoder passwordEncoder;

    /**
     * Getter for userRepository
     * 
     * @return IUserEntityRepository
     */
    public IUserEntityRepository getUserRepository() {
        return this.userRepository;
    }

    /**
     * Setter for userRepository
     * 
     * @param userRepository
     */
    @Autowired
    public void setUserRepository(IUserEntityRepository userRepository) {
        this.userRepository = userRepository;
    }

    public IUserEntityMapper getUserEntityMapper() {
        return this.userEntityMapper;
    }

    @Autowired
    public void setUserEntityMapper(IUserEntityMapper userEntityMapper) {
        this.userEntityMapper = userEntityMapper;
    }

    public PasswordEncoder getPasswordEncoder() {
        return this.passwordEncoder;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAll() {
        try {
            return userEntityMapper.toDomain(userRepository.findAll());
        } catch (Exception e) {
            throw new UserNotFoundException("Users not found");
        }
    }

    @Override
    public User findById(String id) {
        try {
            return userEntityMapper.toDomain(userRepository.findById(id).orElse(null));
        } catch (Exception e) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
    }

    @Override
    public User findByEmail(String email) {
        try {
            return userEntityMapper.toDomain(userRepository.findByEmail(email));
        } catch (Exception e) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<User> findByUsername(String regex) {
        try {
            return userEntityMapper.toDomain(userRepository.findByUsername(regex));
        } catch (Exception e) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public User post(User user) {
        if (user == null || user.getPassword() == null || user.getEmail() == null || user.getCreatedAt() == null) {
            throw new UserSaveException(DATA_REQUIRED);
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserSaveException("Email already exists");
        }
        if (!checkIfEmailIsValid(user.getEmail())) {
            throw new UserUpdateException(
                    INVALID_EMAIL);
        }
        if (!checkIfPhoneNumberIsValid(user.getPhoneNumber())) {
            throw new UserUpdateException(
                    INVALID_PHONENUMBER);
        }

        UserEntity userEntity = userEntityMapper.toEntity(user);
        userEntity.setFollowers(new ArrayList<>());
        userEntity.setFollowing(new ArrayList<>());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            return userEntityMapper.toDomain(userRepository.save(userEntity));
        } catch (Exception e) {
            throw new UserSaveException("Error saving user");
        }
    }

    @Override
    @Transactional
    public User put(User user) {
        if (user == null || user.getId() == null) {
            throw new UserUpdateException(DATA_REQUIRED);
        }

        UserEntity userEntity = userRepository.findById(user.getId()).orElse(null);
        if (userEntity == null) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }

        if (user.getPassword() != null && !user.getPassword().equals(userEntity.getPassword())) {
            userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (!checkIfEmailIsValid(user.getEmail())) {
            throw new UserUpdateException(
                    INVALID_EMAIL);
        }
        if (!checkIfPhoneNumberIsValid(user.getPhoneNumber())) {
            throw new UserUpdateException(
                    INVALID_PHONENUMBER);
        }

        userEntity.setUsername(user.getUsername());
        userEntity.setEmail(user.getEmail());
        userEntity.setNationality(user.getNationality());
        userEntity.setPhoneNumber(user.getPhoneNumber());
        userEntity.setImage(user.getImage());
        userEntity.setCreatedAt(user.getCreatedAt());
        userEntity.setUpdatedAt(user.getUpdatedAt());
        userEntity.setDeletedAt(user.getDeletedAt());
        userEntity.setRole(user.getRole());
        userEntity.setVerified(user.getVerified());
        userEntity.setVerificationToken(user.getVerificationToken());
        try {
            return userEntityMapper.toDomain(userRepository.save(userEntity));
        } catch (Exception e) {
            throw new UserUpdateException(ERROR_UPDATING_USER);
        }
    }

    @Override
    @Transactional
    public User update(User user) {
        if (user == null || user.getId() == null) {
            throw new UserUpdateException(DATA_REQUIRED);
        }

        UserEntity userEntity = userRepository.findById(user.getId()).orElse(null);
        if (userEntity == null) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }

        if (!user.getEmail().equals(userEntity.getEmail())) {
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new UserUpdateException("Email already exists");
            }
            if (!checkIfEmailIsValid(user.getEmail())) {
                throw new UserUpdateException(
                        INVALID_EMAIL);
            }
            userEntity.setEmail(user.getEmail());
            userEntity.setVerified(false);
            userEntity.setVerificationToken(UUID.randomUUID().toString());
        }

        if (user.getPassword() != null && !user.getPassword().equals(userEntity.getPassword())) {
            userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (!checkIfPhoneNumberIsValid(user.getPhoneNumber())) {
            throw new UserUpdateException(
                    INVALID_PHONENUMBER);
        }

        userEntity.setUsername(user.getUsername());
        userEntity.setNationality(user.getNationality());
        userEntity.setPhoneNumber(user.getPhoneNumber());
        userEntity.setImage(user.getImage());
        userEntity.setUpdatedAt(LocalDateTime.now());
        try {
            return userEntityMapper.toDomain(userRepository.save(userEntity));
        } catch (Exception e) {
            throw new UserUpdateException(ERROR_UPDATING_USER);
        }
    }

    @Override
    @Transactional
    public boolean delete(String id) {
        try {
            userRepository.deleteById(id);
            return !userRepository.existsById(id);
        } catch (Exception e) {
            throw new UserDeleteException("Error deleting user");
        }
    }

    @Override
    @Transactional
    public boolean softDelete(String id) {
        UserEntity userEntity = userRepository.findById(id).orElse(null);
        if (userEntity == null) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }

        userEntity.setUpdatedAt(LocalDateTime.now());
        userEntity.setDeletedAt(LocalDateTime.now());
        try {
            return userRepository.save(userEntity).getDeletedAt() != null;
        } catch (Exception e) {
            throw new UserDeleteException("Error deleting user");
        }
    }

    @Override
    @Transactional
    public boolean restore(String id) {
        UserEntity userEntity = userRepository.findById(id).orElse(null);
        if (userEntity == null) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }

        userEntity.setUpdatedAt(LocalDateTime.now());
        userEntity.setDeletedAt(null);
        try {
            return userRepository.save(userEntity).getDeletedAt() == null;
        } catch (Exception e) {
            throw new UserUpdateException(ERROR_UPDATING_USER);
        }
    }

    @Override
    public List<User> findFollowedUsersByEmail(String email, String usernameFilter) {
        try {
            return userEntityMapper.toDomain(userRepository.findFollowedUsersByEmail(email, usernameFilter));
        } catch (Exception e) {
            throw new UserNotFoundException("Followed users not found");
        }
    }

    @Override
    public List<User> findFollowersByEmail(String email, String usernameFilter) {
        try {
            return userEntityMapper.toDomain(userRepository.findFollowersByEmail(email, usernameFilter));
        } catch (Exception e) {
            throw new UserNotFoundException("Followers not found");
        }
    }

    @Override
    @Transactional
    public boolean followUser(String frEmail, String fdEmail) {
        if (!userRepository.existsByEmail(frEmail) || !userRepository.existsByEmail(fdEmail)) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }

        try {
            return userRepository.followUser(frEmail, fdEmail);
        } catch (Exception e) {
            throw new UserNotFoundException("Error following user");
        }
    }

    @Override
    @Transactional
    public boolean unfollowUser(String frEmail, String fdEmail) {
        if (!userRepository.existsByEmail(frEmail) || !userRepository.existsByEmail(fdEmail)) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }

        try {
            return userRepository.unfollowUser(frEmail, fdEmail);
        } catch (Exception e) {
            throw new UserNotFoundException("Error unfollowing user");
        }
    }

    @Override
    public List<String> findAllImages() {
        try {
            return userRepository.findAllImages();
        } catch (Exception e) {
            throw new UserNotFoundException("Error finding images");
        }
    }

    @Override
    public boolean subscribeActivity(String userEmail,String activityID){
        try {
            return userRepository.susbcribeActivity(userEmail,activityID);
        } catch (Exception e) {
            throw new UserNotFoundException("Error subscribing activity: "+e.getMessage());
        }
    }

    @Override
    public boolean unSubscribeActivity(String userEmail,String activityID){
        try {
            return userRepository.unSusbcribeActivity(userEmail,activityID);
        } catch (Exception e) {
            throw new UserNotFoundException("Error unsuscribing activity: "+e.getMessage());
        }
    }

    
    boolean checkIfPhoneNumberIsValid(String phoneNumber) {
        return phoneNumber.matches("^\\+\\d{1,3}\\d{9}$");
    }

    boolean checkIfEmailIsValid(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    @Override
    public User findByEmailOnlyBase(String email) {
        try {
            return userEntityMapper.toDomain(userRepository.findByEmailOnlyBase(email));
        } catch (Exception e) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
    }

    @Override
    public List<User> getPaginationByName(int offset, int limit, String userName, String userID) {
        try {
            return userEntityMapper.toDomain(userRepository.getPaginationByName(offset, limit, userName,userID));
        } catch (Exception e) {
            throw new UserNotFoundException(e.getMessage());
        }
    }

    @Override
    public Boolean likePost(String userId, String postId) {
        try {
            return userRepository.likePost(userId, postId);
        } catch (Exception e) {
            throw new UserNotFoundException("Error liking post");
        }
    }

    @Override
    public Boolean unLikePost(String userId, String postId) {
        try {
            return userRepository.unLikePost(userId, postId);
        } catch (Exception e) {
            throw new UserNotFoundException("Error unliking post");
        }
    }
}
