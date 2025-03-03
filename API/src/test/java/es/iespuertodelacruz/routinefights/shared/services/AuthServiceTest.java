package es.iespuertodelacruz.routinefights.shared.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import es.iespuertodelacruz.routinefights.shared.exceptions.AuthException;
import es.iespuertodelacruz.routinefights.shared.security.JwtService;
import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.domain.ports.primary.IUserService;

class AuthServiceTest {

    private static final boolean VERIFIED = true;

    private static final String ROLE_USER = "ROLE_USER";

    private static final String GENERATED_TOKEN = "generatedToken";

    private static final String HASHED_PASSWORD = "hashedPassword";

    private static final String PASSWORD = "password";

    private static final String EMAIL = "test@example.com";

    @Mock
    private JwtService jwtService;

    @Mock
    private IUserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthService(jwtService, userService, passwordEncoder);
    }

    @Test
    void testLoginSuccess() {
        String email = EMAIL;
        String rawPassword = PASSWORD;
        String hashedPassword = HASHED_PASSWORD;
        String generatedToken = GENERATED_TOKEN;

        User user = new User();
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setVerified(VERIFIED);
        user.setRole(ROLE_USER);

        when(userService.findByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(true);
        when(jwtService.generateToken(email, ROLE_USER)).thenReturn(generatedToken);

        String token = authService.login(email, rawPassword);

        assertEquals(generatedToken, token);
    }

    @Test
    void testLoginUserNotVerified() {
        String email = EMAIL;
        String rawPassword = PASSWORD;
        String hashedPassword = HASHED_PASSWORD;

        User user = new User();
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setVerified(false);
        user.setRole(ROLE_USER);

        when(userService.findByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(true);

        AuthException exception = assertThrows(AuthException.class, () -> {
            authService.login(email, rawPassword);
        });
        assertEquals("User not verified", exception.getMessage());
    }

    @Test
    void testLoginInvalidCredentials_UserNotFound() {
        String email = EMAIL;
        String rawPassword = PASSWORD;

        when(userService.findByEmail(email)).thenReturn(null);

        AuthException exception = assertThrows(AuthException.class, () -> {
            authService.login(email, rawPassword);
        });
        assertEquals("User not found or Invalid Credentials", exception.getMessage());
    }

    @Test
    void testLoginInvalidCredentials_PasswordMismatch() {
        String email = EMAIL;
        String rawPassword = PASSWORD;
        String hashedPassword = HASHED_PASSWORD;

        User user = new User();
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setVerified(true);
        user.setRole(ROLE_USER);

        when(userService.findByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(false);

        AuthException exception = assertThrows(AuthException.class, () -> {
            authService.login(email, rawPassword);
        });
        assertEquals("User not found or Invalid Credentials", exception.getMessage());
    }

    @Test
    void testRegisterSuccess() {
        String username = "user1";
        String email = "user1@example.com";
        String password = PASSWORD;
        String nationality = "Country";
        String phoneNumber = "123456";
        String image = "image.png";

        User createdUser = new User();
        createdUser.setId("1");
        createdUser.setUsername(username);
        createdUser.setEmail(email);
        createdUser.setPassword(password);
        createdUser.setNationality(nationality);
        createdUser.setPhoneNumber(phoneNumber);
        createdUser.setImage(image);
        createdUser.setRole(ROLE_USER);
        createdUser.setVerified(false);
        createdUser.setVerificationToken(UUID.randomUUID().toString());

        when(userService.post(eq(username), eq(email), eq(password), eq(nationality), eq(phoneNumber), eq(image),
                eq(ROLE_USER), eq(false), anyString(), any(), any(), any())).thenReturn(createdUser);

        User result = authService.register(username, email, password, nationality, phoneNumber, image);

        assertEquals("HIDDEN", result.getPassword());
        assertEquals(username, result.getUsername());
        assertEquals(email, result.getEmail());
    }

    @Test
    void testRegisterFailure() {
        String username = "user1";
        String email = "user1@example.com";
        String password = PASSWORD;
        String nationality = "Country";
        String phoneNumber = "123456";
        String image = "image.png";

        when(userService.post(eq(username), eq(email), eq(password), eq(nationality), eq(phoneNumber), eq(image),
                eq(ROLE_USER), eq(false), anyString(), any(),  any(),  any())).thenReturn(null);

        AuthException exception = assertThrows(AuthException.class, () -> {
            authService.register(username, email, password, nationality, phoneNumber, image);
        });
        assertEquals("Something happened", exception.getMessage());
    }

    @Test
    void testVerifySuccess() {
        String email = EMAIL;
        String token = "token123";

        User user = new User();
        user.setId("1");
        user.setEmail(email);
        user.setVerificationToken(token);
        user.setUsername("user1");
        user.setPassword(HASHED_PASSWORD);
        user.setNationality("Country");
        user.setPhoneNumber("123456");
        user.setImage("image.png");
        user.setRole(ROLE_USER);
        user.setVerified(false);

        when(userService.findByEmail(email)).thenReturn(user);

        boolean verified = authService.verify(email, token);

        assertTrue(verified);
        verify(userService, times(1)).put( eq(user.getId()),  eq(user.getUsername()),  eq(user.getEmail()),  eq(user.getPassword()),
        eq(user.getNationality()),  eq(user.getPhoneNumber()),  eq(user.getImage()), eq(user.getRole()), eq(true), any(),  any(),  any(),  any());
    }

    @Test
    void testVerifyFailure_UserNotFound() {
        String email = EMAIL;
        String token = "token123";

        when(userService.findByEmail(email)).thenReturn(null);

        boolean verified = authService.verify(email, token);
        assertFalse(verified);
    }

    @Test
    void testVerifyFailure_TokenMismatch() {
        String email = EMAIL;
        String token = "token123";

        User user = new User();
        user.setId("1");
        user.setEmail(email);
        user.setVerificationToken("differentToken");

        when(userService.findByEmail(email)).thenReturn(user);

        boolean verified = authService.verify(email, token);
        assertFalse(verified);
    }
}
