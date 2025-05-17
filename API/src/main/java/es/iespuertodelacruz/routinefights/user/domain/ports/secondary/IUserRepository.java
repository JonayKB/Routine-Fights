package es.iespuertodelacruz.routinefights.user.domain.ports.secondary;

import es.iespuertodelacruz.routinefights.user.common.IUserCommon;
import es.iespuertodelacruz.routinefights.user.domain.User;

/**
 * IUserRepository
 */
public interface IUserRepository extends IUserCommon<User> {
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

    /**
     * Method to update a user (for v2)
     * 
     * @param user user
     * @return User
     */
    public User update(User user);


}
