package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import es.iespuertodelacruz.routinefights.shared.services.MailService;
import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.domain.services.UserService;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions.UserDeleteException;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions.UserNotFoundException;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions.UserUpdateException;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.dtos.UserInputDTOV2;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.dtos.UserOutputDTOV2;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.mappers.FollowerMapper;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.mappers.UserOutputV2Mapper;
import es.iespuertodelacruz.routinefights.user.utils.UserInitializer;

@SpringBootTest
class UserControllerV2Test extends UserInitializer {
    private static final String TEST_EXCEPTION = "Test Exception";

    private UserControllerV2 userControllerV2;

    @Mock
    private UserOutputV2Mapper userOutputMapper;

    @Mock
    private FollowerMapper followerMapper;

    @Mock
    private UserService userService;

    @Mock
    private MailService mailService;

    private UserInputDTOV2 userInputDTOV2;
    private UserOutputDTOV2 userOutputDTOV2;

    @BeforeEach
    void init() {
        userControllerV2 = new UserControllerV2();
        userControllerV2.setFollowerMapper(followerMapper);
        userControllerV2.setUserOutputMapper(userOutputMapper);
        userControllerV2.setUserService(userService);
        userControllerV2.setMailService(mailService);

        userInputDTOV2 = new UserInputDTOV2("id", "username", "email", "password", "nationality", "phone_number",
                "image");
        userOutputDTOV2 = new UserOutputDTOV2(null, null, null, null, null, null, null, 0, 0);
    }

    @Test
    void getFollowerMapperTest() {
        assertNotNull(userControllerV2.getFollowerMapper(), TEST_EXCEPTION);
    }

    @Test
    void getUserOutputMapperTest() {
        assertNotNull(userControllerV2.getUserOutputMapper(), TEST_EXCEPTION);
    }

    @Test
    void getUserServiceTest() {
        assertNotNull(userControllerV2.getUserService(), TEST_EXCEPTION);
    }

    @Test
    void getMailServiceTest() {
        assertNotNull(userControllerV2.getMailService(), TEST_EXCEPTION);
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
        when(userService.followUser(anyString(), anyString())).thenThrow(new UserUpdateException(TEST_EXCEPTION));

        UserUpdateException exception = assertThrows(UserUpdateException.class, () -> {
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
        when(userService.unfollowUser(anyString(), anyString())).thenThrow(new UserUpdateException(TEST_EXCEPTION));

        UserUpdateException exception = assertThrows(UserUpdateException.class, () -> {
            userControllerV2.unfollowUser("frEmail", "fdEmail");
        });
        assertEquals("Error unfollowing user", exception.getMessage());
    }

    @Test
    void deleteTest() {
        when(userService.delete(anyString())).thenReturn(true);
        assertTrue(userControllerV2.delete("id"));
    }

    @Test
    void deleteExceptionTest() {
        when(userService.delete(anyString())).thenThrow(new UserDeleteException(TEST_EXCEPTION));
        UserDeleteException exception = assertThrows(UserDeleteException.class, () -> {
            userControllerV2.delete("id");
        });
        assertEquals("Unable to delete the user", exception.getMessage());
    }

    @Test
    void findByIdTest() {
        when(userService.findById(anyString())).thenReturn(new User());
        when(userOutputMapper.toOutputDTOV2(any(User.class))).thenReturn(userOutputDTOV2);
        assertNotNull(userControllerV2.findById("id"));
    }

    @Test
    void findByIdExceptionTest() {
        when(userService.findById(anyString())).thenThrow(new UserNotFoundException(TEST_EXCEPTION));
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userControllerV2.findById("id");
        });
        assertEquals("Error finding user", exception.getMessage());
    }

    @Test
    void findUsersByUsernameTest() {
        when(userService.findByUsername(anyString())).thenReturn(new ArrayList<User>());
        assertNotNull(userControllerV2.findUsersByUsername("username"));
    }

    @Test
    void findUsersByUsernameExceptionTest() {
        when(userService.findByUsername(anyString())).thenThrow(new UserNotFoundException(TEST_EXCEPTION));
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userControllerV2.findUsersByUsername("username");
        });
        assertEquals("Error finding users", exception.getMessage());
    }

    @Test
    void updateTest() {
        when(userService.update(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString()))
                .thenReturn(new User());
        when(userOutputMapper.toOutputDTOV2(any(User.class))).thenReturn(userOutputDTOV2);
        assertNotNull(userControllerV2.update(userInputDTOV2));
    }

    @Test
    void updateWithMailTest() {
        when(userService.update(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString()))
                .thenReturn(user);
        when(userOutputMapper.toOutputDTOV2(any(User.class))).thenReturn(userOutputDTOV2);
        assertNotNull(userControllerV2.update(userInputDTOV2));
    }

    @Test
    void updateExceptionTest() {
        when(userService.update(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString()))
                .thenThrow(new UserUpdateException(TEST_EXCEPTION));
        UserUpdateException exception = assertThrows(UserUpdateException.class, () -> {
            userControllerV2.update(userInputDTOV2);
        });
        assertEquals("Unable to update the user", exception.getMessage());
    }
}
