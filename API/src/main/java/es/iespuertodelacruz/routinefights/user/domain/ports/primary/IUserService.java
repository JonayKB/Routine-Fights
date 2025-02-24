package es.iespuertodelacruz.routinefights.user.domain.ports.primary;

import java.util.List;

import es.iespuertodelacruz.routinefights.user.domain.User;

/**
 * IUserService
 */
public interface IUserService {
    /**
     * Method to find all users
     * 
     * @return List<User> list of users
     */
    public List<User> findAll();
}
