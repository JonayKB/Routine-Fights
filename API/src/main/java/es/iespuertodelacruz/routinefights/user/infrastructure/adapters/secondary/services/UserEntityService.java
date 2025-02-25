package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public User findById(Long id) {
        return userEntityMapper.toDomain(userRepository.findById(id).orElse(null));
    }

    /**
     * Method to save a user
     * 
     * @param user user
     * @return User
     */
    @Override
    public User post(User user) {
        if (user == null) {
            return null;
        }
        UserEntity userEntity = userEntityMapper.toEntity(user);
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
    public User put(User user) {
        if(user == null || user.getId() == null) {
            return null;
        }

        UserEntity userEntity = userRepository.findById(user.getId()).orElse(null);
        if(userEntity == null) {
            return null;
        }

        UserEntity updatedUserEntity = userEntityMapper.toEntity(user);
        updatedUserEntity.setCreatedAt(userEntity.getCreatedAt());
        userEntity.setUpdatedAt(LocalDateTime.now());
        return userEntityMapper.toDomain(userRepository.save(updatedUserEntity));
    }

    /**
     * Method to delete a user
     * 
     * @param id id
     * @return boolean
     */
    @Override
    public boolean delete(Long id) {
        userRepository.deleteById(id);
        return true;
    }
}
