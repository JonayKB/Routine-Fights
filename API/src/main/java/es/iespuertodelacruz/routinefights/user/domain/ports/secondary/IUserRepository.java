package es.iespuertodelacruz.routinefights.user.domain.ports.secondary;

import java.util.List;

import es.iespuertodelacruz.routinefights.user.domain.User;

/**
 * IUserRepository
 */
public interface IUserRepository {
    /**
     * Method to find all users
     * 
     * @return List<User> list of users
     */
    public List<User> findAll();
}
