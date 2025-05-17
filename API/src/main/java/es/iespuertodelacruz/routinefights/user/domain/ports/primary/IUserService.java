package es.iespuertodelacruz.routinefights.user.domain.ports.primary;

import java.time.LocalDateTime;

import es.iespuertodelacruz.routinefights.user.common.IUserCommon;
import es.iespuertodelacruz.routinefights.user.domain.User;

/**
 * IUserService
 */
public interface IUserService extends IUserCommon<User> {
        /**
         * Method to save a user
         * 
         * @param username          username
         * @param email             email
         * @param password          password
         * @param nationality       nationality
         * @param phoneNumber       phoneNumber
         * @param image             image
         * @param role              role
         * @param verified          verified
         * @param verificationToken verificationToken
         * @param createdAt         createdAt
         * @param updatedAt         updatedAt
         * @param deletedAt         deletedAt
         * @return User
         */
        public User post(String username, String email, String password, String nationality, String phoneNumber,
                        String image, String role, boolean verified, String verificationToken, LocalDateTime createdAt,
                        LocalDateTime updatedAt, LocalDateTime deletedAt);

        /**
         * Method to update a user
         * 
         * @param id                id
         * @param username          username
         * @param email             email
         * @param password          password
         * @param nationality       nationality
         * @param phoneNumber       phoneNumber
         * @param image             image
         * @param role              role
         * @param verified          verified
         * @param verificationToken verificationToken
         * @param createdAt         createdAt
         * @param updatedAt         updatedAt
         * @param deletedAt         deletedAt
         * @return User
         */
        public User put(String id, String username, String email, String password, String nationality,
                        String phoneNumber, String image, String role, boolean verified, String verificationToken,
                        LocalDateTime createdAt,
                        LocalDateTime updatedAt, LocalDateTime deletedAt);

        /**
         * Method to update a user (for v2)
         * 
         * @param id          id
         * @param username    username
         * @param email       email
         * @param password    password
         * @param nationality nationality
         * @param phoneNumber phoneNumber
         * @param image       image
         * @return User
         */
        public User update(String id, String username, String email, String password, String nationality,
                        String phoneNumber, String image);


}
