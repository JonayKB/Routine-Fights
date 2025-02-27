package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.domain.ports.secondary.IUserRepository;
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

    /**
     * Method to find all users
     * 
     * @return List<User> list of users
     */
    @Override
    public List<User> findAll() {
        return userEntityMapper.toDomain(userRepository.findAll());
    }

    /**
     * Method to find a user by id
     * 
     * @param id id of the user
     * @return User
     */
    @Override
    public User findById(String id) {
        return userEntityMapper.toDomain(userRepository.findById(id).orElse(null));
    }

    @Override
    public User findByEmail(String email) {
        return userEntityMapper.toDomain(userRepository.findByEmail(email));
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Method to save a user
     * 
     * @param user user
     * @return User
     */
    @Override
    @Transactional
    public User post(User user) {
        if (user == null || user.getPassword() == null) {
            return null;
        }

        UserEntity userEntity = userEntityMapper.toEntity(user);
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setUpdatedAt(LocalDateTime.now());
        return userEntityMapper.toDomain(userRepository.save(userEntity));
    }

    /**
     * Method to update a user
     * 
     * @param user user
     * @return User
     */
    @Override
    @Transactional
    public User put(User user) {
        if (user == null) {
            return null;
        }

        UserEntity userEntity = userRepository.findById(user.getId()).orElse(null);
        if (userEntity == null) {
            throw new RuntimeException("User not found");
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
        return userEntityMapper.toDomain(userRepository.save(userEntity));
    }

    /**
     * Method to delete a user
     * 
     * @param id id
     * @return boolean
     */
    @Override
    @Transactional
    public boolean delete(String id) {
        userRepository.deleteById(id);
        return true;
    }
}
