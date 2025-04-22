package es.iespuertodelacruz.routinefights.user.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.domain.ports.secondary.IUserRepository;

@SpringBootTest
class UserServiceTest {
    private static final String IMAGE = "image";

    private static final String PHONE_NUMBER = "phoneNumber";

    private static final String NATIONALITY = "nationality";

    private static final String PASSWORD = "password";

    private static final String USERNAME = "username";

    private static final String ID = "1";

    private static final String EMAIL = "email";

    private UserService userService;

    @Mock
    private IUserRepository userRepository;

    @BeforeEach
    void setUp() {
        userService = new UserService();
        userService.setUserRepository(userRepository);
    }

    @Test
    void getUserRepositoryTest() {
        assertEquals(userRepository, userService.getUserRepository());
    }

    @Test
    void deleteTest() {
        when(userRepository.delete(anyString())).thenReturn(true);
        Boolean deleted = userService.delete(ID);
        assertNotNull(deleted);
    }

    @Test
    void existsByEmailTest() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        assertNotNull(userService.existsByEmail(EMAIL));
    }

    @Test
    void findAllTest() {
        when(userRepository.findAll()).thenReturn(new ArrayList<User>());
        assertNotNull(userService.findAll());
    }

    @Test
    void findAllImagesTest() {
        when(userRepository.findAllImages()).thenReturn(new ArrayList<String>());
        assertNotNull(userService.findAllImages());
    }

    @Test
    void findByEmailTest() {
        when(userRepository.findByEmail(anyString())).thenReturn(new User());
        assertNotNull(userService.findByEmail(EMAIL));
    }

    @Test
    void findByIdTest() {
        when(userRepository.findById(anyString())).thenReturn(new User());
        assertNotNull(userService.findById(ID));
    }

    @Test
    void findFollowedUsersByEmailTest() {
        when(userRepository.findFollowedUsersByEmail(anyString())).thenReturn(new ArrayList<User>());
        assertNotNull(userService.findFollowedUsersByEmail(EMAIL));
    }

    @Test
    void findFollowersByEmailTest() {
        when(userRepository.findFollowersByEmail(anyString())).thenReturn(new ArrayList<User>());
        assertNotNull(userService.findFollowersByEmail(EMAIL));
    }

    @Test
    void followUserTest() {
        when(userRepository.followUser(anyString(), anyString())).thenReturn(true);
        assertTrue(userService.followUser("frEmail", "fdEmail"));
    }

    @Test
    void postTest() {
        when(userRepository.post(any(User.class))).thenReturn(new User());
        User user = userService.post(USERNAME, EMAIL, PASSWORD, NATIONALITY, PHONE_NUMBER, IMAGE, "role",
                true, "verificationToken", LocalDateTime.now(), LocalDateTime.now(), null);
        assertNotNull(user);
    }

    @Test
    void putTest() {
        when(userRepository.put(any(User.class))).thenReturn(new User());
        User user = userService.put(ID, USERNAME, EMAIL, PASSWORD, NATIONALITY, PHONE_NUMBER, IMAGE, "role",
                true, "verificationToken", LocalDateTime.now(), LocalDateTime.now(), null);
        assertNotNull(user);
    }

    @Test
    void unfollowUserTest() {
        when(userRepository.unfollowUser(anyString(), anyString())).thenReturn(true);
        assertTrue(userService.unfollowUser("frEmail", "fdEmail"));
    }

    @Test
    void restoreTest() {
        when(userRepository.restore(anyString())).thenReturn(true);
        assertTrue(userService.restore(ID));
    }

    @Test
    void updateTest() {
        when(userRepository.update(any(User.class))).thenReturn(new User());
        User user = userService.update(ID, USERNAME, EMAIL, PASSWORD, NATIONALITY, PHONE_NUMBER, IMAGE);
        assertNotNull(user);
    }

    @Test
    void softDeleteTest() {
        when(userRepository.softDelete(anyString())).thenReturn(true);
        assertTrue(userService.softDelete(ID));
    }

    @Test
    void findByUsernameTest() {
        when(userRepository.findByUsername(anyString())).thenReturn(new ArrayList<User>());
        assertNotNull(userService.findByUsername(USERNAME));
    }

    @Test
    void findByEmailOnlyBaseTest() {
        when(userRepository.findByEmailOnlyBase(anyString())).thenReturn(new User());
        assertNotNull(userService.findByEmailOnlyBase(EMAIL));
    }

}
