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
    private static final String TEST_EXCEPTION = "Test Exception";

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
        when(userService.delete(anyString())).thenThrow(new UserDeleteException(TEST_EXCEPTION));

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
        when(userService.findAll()).thenThrow(new UserNotFoundException(TEST_EXCEPTION));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userControllerV3.findAll();
        });
        assertEquals("Error finding users", exception.getMessage());
    }

    @Test
    void findByIdTest() {
        when(userService.findById(anyString())).thenReturn(new User());
        when(userOutputMapper.toDTO(any(User.class))).thenReturn(userOutputDTOV3);
        assertNotNull(userControllerV3.findById("id"));
    }

    @Test
    void findByIdExceptionTest() {
        when(userService.findById(anyString())).thenThrow(new UserNotFoundException(TEST_EXCEPTION));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userControllerV3.findById("id");
        });
        assertEquals("Error finding user", exception.getMessage());
    }

    @Test
    void postTest() {
        when(userService.post(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyBoolean(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class),
                any(LocalDateTime.class))).thenReturn(new User());
        when(userOutputMapper.toDTO(any(User.class))).thenReturn(userOutputDTOV3);
        assertNotNull(userControllerV3.post(userInputDTOV3));
    }

    @Test
    void postExceptionTest() {
        when(userService.post(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyBoolean(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class),
                any(LocalDateTime.class))).thenThrow(new UserSaveException(TEST_EXCEPTION));

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
        when(userOutputMapper.toDTO(any(User.class))).thenReturn(userOutputDTOV3);
        assertNotNull(userControllerV3.put(userInputDTOV3));
    }

    @Test
    void putExceptionTest() {
        when(userService.put(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyBoolean(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class),
                any(LocalDateTime.class))).thenThrow(new UserUpdateException(TEST_EXCEPTION));

        UserUpdateException exception = assertThrows(UserUpdateException.class, () -> {
            userControllerV3.put(userInputDTOV3);
        });
        assertEquals("Unable to update the user", exception.getMessage());
    }

    @Test
    void restoreUserTest() {
        when(userService.restore(anyString())).thenReturn(true);
        assertTrue(userControllerV3.restoreUser("id"));
    }

    @Test
    void restoreUserExceptionTest() {
        when(userService.restore(anyString())).thenThrow(new UserUpdateException(TEST_EXCEPTION));

        UserUpdateException exception = assertThrows(UserUpdateException.class, () -> {
            userControllerV3.restoreUser("id");
        });
        assertEquals("Unable to restore the user", exception.getMessage());
    }
}
