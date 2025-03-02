package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v3.controllers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    void findAllTest() {
        when(userService.findAll()).thenReturn(new ArrayList<User>());
        assertNotNull(userControllerV3.findAll());
    }

    @Test
    void findAllImagesTest() {
        when(userService.findAllImages()).thenReturn(new ArrayList<String>());
        assertNotNull(userControllerV3.findAllImages());
    }

    @Test
    void findByIdTest() {
        when(userService.findById(anyString())).thenReturn(new User());
        when(userOutputMapper.tOutputDTOV3(any(User.class))).thenReturn(userOutputDTOV3);
        assertNotNull(userControllerV3.findById("id"));
    }

    @Test
    void findFollowedUsersByEmailTest() {
        when(userService.findFollowedUsersByEmail(anyString())).thenReturn(new ArrayList<User>());
        assertNotNull(userControllerV3.findFollowedUsersByEmail("email"));
    }

    @Test
    void findFollowersByEmailTest() {
        when(userService.findFollowersByEmail(anyString())).thenReturn(new ArrayList<User>());
        assertNotNull(userControllerV3.findFollowersByEmail("email"));
    }

    @Test
    void followUserTest() {
        when(userService.followUser(anyString(), anyString())).thenReturn(true);
        assertTrue(userControllerV3.followUser("frEmail", "fdEmail"));
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
    void putTest() {
        when(userService.put(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyBoolean(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class),
                any(LocalDateTime.class))).thenReturn(new User());
        when(userOutputMapper.tOutputDTOV3(any(User.class))).thenReturn(userOutputDTOV3);
        assertNotNull(userControllerV3.put(userInputDTOV3));
    }

    @Test
    void unfollowUserTest() {
        when(userService.unfollowUser(anyString(), anyString())).thenReturn(true);
        assertTrue(userControllerV3.unfollowUser("frEmail", "fdEmail"));
    }
}
