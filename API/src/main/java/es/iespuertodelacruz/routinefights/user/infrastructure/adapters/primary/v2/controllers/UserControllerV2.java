package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.domain.ports.primary.IUserService;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.dtos.UserDTOV2;

/**
 * UserControllerV2
 * 
 * @see <a href="https://www.baeldung.com/spring-graphql">Spring GraphQL
 *      Controllers</a>
 */
@Controller
@CrossOrigin
public class UserControllerV2 {
    Logger logger = LoggerFactory.getLogger(UserControllerV2.class);

    private IUserService userService;

    public IUserService getUserService() {
        return this.userService;
    }

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @QueryMapping("users")
    public List<UserDTOV2> findAll() {
        logger.info("Find all users");
        List<User> users;
        try {
            users = userService.findAll();
        } catch (Exception e) {
            logger.error("Error finding all users: {}", e.getMessage());
            return null;
        }
        List<UserDTOV2> usersDTO = users.stream().map(user -> new UserDTOV2(user.getId(), user.getUsername(),
                user.getEmail(), user.getNationality(), user.getPhoneNumber())).toList();
        logger.info("Users: {}", usersDTO);
        return usersDTO;
    }

    @QueryMapping("user")
    public UserDTOV2 findById(@Argument Long id) {
        logger.info("Find user by id");
        User user;
        try {
            user = userService.findById(id);
        } catch (Exception e) {
            logger.error("Error finding all users: {}", e.getMessage());
            return null;
        }
        UserDTOV2 usersDTO = new UserDTOV2(user.getId(), user.getUsername(),
                user.getEmail(), user.getNationality(), user.getPhoneNumber());
        logger.info("Users: {}", usersDTO);
        return usersDTO;
    }
}
