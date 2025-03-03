package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v3.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.domain.services.UserService;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions.UserDeleteException;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions.UserNotFoundException;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions.UserSaveException;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions.UserUpdateException;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v3.dtos.UserInputDTOV3;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v3.dtos.UserOutputDTOV3;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v3.mappers.UserOutputV3Mapper;

@SpringBootTest
class UserControllerV3Test {
    private UserControllerV3 userControllerV3;

    @Mock
    private UserOutputV3Mapper userOutputMapper;

    @Mock
    private UserService userService;

    private UserInputDTOV3 userInputDTOV3;
    private UserOutputDTOV3 userOutputDTOV3;

    @BeforeEach
    void setUp() {
        userControllerV3 = new UserControllerV3();
        userControllerV3.setUserOutputMapper(userOutputMapper);
        userControllerV3.setUserService(userService);

        userInputDTOV3 = new UserInputDTOV3("id", "username", "email", "password", "nationality", "phone_number",
                "image", "role", false, "verification_token", LocalDateTime.now(), LocalDateTime.now(),
                LocalDateTime.now());
        userOutputDTOV3 = new UserOutputDTOV3(null, null, null, null, null, null, null, false, null, null, null, null,
                null, null);
    }

    @Test
    void getUserOutputMapperTest() {
        assertNotNull(userControllerV3.getUserOutputMapper());
    }

    @Test
    void getUserServiceTest() {
        assertNotNull(userControllerV3.getUserService());
    }

    @Test
    void deleteTest() {
        when(userService.delete(anyString())).thenReturn(true);
        assertTrue(userControllerV3.delete("id"));
    }

    @Test
    void deleteExceptionTest() {
        when(userService.delete(anyString())).thenThrow(new UserDeleteException("Test Exception"));

        UserDeleteException exception = assertThrows(UserDeleteException.class, () -> {
            userControllerV3.delete("id");
        });
        assertEquals("Unable to delete the user", exception.getMessage());
    }

    @Test
    void findAllTest() {
        when(userService.findAll()).thenReturn(new ArrayList<User>());
        assertNotNull(userControllerV3.findAll());
    }

    @Test
    void findAllExceptionTest() {
        when(userService.findAll()).thenThrow(new UserNotFoundException("Test Exception"));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userControllerV3.findAll();
        });
        assertEquals("Error finding users", exception.getMessage());
    }

    @Test
    void findAllImagesTest() {
        when(userService.findAllImages()).thenReturn(new ArrayList<String>());
        assertNotNull(userControllerV3.findAllImages());
    }

    @Test
    void findAllImagesExceptionTest() {
        when(userService.findAllImages()).thenThrow(new UserNotFoundException("Test Exception"));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userControllerV3.findAllImages();
        });
        assertEquals("Error finding images", exception.getMessage());
    }

    @Test
    void findByIdTest() {
        when(userService.findById(anyString())).thenReturn(new User());
        when(userOutputMapper.tOutputDTOV3(any(User.class))).thenReturn(userOutputDTOV3);
        assertNotNull(userControllerV3.findById("id"));
    }

    @Test
    void findByIdExceptionTest() {
        when(userService.findById(anyString())).thenThrow(new UserNotFoundException("Test Exception"));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userControllerV3.findById("id");
        });
        assertEquals("Error finding user", exception.getMessage());
    }

    @Test
    void findFollowedUsersByEmailTest() {
        when(userService.findFollowedUsersByEmail(anyString())).thenReturn(new ArrayList<User>());
        assertNotNull(userControllerV3.findFollowedUsersByEmail("email"));
    }

    @Test
    void findFollowedUsersByEmailExceptionTest() {
        when(userService.findFollowedUsersByEmail(anyString())).thenThrow(new UserNotFoundException("Test Exception"));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userControllerV3.findFollowedUsersByEmail("email");
        });
        assertEquals("Error finding followed users", exception.getMessage());
    }

    @Test
    void findFollowersByEmailTest() {
        when(userService.findFollowersByEmail(anyString())).thenReturn(new ArrayList<User>());
        assertNotNull(userControllerV3.findFollowersByEmail("email"));
    }

    @Test
    void findFollowersByEmailExceptionTest() {
        when(userService.findFollowersByEmail(anyString())).thenThrow(new UserNotFoundException("Test Exception"));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userControllerV3.findFollowersByEmail("email");
        });
        assertEquals("Error finding followers", exception.getMessage());
    }

    @Test
    void followUserTest() {
        when(userService.followUser(anyString(), anyString())).thenReturn(true);
        assertTrue(userControllerV3.followUser("frEmail", "fdEmail"));
    }

    @Test
    void followUserExceptionTest() {
        when(userService.followUser(anyString(), anyString())).thenThrow(new UserNotFoundException("Test Exception"));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userControllerV3.followUser("frEmail", "fdEmail");
        });
        assertEquals("Error following user", exception.getMessage());
    }

    @Test
    void postTest() {
        when(userService.post(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyBoolean(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class),
                any(LocalDateTime.class))).thenReturn(new User());
        when(userOutputMapper.tOutputDTOV3(any(User.class))).thenReturn(userOutputDTOV3);
        assertNotNull(userControllerV3.post(userInputDTOV3));
    }

    @Test
    void postExceptionTest() {
        when(userService.post(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyBoolean(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class),
                any(LocalDateTime.class))).thenThrow(new UserSaveException("Test Exception"));

        UserSaveException exception = assertThrows(UserSaveException.class, () -> {
            userControllerV3.post(userInputDTOV3);
        });
        assertEquals("Unable to create the user", exception.getMessage());
    }

    @Test
    void putTest() {
        when(userService.put(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyBoolean(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class),
                any(LocalDateTime.class))).thenReturn(new User());
        when(userOutputMapper.tOutputDTOV3(any(User.class))).thenReturn(userOutputDTOV3);
        assertNotNull(userControllerV3.put(userInputDTOV3));
    }

    @Test
    void putExceptionTest() {
        when(userService.put(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyBoolean(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class),
                any(LocalDateTime.class))).thenThrow(new UserUpdateException("Test Exception"));

        UserUpdateException exception = assertThrows(UserUpdateException.class, () -> {
            userControllerV3.put(userInputDTOV3);
        });
        assertEquals("Unable to update the user", exception.getMessage());
    }

    @Test
    void unfollowUserTest() {
        when(userService.unfollowUser(anyString(), anyString())).thenReturn(true);
        assertTrue(userControllerV3.unfollowUser("frEmail", "fdEmail"));
    }

    @Test
    void unfollowUserExceptionTest() {
        when(userService.unfollowUser(anyString(), anyString())).thenThrow(new UserNotFoundException("Test Exception"));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userControllerV3.unfollowUser("frEmail", "fdEmail");
        });
        assertEquals("Error unfollowing user", exception.getMessage());
    }
}
