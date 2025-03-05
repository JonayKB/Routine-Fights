package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.controllers;

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
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions.UserNotFoundException;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.dtos.Follower;
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

    @QueryMapping("followedByEmail")
    public List<Follower> findFollowedUsersByEmail(@Argument String email) {
        List<User> following;
        try {
            following = userService.findFollowedUsersByEmail(email);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error finding followed users: {0}", e.getMessage());
            throw new UserNotFoundException("Error finding followed users");
        }
        return followerMapper.toFollower(following);
    }

    @Secured("ROLE_ADMIN")
    @QueryMapping("followersByEmail")
    public List<Follower> findFollowersByEmail(@Argument String email) {
        List<User> followers;
        try {
            followers = userService.findFollowersByEmail(email);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error finding followers: {0}", e.getMessage());
            throw new UserNotFoundException("Error finding followers");
        }
        return followerMapper.toFollower(followers);
    }

    @Secured("ROLE_ADMIN")
    @MutationMapping("followUser")
    public boolean followUser(@Argument String followerEmail, @Argument String followingEmail) {
        try {
            return userService.followUser(followerEmail, followingEmail);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error following user: {0}", e.getMessage());
            throw new UserNotFoundException("Error following user");
        }
    }

    @Secured("ROLE_ADMIN")
    @MutationMapping("unfollowUser")
    public boolean unfollowUser(@Argument String followerEmail, @Argument String followingEmail) {
        try {
            return userService.unfollowUser(followerEmail, followingEmail);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error unfollowing user: {0}", e.getMessage());
            throw new UserNotFoundException("Error unfollowing user");
        }
    }

    @QueryMapping("images")
    public List<String> findAllImages() {
        try {
            return userService.findAllImages();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error finding images: {0}", e.getMessage());
            throw new UserNotFoundException("Error finding images");
        }
    }
}
