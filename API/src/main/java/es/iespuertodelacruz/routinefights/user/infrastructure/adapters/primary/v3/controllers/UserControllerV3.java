package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v3.controllers;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.domain.ports.primary.IUserService;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions.UserDeleteException;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions.UserNotFoundException;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions.UserSaveException;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions.UserUpdateException;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v3.dtos.UserInputDTOV3;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v3.dtos.UserOutputDTOV3;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v3.mappers.UserOutputV3Mapper;

/**
 * UserControllerV3
 */
@Controller
@CrossOrigin
public class UserControllerV3 {
    Logger logger = Logger.getLogger(UserControllerV3.class.getName());

    private IUserService userService;
    private UserOutputV3Mapper userOutputMapper;

    public UserOutputV3Mapper getUserOutputMapper() {
        return this.userOutputMapper;
    }

    @Autowired
    public void setUserOutputMapper(UserOutputV3Mapper userOutputMapper) {
        this.userOutputMapper = userOutputMapper;
    }

    public IUserService getUserService() {
        return this.userService;
    }

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @Secured("ROLE_ADMIN")
    @QueryMapping("usersV3")
    public List<UserOutputDTOV3> findAll() {
        List<User> users;
        try {
            users = userService.findAll();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error finding users: {0}", e.getMessage());
            throw new UserNotFoundException("Error finding users");
        }
        return userOutputMapper.tOutputDTOV3(users);
    }

    @Secured("ROLE_ADMIN")
    @QueryMapping("userV3")
    public UserOutputDTOV3 findById(@Argument String id) {
        User user;
        try {
            user = userService.findById(id);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error finding user: {0}", e.getMessage());
            throw new UserNotFoundException("Error finding user");
        }
        return userOutputMapper.tOutputDTOV3(user);
    }

    @Secured("ROLE_ADMIN")
    @MutationMapping("saveUserV3")
    public UserOutputDTOV3 post(@Argument UserInputDTOV3 user) {
        User userDomain;
        try {
            userDomain = userService.post(user.username(), user.email(),
                    user.password(), user.nationality(), user.phoneNumber(),
                    user.image(), user.role(), user.verified(),
                    user.verificationToken(), user.createdAt(), user.updatedAt(),
                    user.deletedAt());
        } catch (Exception e) {
            logger.log(Level.WARNING, "Unable to create the user: {0}", e.getMessage());
            throw new UserSaveException("Unable to create the user");
        }
        return userOutputMapper.tOutputDTOV3(userDomain);
    }

    @Secured("ROLE_ADMIN")
    @MutationMapping("updateUserV3")
    public UserOutputDTOV3 put(@Argument UserInputDTOV3 user) {
        User userDomain;
        try {
            userDomain = userService.put(user.id(), user.username(), user.email(),
                    user.password(), user.nationality(), user.phoneNumber(),
                    user.image(), user.role(), user.verified(),
                    user.verificationToken(), user.createdAt(), user.updatedAt(),
                    user.deletedAt());
        } catch (Exception e) {
            logger.log(Level.WARNING, "Unable to update the user: {0}", e.getMessage());
            throw new UserUpdateException("Unable to update the user");
        }
        return userOutputMapper.tOutputDTOV3(userDomain);
    }

    @Secured("ROLE_ADMIN")
    @MutationMapping("deleteUserV3")
    public boolean delete(@Argument String id) {
        try {
            return userService.delete(id);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Unable to delete the user: {0}", e.getMessage());
            throw new UserDeleteException("Unable to delete the user");
        }
    }

    @Secured("ROLE_ADMIN")
    @MutationMapping("followedByEmailV3")
    public List<UserOutputDTOV3> findFollowedUsersByEmail(String email) {
        List<User> following;
        try {
            following = userService.findFollowedUsersByEmail(email);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error finding followed users: {0}", e.getMessage());
            throw new UserNotFoundException("Error finding followed users");
        }
        return userOutputMapper.tOutputDTOV3(following);
    }

    @Secured("ROLE_ADMIN")
    @MutationMapping("followersByEmailV3")
    public List<UserOutputDTOV3> findFollowersByEmail(String email) {
        List<User> followers;
        try {
            followers = userService.findFollowersByEmail(email);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error finding followers: {0}", e.getMessage());
            throw new UserNotFoundException("Error finding followers");
        }
        return userOutputMapper.tOutputDTOV3(followers);
    }

    @Secured("ROLE_ADMIN")
    @MutationMapping("followUserV3")
    public boolean followUser(String frEmail, String fdEmail) {
        return userService.followUser(frEmail, fdEmail);
    }

    @Secured("ROLE_ADMIN")
    @MutationMapping("unfollowUserV3")
    public boolean unfollowUser(String frEmail, String fdEmail) {
        return userService.unfollowUser(fdEmail, fdEmail);
    }

    @MutationMapping("images")
    public List<String> findAllImages() {
        return userService.findAllImages();
    }
}
