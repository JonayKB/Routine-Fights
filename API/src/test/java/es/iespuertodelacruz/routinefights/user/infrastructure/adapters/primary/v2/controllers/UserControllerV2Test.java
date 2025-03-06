package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.domain.services.UserService;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions.UserNotFoundException;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.dtos.UserInputDTOV2;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.dtos.UserOutputDTOV2;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.mappers.FollowerMapper;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.mappers.UserOutputV2Mapper;

@SpringBootTest
class UserControllerV2Test {
    private static final String TEST_EXCEPTION = "Test Exception";

    private UserControllerV2 userControllerV2;

    @Mock
    private UserOutputV2Mapper userOutputMapper;

    @Mock
    private FollowerMapper followerMapper;

    @Mock
    private UserService userService;

    private UserInputDTOV2 userInputDTOV2;
    private UserOutputDTOV2 userOutputDTOV2;

    @BeforeEach
    void setUp() {
        userControllerV2 = new UserControllerV2();
        userControllerV2.setFollowerMapper(followerMapper);
        userControllerV2.setUserOutputMapper(userOutputMapper);
        userControllerV2.setUserService(userService);

        userInputDTOV2 = new UserInputDTOV2("id", "username", "email", "password", "nationality", "phone_number",
                "image");
        userOutputDTOV2 = new UserOutputDTOV2(null, null, null, null, null, null, null, 0, 0);
    }

    @Test
    void findFollowedUsersByEmailTest() {
        when(userService.findFollowedUsersByEmail(anyString())).thenReturn(new ArrayList<User>());
        assertNotNull(userControllerV2.findFollowedUsersByEmail("email"));
    }

    @Test
    void findFollowedUsersByEmailExceptionTest() {
        when(userService.findFollowedUsersByEmail(anyString())).thenThrow(new UserNotFoundException(TEST_EXCEPTION));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userControllerV2.findFollowedUsersByEmail("email");
        });
        assertEquals("Error finding followed users", exception.getMessage());
    }

    @Test
    void findFollowersByEmailTest() {
        when(userService.findFollowersByEmail(anyString())).thenReturn(new ArrayList<User>());
        assertNotNull(userControllerV2.findFollowersByEmail("email"));
    }

    @Test
    void findFollowersByEmailExceptionTest() {
        when(userService.findFollowersByEmail(anyString())).thenThrow(new UserNotFoundException(TEST_EXCEPTION));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userControllerV2.findFollowersByEmail("email");
        });
        assertEquals("Error finding followers", exception.getMessage());
    }

    @Test
    void followUserTest() {
        when(userService.followUser(anyString(), anyString())).thenReturn(true);
        assertTrue(userControllerV2.followUser("frEmail", "fdEmail"));
    }

    @Test
    void followUserExceptionTest() {
        when(userService.followUser(anyString(), anyString())).thenThrow(new UserNotFoundException(TEST_EXCEPTION));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userControllerV2.followUser("frEmail", "fdEmail");
        });
        assertEquals("Error following user", exception.getMessage());
    }

    @Test
    void unfollowUserTest() {
        when(userService.unfollowUser(anyString(), anyString())).thenReturn(true);
        assertTrue(userControllerV2.unfollowUser("frEmail", "fdEmail"));
    }

    @Test
    void unfollowUserExceptionTest() {
        when(userService.unfollowUser(anyString(), anyString())).thenThrow(new UserNotFoundException(TEST_EXCEPTION));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userControllerV2.unfollowUser("frEmail", "fdEmail");
        });
        assertEquals("Error unfollowing user", exception.getMessage());
    }
}
