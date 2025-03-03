package es.iespuertodelacruz.routinefights.shared.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import es.iespuertodelacruz.routinefights.shared.dto.UserDTOAuth;
import es.iespuertodelacruz.routinefights.shared.exceptions.MailException;
import es.iespuertodelacruz.routinefights.shared.mappers.UserDTOAuthMapper;
import es.iespuertodelacruz.routinefights.shared.services.AuthService;
import es.iespuertodelacruz.routinefights.shared.services.MailService;
import es.iespuertodelacruz.routinefights.shared.utils.HTMLTemplates;
import es.iespuertodelacruz.routinefights.user.domain.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = AuthController.class)
class AuthControllerTest {
    private static final String VERIFICATION_TOKEN = "verificationToken";
    private static final String ROLE = "ROLE";
    private static final boolean VERIFIED = false;
    private static final String IMAGE = "image";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String NATIONALITY = "nationality";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final String USERNAME = "username";
    private static final String TOKEN = "token";

    private static final User USER = new User(USERNAME, EMAIL, PASSWORD, NATIONALITY, PHONE_NUMBER, IMAGE, ROLE,
            VERIFIED,
            VERIFICATION_TOKEN, null, null, null);
    @MockitoBean
    private MailService mailService;
    @MockitoBean
    private AuthService authService;
    @MockitoBean
    private UserDTOAuthMapper userDTOAuthMapper;

    @Autowired
    private AuthController authController;

    @Test
    void registerTestOK() {
        UserDTOAuth userDTOAuth = new UserDTOAuth(USERNAME, EMAIL, PASSWORD, NATIONALITY, PHONE_NUMBER, IMAGE);
        when(authService.register(userDTOAuth.username(), userDTOAuth.email(),
                userDTOAuth.password(),
                userDTOAuth.nationality(), userDTOAuth.phoneNumber(), userDTOAuth.image()))
                .thenReturn(USER);
        when(userDTOAuthMapper.toDTO(USER)).thenReturn(userDTOAuth);
        ResponseEntity<?> response = authController.register(userDTOAuth);
        assertEquals(HttpStatus.valueOf(200), response.getStatusCode());
        assertTrue(response.getBody() instanceof UserDTOAuth);
    }

    @Test
    void registerTestException() {
        UserDTOAuth userDTOAuth = new UserDTOAuth(USERNAME, EMAIL, PASSWORD, NATIONALITY, PHONE_NUMBER, IMAGE);
        when(authService.register(userDTOAuth.username(), userDTOAuth.email(),
                userDTOAuth.password(),
                userDTOAuth.nationality(), userDTOAuth.phoneNumber(), userDTOAuth.image()))
                .thenReturn(USER);
        doThrow(new MailException("Error sending email")).when(mailService).sentVerifyToken(USER.getEmail(),
                "Verify your email: " + USER.getUsername(),
                USER.getVerificationToken());
        ResponseEntity<?> response = authController.register(userDTOAuth);
        assertEquals(HttpStatus.valueOf(400), response.getStatusCode());
        assertEquals("Error sending email", response.getBody());
    }

    @Test
    void loginTestOK() {
        when(authService.login(EMAIL, PASSWORD)).thenReturn(TOKEN);
        ResponseEntity<String> response = authController.login(EMAIL, PASSWORD);
        assertEquals(HttpStatus.valueOf(200), response.getStatusCode());
        assertEquals(TOKEN, response.getBody());
    }

    @Test
    void loginTestException() {
        when(authService.login(EMAIL, PASSWORD)).thenThrow(new RuntimeException("Error"));
        ResponseEntity<String> response = authController.login(EMAIL, PASSWORD);
        assertEquals(HttpStatus.valueOf(400), response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void verifyTestOK() {
        when(authService.verify(EMAIL, TOKEN)).thenReturn(true);
        String response = authController.verify(EMAIL, TOKEN);
        assertEquals(HTMLTemplates.VERIFIED, response);
    }

    @Test
    void verifyTestException() {
        when(authService.verify(EMAIL, TOKEN)).thenThrow(new RuntimeException("Error"));
        String response = authController.verify(EMAIL, TOKEN);
        assertEquals(HTMLTemplates.ERROR.formatted("Error"), response);
    }

    @Test
    void verifyTestMissingArguments() {
        String response = authController.verify(null, null);
        assertEquals(HTMLTemplates.NEED_EMAIL_TOKEN, response);
        response = authController.verify(EMAIL, null);
        assertEquals(HTMLTemplates.NEED_EMAIL_TOKEN, response);

    }
    @Test
    void verifyTestNotVerified(){
        when(authService.verify(EMAIL, TOKEN)).thenReturn(false);
        String response = authController.verify(EMAIL, TOKEN);
        assertEquals(HTMLTemplates.BAD_REQUEST, response);
    }
}
