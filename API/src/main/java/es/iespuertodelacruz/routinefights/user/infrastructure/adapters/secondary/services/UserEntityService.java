package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.domain.ports.secondary.IUserRepository;
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

    @Override
    public User findById(Long id) {
        return userEntityMapper.toDomain(userRepository.findById(id).orElse(null));
    }
}
