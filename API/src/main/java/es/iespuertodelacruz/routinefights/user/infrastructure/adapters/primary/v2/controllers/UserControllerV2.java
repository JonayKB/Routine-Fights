package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import es.iespuertodelacruz.routinefights.user.domain.ports.primary.IUserService;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.dtos.UserDTOV2;

/**
 * UserControllerV2
 * 
 * @see <a href="https://www.baeldung.com/spring-graphql">Spring GraphQL
 *      Controllers</a>
 */
@Controller
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

    // TODO: Implement the method
    @QueryMapping
    public List<UserDTOV2> findAll() {
        return null;
    }
}
