package es.iespuertodelacruz.routinefights.shared.graphql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.iespuertodelacruz.routinefights.device_token.domain.ports.primary.IDeviceTokenService;
import es.iespuertodelacruz.routinefights.shared.dto.UserDTOAuth;
import es.iespuertodelacruz.routinefights.shared.exceptions.MailException;
import es.iespuertodelacruz.routinefights.shared.mappers.UserDTOAuthMapper;
import es.iespuertodelacruz.routinefights.shared.services.AuthService;
import es.iespuertodelacruz.routinefights.shared.services.MailService;
import es.iespuertodelacruz.routinefights.user.domain.User;

class AuthResolverTest {
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String TOKEN = "token";
    private static final User USER = new User("username", EMAIL, PASSWORD, "nationality", "phone", "image", "ROLE",
            false, "verificationToken", null, null, null);
    private static final UserDTOAuth INPUT = new UserDTOAuth("username", EMAIL, PASSWORD, "nationality", "phone",
            "image");

    private MailService mailService;
    private AuthService authService;
    private UserDTOAuthMapper mapper;
    private IDeviceTokenService deviceTokenService;
    private AuthResolver resolver;

    @BeforeEach
    void setUp() {
        mailService = mock(MailService.class);
        authService = mock(AuthService.class);
        mapper = mock(UserDTOAuthMapper.class);
        deviceTokenService = mock(IDeviceTokenService.class);
        resolver = new AuthResolver(mailService, authService, mapper, deviceTokenService);
    }

    @Test
    void loginSavesDeviceToken() {
        when(authService.login(EMAIL, PASSWORD)).thenReturn(TOKEN);
        assertEquals(TOKEN, resolver.login(EMAIL, PASSWORD, "device", "es"));
        verify(deviceTokenService).save(EMAIL, "device", "es");
    }

    @Test
    void loginWithoutDeviceToken() {
        when(authService.login(EMAIL, PASSWORD)).thenReturn(TOKEN);
        assertEquals(TOKEN, resolver.login(EMAIL, PASSWORD, null, null));
        verify(deviceTokenService, never()).save(anyString(), anyString(), anyString());
    }

    @Test
    void loginPropagatesError() {
        when(authService.login(EMAIL, PASSWORD)).thenThrow(new RuntimeException("Error"));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> resolver.login(EMAIL, PASSWORD, null, null));
        assertEquals("Error", ex.getMessage());
    }

    @Test
    void registerSendsVerificationMail() {
        when(authService.register("username", EMAIL, PASSWORD, "nationality", "phone", "image")).thenReturn(USER);
        when(mapper.toDTO(USER)).thenReturn(INPUT);
        assertSame(INPUT, resolver.register(INPUT));
        verify(mailService).sentVerifyToken(EMAIL, "Verify your email: username", "verificationToken");
    }

    @Test
    void registerPropagatesMailError() {
        when(authService.register("username", EMAIL, PASSWORD, "nationality", "phone", "image")).thenReturn(USER);
        doThrow(new MailException("Error sending email")).when(mailService).sentVerifyToken(anyString(), anyString(),
                anyString());
        assertThrows(MailException.class, () -> resolver.register(INPUT));
    }
}
