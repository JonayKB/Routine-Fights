package es.iespuertodelacruz.routinefights.shared.controllers;

import java.util.logging.Logger;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.iespuertodelacruz.routinefights.shared.services.AuthService;
import es.iespuertodelacruz.routinefights.shared.utils.HTMLTemplates;
import es.iespuertodelacruz.routinefights.shared.utils.HTMLTemplates.VerifyResult;
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

    /**
     * @param lang           language added to the link when the e-mail was sent
     * @param acceptLanguage browser language, used when the link has no lang
     */
    @GetMapping(value = "/verify/{email}/{token}", produces = MediaType.TEXT_HTML_VALUE + ";charset=UTF-8")
    public String verify(@PathVariable String email, @PathVariable String token,
            @RequestParam(required = false) String lang,
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, required = false) String acceptLanguage) {
        String language = lang != null && !lang.isBlank() ? lang : acceptLanguage;
        if (email == null || token == null) {
            return HTMLTemplates.verifyPage(language, VerifyResult.MISSING_DATA);
        }

        boolean isVerified;
        try {
            isVerified = authService.verify(email, token);
        } catch (Exception e) {
            logger.warning("Verify /auth/verify ERROR: " + e.getMessage());
            return HTMLTemplates.verifyPage(language, VerifyResult.ERROR);
        }
        if (isVerified) {
            logger.info("Verify /auth/verify OK");
            return HTMLTemplates.verifyPage(language, VerifyResult.VERIFIED);
        }

        return HTMLTemplates.verifyPage(language, VerifyResult.BAD_REQUEST);
    }
}
