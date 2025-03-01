package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.services;

import java.time.LocalDateTime;
import java.util.List;

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
        return userEntityMapper.toDomain(userRepository.findAll());
    }

    @Override
    public User findById(String id) {
        try {
            UserEntity userEntity = userRepository.findById(id).orElse(null);
            if (userEntity != null) {
                if (userEntity.getFollowers() == null) {
                    userEntity.setFollowers(userRepository.findFollowersByEmail(userEntity.getEmail()));
                }
                if (userEntity.getFollowing() == null) {
                    userEntity.setFollowing(userRepository.findFollowedUsersByEmail(userEntity.getEmail()));
                }
            }
            return userEntityMapper.toDomain(userEntity);
        } catch (Exception e) {
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public User findByEmail(String email) {
        return userEntityMapper.toDomain(userRepository.findByEmail(email));
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public User post(User user) {
        if (user == null || user.getPassword() == null || user.getEmail() == null) {
            throw new UserSaveException("Data required");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserSaveException("Email already exists");
        }

        UserEntity userEntity = userEntityMapper.toEntity(user);
        // TODO: search followers and following number
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setUpdatedAt(LocalDateTime.now());
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
            throw new UserUpdateException("Data required");
        }

        UserEntity userEntity = userRepository.findById(user.getId()).orElse(null);
        if (userEntity == null) {
            throw new UserNotFoundException("User not found");
        }

        if (user.getPassword() != null && !user.getPassword().equals(userEntity.getPassword())) {
            userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userEntity.setUsername(user.getUsername());
        userEntity.setEmail(user.getEmail());
        userEntity.setNationality(user.getNationality());
        userEntity.setPhoneNumber(user.getPhoneNumber());
        userEntity.setImage(user.getImage());
        userEntity.setUpdatedAt(LocalDateTime.now());
        userEntity.setRole(user.getRole());
        userEntity.setVerified(user.getVerified());
        userEntity.setVerificationToken(user.getVerificationToken());
        // TODO: search followers and following number
        try {
            return userEntityMapper.toDomain(userRepository.save(userEntity));
        } catch (Exception e) {
            throw new UserUpdateException("Error updating user");
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

    public List<User> findFollowedUsersByEmail(String email) {
        return userEntityMapper.toDomain(userRepository.findFollowedUsersByEmail(email));
    }

    public List<User> findFollowersByEmail(String email) {
        return userEntityMapper.toDomain(userRepository.findFollowersByEmail(email));
    }

    @Transactional
    public boolean followUser(String email1, String email2) {
        return userRepository.followUser(email1, email2);
    }

    public List<String> findAllImages() {
        return userRepository.findAllImages();
    }
}
