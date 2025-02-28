package es.iespuertodelacruz.routinefights.user.domain.ports.secondary;

import es.iespuertodelacruz.routinefights.user.common.IUserGeneric;
import es.iespuertodelacruz.routinefights.user.domain.User;

/**
 * IUserRepository
 */
public interface IUserRepository extends IUserGeneric {
    /**
     * Method to save a user
     * 
     * @param username username
     * @return User
     */
    public User post(User user);

    /**
     * Method to update a user
     * 
     * @param user user
     * @return User
     */
    public User put(User user);
}
