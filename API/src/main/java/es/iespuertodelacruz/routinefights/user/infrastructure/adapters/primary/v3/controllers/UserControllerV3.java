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
            throw new RuntimeException("Error finding users");
        }
        List<UserOutputDTOV3> usersDTO = users.stream().map(user -> userOutputMapper.tOutputDTOV3(user)).toList();
        logger.log(Level.INFO, "Users: {0}", usersDTO);
        return usersDTO;
    }

    @Secured("ROLE_ADMIN")
    @QueryMapping("userV3")
    public UserOutputDTOV3 findById(@Argument String id) {
        User user;
        try {
            user = userService.findById(id);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error finding user: {0}", e.getMessage());
            throw new RuntimeException("Error finding user");
        }
        UserOutputDTOV3 userDTO = userOutputMapper.tOutputDTOV3(user);
        logger.log(Level.INFO, "User found: {0}", userDTO);
        return userDTO;
    }

    @Secured("ROLE_ADMIN")
    @MutationMapping("saveUserV3")
    public UserOutputDTOV3 post(@Argument UserInputDTOV3 userInputDTO) {
        User user;
        try {
            user = userService.post(userInputDTO.username(), userInputDTO.email(),
                    userInputDTO.password(), userInputDTO.nationality(), userInputDTO.phoneNumber(),
                    userInputDTO.image(), userInputDTO.role(), userInputDTO.verified(),
                    userInputDTO.verificationToken(), userInputDTO.createdAt(), userInputDTO.updatedAt(),
                    userInputDTO.deletedAt());
        } catch (Exception e) {
            logger.log(Level.WARNING, "Unable to create the user: {0}", e.getMessage());
            throw new RuntimeException("Unable to create the user");
        }
        return userOutputMapper.tOutputDTOV3(user);
    }

    @Secured("ROLE_ADMIN")
    @MutationMapping("updateUserV3")
    public UserOutputDTOV3 put(@Argument UserInputDTOV3 userInputDTO) {
        User user;
        try {
            user = userService.put(userInputDTO.id(), userInputDTO.username(), userInputDTO.email(),
                    userInputDTO.password(), userInputDTO.nationality(), userInputDTO.phoneNumber(),
                    userInputDTO.image(), userInputDTO.role(), userInputDTO.verified(),
                    userInputDTO.verificationToken(), userInputDTO.createdAt(), userInputDTO.updatedAt(),
                    userInputDTO.deletedAt());
        } catch (Exception e) {
            logger.log(Level.WARNING, "Unable to update the user: {0}", e.getMessage());
            throw new RuntimeException("Unable to update the user");
        }
        return userOutputMapper.tOutputDTOV3(user);
    }

    @Secured("ROLE_ADMIN")
    @MutationMapping("deleteUser")
    public boolean delete(@Argument String id) {
        try {
            return userService.delete(id);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Unable to delete the user: {0}", e.getMessage());
            throw new RuntimeException("Unable to delete the user");
        }
    }
}
