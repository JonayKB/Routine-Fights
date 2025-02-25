package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v3.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.domain.ports.primary.IUserService;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v3.dtos.UserDTOV3;

/**
 * UserControllerV3
 * 
 * @see <a href="https://www.baeldung.com/spring-graphql">Spring GraphQL
 *      Controllers</a>
 */
@Controller
@CrossOrigin
public class UserControllerV3 {
    Logger logger = LoggerFactory.getLogger(UserControllerV3.class);

    private IUserService userService;

    public IUserService getUserService() {
        return this.userService;
    }

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @QueryMapping("users")
    public List<UserDTOV3> findAll() {
        logger.info("Find all users");
        List<User> users;
        try {
            users = userService.findAll();
        } catch (Exception e) {
            logger.error("Error finding all users: {}", e.getMessage());
            return null;
        }
        List<UserDTOV3> usersDTO = users.stream().map(user -> new UserDTOV3(user.getId(), user.getUsername(),
                user.getEmail(), user.getNationality(), user.getPhoneNumber())).toList();
        logger.info("Users: {}", usersDTO);
        return usersDTO;
    }

    @QueryMapping("user")
    public UserDTOV3 findById(@Argument Long id) {
        logger.info("Find user by id");
        User user;
        try {
            user = userService.findById(id);
        } catch (Exception e) {
            logger.error("Error finding all users: {}", e.getMessage());
            return null;
        }
        UserDTOV3 usersDTO = new UserDTOV3(user.getId(), user.getUsername(),
                user.getEmail(), user.getNationality(), user.getPhoneNumber());
        logger.info("Users: {}", usersDTO);
        return usersDTO;
    }

    @MutationMapping("saveUser")
    public UserDTOV3 post(@Argument String username, @Argument String email,
            @Argument String password, @Argument String nationality, @Argument String phoneNumber) {
        User user = userService.post(username, email, password, nationality, phoneNumber);
        return new UserDTOV3(user.getId(), user.getUsername(), user.getEmail(), user.getNationality(),
                user.getPhoneNumber());
    }

    @MutationMapping("updateUser")
    public UserDTOV3 put(@Argument Long id, @Argument String username, @Argument String email,
            @Argument String password, @Argument String nationality, @Argument String phoneNumber) {
        User user = userService.put(id, username, email, password, nationality, phoneNumber);
        return new UserDTOV3(user.getId(), user.getUsername(), user.getEmail(), user.getNationality(),
                user.getPhoneNumber());
    }

    @MutationMapping("deleteUser")
    public boolean delete(@Argument Long id) {
        return userService.delete(id);
    }
}
