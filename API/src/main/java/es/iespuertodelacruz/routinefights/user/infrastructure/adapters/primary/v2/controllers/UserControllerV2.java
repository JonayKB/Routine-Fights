package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.controllers;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import es.iespuertodelacruz.routinefights.shared.services.MailService;
import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.domain.ports.primary.IUserService;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions.UserDeleteException;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions.UserNotFoundException;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions.UserUpdateException;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.dtos.Follower;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.dtos.UserInputDTOV2;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.dtos.UserOutputDTOV2;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.mappers.FollowerMapper;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.mappers.UserOutputV2Mapper;

/**
 * UserControllerV2
 */
@Controller
@CrossOrigin
public class UserControllerV2 {
    Logger logger = Logger.getLogger(UserControllerV2.class.getName());

    private IUserService userService;
    private UserOutputV2Mapper userOutputMapper;
    private FollowerMapper followerMapper;
    private MailService mailService;

    public MailService getMailService() {
        return this.mailService;
    }

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public FollowerMapper getFollowerMapper() {
        return this.followerMapper;
    }

    @Autowired
    public void setFollowerMapper(FollowerMapper followerMapper) {
        this.followerMapper = followerMapper;
    }

    public UserOutputV2Mapper getUserOutputMapper() {
        return this.userOutputMapper;
    }

    @Autowired
    public void setUserOutputMapper(UserOutputV2Mapper userOutputMapper) {
        this.userOutputMapper = userOutputMapper;
    }

    public IUserService getUserService() {
        return this.userService;
    }

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @QueryMapping("usersV2")
    public List<UserOutputDTOV2> findUsersByUsername(@Argument String regex) {
        try {
            return userOutputMapper.toOutputDTOV2(userService.findByUsername(regex));
        } catch (Exception e) {
            logger.log(Level.WARNING, "(findUsersByUsername) Error finding users: {0}", e.getMessage());
            throw new UserNotFoundException("Error finding users");
        }
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @QueryMapping("userV2")
    public UserOutputDTOV2 findById(@Argument String id) {
        User user;
        try {
            user = userService.findById(id);
        } catch (Exception e) {
            logger.log(Level.WARNING, "(findById) Error finding user: {0}", e.getMessage());
            throw new UserNotFoundException("Error finding user");
        }
        return userOutputMapper.toOutputDTOV2(user);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @QueryMapping("followedByEmail")
    public List<Follower> findFollowedUsersByEmail(@Argument String email) {
        List<User> following;
        User self;
        try {
            following = userService.findFollowedUsersByEmail(email);
            self = userService.findByEmail(email);
        } catch (Exception e) {
            logger.log(Level.WARNING, "(findFollowedUsersByEmail) Error finding followed users: {0}", e.getMessage());
            throw new UserNotFoundException("Error finding followed users");
        }
        return followerMapper.toFollower(following,self);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @QueryMapping("followersByEmail")
    public List<Follower> findFollowersByEmail(@Argument String email) {
        List<User> followers;
        User self;
        try {
            followers = userService.findFollowersByEmail(email);
            self = userService.findByEmailOnlyBase(email);
        } catch (Exception e) {
            logger.log(Level.WARNING, "(findFollowersByEmail) Error finding followers: {0}", e.getMessage());
            throw new UserNotFoundException("Error finding followers");
        }
        return followerMapper.toFollower(followers,self);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @MutationMapping("followUser")
    public boolean followUser(@Argument String followingEmail) {
        String followerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            return userService.followUser(followerEmail, followingEmail);
        } catch (Exception e) {
            logger.log(Level.WARNING, "(followUser) Error following user: {0}", e.getMessage());
            throw new UserUpdateException("Error following user");
        }
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @MutationMapping("unfollowUser")
    public boolean unfollowUser( @Argument String followingEmail) {
        String followerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            return userService.unfollowUser(followerEmail, followingEmail);
        } catch (Exception e) {
            logger.log(Level.WARNING, "(unfollowUser) Error unfollowing user: {0}", e.getMessage());
            throw new UserUpdateException("Error unfollowing user");
        }
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @MutationMapping("updateUserV2")
    public UserOutputDTOV2 update(@Argument UserInputDTOV2 user) {
        User userDomain;
        try {
            userDomain = userService.update(user.id(), user.username(), user.email(), user.password(),
                    user.nationality(), user.phoneNumber(), user.image());

            if (userDomain != null && (!userDomain.getVerified() && userDomain.getVerificationToken() != null)) {
                mailService.sentVerifyToken(userDomain.getEmail(), "Verify your email: " + userDomain.getUsername(),
                        userDomain.getVerificationToken());
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "(update) Unable to update the user: {0}", e.getMessage());
            throw new UserUpdateException("Unable to update the user");
        }
        return userOutputMapper.toOutputDTOV2(userDomain);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @MutationMapping("deleteUserV2")
    public boolean softDelete(@Argument String id) {
        try {
            return userService.softDelete(id);
        } catch (Exception e) {
            logger.log(Level.WARNING, "(delete) Unable to delete the user: {0}", e.getMessage());
            throw new UserDeleteException("Unable to delete the user");
        }
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @MutationMapping("subscribeActivity")
    public boolean subscribeActivity(@Argument String activityID) {
        try {
            return userService.subscribeActivity(SecurityContextHolder.getContext().getAuthentication().getName(),
                    activityID);
        } catch (Exception e) {
            logger.log(Level.WARNING, "(subscribeActivity) Unable to subscribe the user to the activity: {0}",
                    e.getMessage());
            throw new UserUpdateException("Unable to subscribe the user to the activity");
        }
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @MutationMapping("unSubscribeActivity")
    public boolean unSubscribeActivity(@Argument String activityID) {
        try {
            return userService.unSubscribeActivity(SecurityContextHolder.getContext().getAuthentication().getName(),
                    activityID);
        } catch (Exception e) {
            logger.log(Level.WARNING, "(unSubscribeActivity) Unable to unsubscribe the user to the activity: {0}",
                    e.getMessage());
            throw new UserUpdateException("Unable to unsubscribe the user to the activity");
        }
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @QueryMapping("getOwnUser")
    public UserOutputDTOV2 getOwnUser() {
        User user;
        try {
            user = userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        } catch (Exception e) {
            logger.log(Level.WARNING, "(getOwnUser) Error finding user: {0}", e.getMessage());
            throw new UserNotFoundException("Error finding user");
        }
        return userOutputMapper.toOutputDTOV2(user);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @QueryMapping("getUserPaginationByName")
    public List<UserOutputDTOV2> getUserPaginationByName(@Argument int page, @Argument int perPage,
            @Argument String userName) {
        try {
            if (page < 0 || perPage <= 0) {
                throw new IllegalArgumentException("Page and perPage must be greater than 0");
            }
            User self = userService.findByEmailOnlyBase(SecurityContextHolder.getContext().getAuthentication().getName());
            return userOutputMapper.toOutputDTOV2(userService.getPaginationByName(page, perPage, userName, self.getId()));
        } catch (Exception e) {
            logger.log(Level.WARNING, "(getUserPaginationByName) Error finding users: {0}", e.getMessage());
            throw new UserNotFoundException("Error finding users");
        }
    }

}
