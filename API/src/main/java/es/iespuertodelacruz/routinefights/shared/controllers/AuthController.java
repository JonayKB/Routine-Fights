package es.iespuertodelacruz.routinefights.shared.controllers;

import java.util.logging.Logger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.iespuertodelacruz.routinefights.shared.services.AuthService;
import es.iespuertodelacruz.routinefights.shared.utils.HTMLTemplates;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Only the e-mail verification link stays as plain HTTP: it is opened from the
 * user's mail client and must answer a GET with an HTML page. Login and
 * register live in GraphQL (see AuthResolver).
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "AUTH", description = "E-mail verification link")
public class AuthController {

    private final AuthService authService;
    Logger logger = Logger.getLogger(AuthController.class.getName());

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/verify/{email}/{token}")
    public String verify(@PathVariable String email, @PathVariable String token) {
        if (email == null || token == null) {
            return HTMLTemplates.NEED_EMAIL_TOKEN;
        }

        boolean isVerified;
        try {
            isVerified = authService.verify(email, token);
        } catch (Exception e) {
            logger.warning("Verify /auth/verify ERROR: " + e.getMessage());
            return HTMLTemplates.ERROR.formatted(e.getMessage());
        }
        if (isVerified) {
            logger.info("Verify /auth/verify OK");
            return HTMLTemplates.VERIFIED;
        }

        return HTMLTemplates.BAD_REQUEST;
    }
}
