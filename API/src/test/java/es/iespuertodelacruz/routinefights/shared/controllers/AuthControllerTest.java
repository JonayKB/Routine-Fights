package es.iespuertodelacruz.routinefights.shared.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import es.iespuertodelacruz.routinefights.shared.services.AuthService;
import es.iespuertodelacruz.routinefights.shared.utils.HTMLTemplates;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = AuthController.class)
class AuthControllerTest {
    private static final String EMAIL = "email";
    private static final String TOKEN = "token";

    @MockitoBean
    private AuthService authService;

    @Autowired
    private AuthController authController;

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
    void verifyTestNotVerified() {
        when(authService.verify(EMAIL, TOKEN)).thenReturn(false);
        String response = authController.verify(EMAIL, TOKEN);
        assertEquals(HTMLTemplates.BAD_REQUEST, response);
    }
}
