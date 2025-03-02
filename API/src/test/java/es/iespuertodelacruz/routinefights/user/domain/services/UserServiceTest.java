package es.iespuertodelacruz.routinefights.user.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.domain.ports.secondary.IUserRepository;

@SpringBootTest
class UserServiceTest {
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
        Boolean deleted = userService.delete("1");
        assertNotNull(deleted);
    }

    @Test
    void existsByEmailTest() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        Boolean exists = userService.existsByEmail("email");
        assertNotNull(exists);
    }

    @Test
    void findAllTest() {
        when(userRepository.findAll()).thenReturn(new ArrayList<User>());
        List<User> users = userService.findAll();
        assertNotNull(users);
    }

    @Test
    void findAllImagesTest() {
        when(userRepository.findAllImages()).thenReturn(new ArrayList<String>());
        List<String> images = userService.findAllImages();
        assertNotNull(images);
    }

    @Test
    void findByEmailTest() {
        when(userRepository.findByEmail(anyString())).thenReturn(new User());
        User user = userService.findByEmail("email");
        assertNotNull(user);
    }

    @Test
    void findByIdTest() {
        when(userRepository.findById(anyString())).thenReturn(new User());
        User user = userService.findById("1");
        assertNotNull(user);
    }

    @Test
    void findFollowedUsersByEmailTest() {
        when(userRepository.findFollowedUsersByEmail(anyString())).thenReturn(new ArrayList<User>());
        List<User> users = userService.findFollowedUsersByEmail("email");
        assertNotNull(users);
    }

    @Test
    void findFollowersByEmailTest() {
        when(userRepository.findFollowersByEmail(anyString())).thenReturn(new ArrayList<User>());
        List<User> users = userService.findFollowersByEmail("email");
        assertNotNull(users);
    }

    @Test
    void followUserTest() {
        when(userRepository.followUser(anyString(), anyString())).thenReturn(true);
        Boolean followed = userService.followUser("frEmail", "fdEmail");
        assertNotNull(followed);
    }

    @Test
    void postTest() {
        when(userRepository.post(any())).thenReturn(new User());
        User user = userService.post("username", "email", "password", "nationality", "phoneNumber", "image", "role",
                true, "verificationToken", LocalDateTime.now(), LocalDateTime.now(), null);
        assertNotNull(user);
    }

    @Test
    void putTest() {
        when(userRepository.put(any())).thenReturn(new User());
        User user = userService.put("1", "username", "email", "password", "nationality", "phoneNumber", "image", "role",
                true, "verificationToken", LocalDateTime.now(), LocalDateTime.now(), null);
        assertNotNull(user);
    }

    @Test
    void unfollowUserTest() {
        when(userRepository.unfollowUser(anyString(), anyString())).thenReturn(true);
        Boolean unfollowed = userService.unfollowUser("frEmail", "fdEmail");
        assertNotNull(unfollowed);
    }
}
