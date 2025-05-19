package es.iespuertodelacruz.routinefights.shared.soap;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import es.iespuertodelacruz.routinefights.shared.mappers.UserDTOAuthMapper;
import es.iespuertodelacruz.routinefights.shared.services.AuthService;
import es.iespuertodelacruz.routinefights.shared.services.MailService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import es.iespuertodelacruz.routinefights.shared.dto.UserDTOAuth;
import es.iespuertodelacruz.routinefights.shared.utils.HTMLTemplates;
import es.iespuertodelacruz.routinefights.user.domain.User;

class AuthSOAPImplTest {
    private AuthSOAPImpl authSOAPImpl;

    @Mock
    private MailService mailService;
    @Mock
    private AuthService authService;
    @Mock
    private UserDTOAuthMapper userDTOAuthMapper;

    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "password123";
    private static final String VALID_TOKEN = "validToken";
    private static final String INVALID_TOKEN = "invalidToken";
    private static final String MOCK_TOKEN = "mockToken";
    private static final String VERIFICATION_ERROR = "Verification error";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authSOAPImpl = new AuthSOAPImpl(mailService, authService, userDTOAuthMapper);
    }

    @Test
    void testLogin() {
        when(authService.login(EMAIL, PASSWORD)).thenReturn(MOCK_TOKEN);

        String result = authSOAPImpl.login(EMAIL, PASSWORD);

        assertEquals(MOCK_TOKEN, result);
        verify(authService, times(1)).login(EMAIL, PASSWORD);
    }

    @Test
    void testVerifySuccess() {
        when(authService.verify(EMAIL, VALID_TOKEN)).thenReturn(true);

        String result = authSOAPImpl.verify(EMAIL, VALID_TOKEN);

        assertEquals(HTMLTemplates.VERIFIED, result);
        verify(authService, times(1)).verify(EMAIL, VALID_TOKEN);
    }

    @Test
    void testVerifyFailure() {
        when(authService.verify(EMAIL, INVALID_TOKEN)).thenReturn(false);

        String result = authSOAPImpl.verify(EMAIL, INVALID_TOKEN);

        assertEquals(HTMLTemplates.BAD_REQUEST, result);
        verify(authService, times(1)).verify(EMAIL, INVALID_TOKEN);
    }

    @Test
    void testVerifyWithNullInputs() {
        String result = authSOAPImpl.verify(null, null);

        assertEquals(HTMLTemplates.NEED_EMAIL_TOKEN, result);
        verify(authService, never()).verify(anyString(), anyString());
    }

    @Test
    void testVerifyWithException() {
        when(authService.verify(EMAIL, VALID_TOKEN)).thenThrow(new RuntimeException(VERIFICATION_ERROR));

        String result = authSOAPImpl.verify(EMAIL, VALID_TOKEN);

        assertEquals(HTMLTemplates.ERROR.formatted(VERIFICATION_ERROR), result);
        verify(authService, times(1)).verify(EMAIL, VALID_TOKEN);
    }

    @Test
    void testRegister() {
        UserDTOAuth userDTOAuth = new UserDTOAuth("username", EMAIL, PASSWORD, "nationality", "123456789", "image");
        User user = new User();
        user.setEmail(userDTOAuth.email());
        user.setUsername(userDTOAuth.username());
        user.setVerificationToken(MOCK_TOKEN);

        when(authService.register(userDTOAuth.username(), userDTOAuth.email(), userDTOAuth.password(),
                userDTOAuth.nationality(), userDTOAuth.phoneNumber(), userDTOAuth.image())).thenReturn(user);
        when(userDTOAuthMapper.toDTO(user)).thenReturn(userDTOAuth);

        UserDTOAuth result = authSOAPImpl.register(userDTOAuth);

        assertEquals(userDTOAuth, result);
        verify(authService, times(1)).register(userDTOAuth.username(), userDTOAuth.email(), userDTOAuth.password(),
                userDTOAuth.nationality(), userDTOAuth.phoneNumber(), userDTOAuth.image());
        verify(mailService, times(1)).sentVerifyToken(user.getEmail(), "Verify your email: " + user.getUsername(),
                user.getVerificationToken());
        verify(userDTOAuthMapper, times(1)).toDTO(user);
    }
}
