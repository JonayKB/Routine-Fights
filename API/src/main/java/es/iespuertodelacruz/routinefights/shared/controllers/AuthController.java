package es.iespuertodelacruz.routinefights.shared.controllers;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.iespuertodelacruz.routinefights.device_token.domain.ports.primary.IDeviceTokenService;
import es.iespuertodelacruz.routinefights.shared.dto.UserDTOAuth;
import es.iespuertodelacruz.routinefights.shared.mappers.UserDTOAuthMapper;
import es.iespuertodelacruz.routinefights.shared.services.AuthService;
import es.iespuertodelacruz.routinefights.shared.services.MailService;
import es.iespuertodelacruz.routinefights.shared.utils.HTMLTemplates;
import es.iespuertodelacruz.routinefights.user.domain.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.java.Log;

@RestController
@RequestMapping("/auth")
@CrossOrigin
@Tag(name = "AUTH", description = "Get access token")
@Log
public class AuthController {

    private final MailService mailService;
    private final AuthService authService;
    private final UserDTOAuthMapper userDTOAuthMapper;
    Logger logger = Logger.getLogger(AuthController.class.getName());
    private final IDeviceTokenService deviceTokenService;

    public AuthController(MailService mailService, AuthService authService, UserDTOAuthMapper userDTOAuthMapper, IDeviceTokenService deviceTokenService) {
        this.mailService = mailService;
        this.authService = authService;
        this.userDTOAuthMapper = userDTOAuthMapper;
        this.deviceTokenService = deviceTokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTOAuth userDTOAuth) {
        try {
            User user = authService.register(userDTOAuth.username(), userDTOAuth.email(),
                    userDTOAuth.password(),
                    userDTOAuth.nationality(), userDTOAuth.phoneNumber(), userDTOAuth.image());
            mailService.sentVerifyToken(user.getEmail(), "Verify your email: " + user.getUsername(),
                    user.getVerificationToken());
            return ResponseEntity.ok(userDTOAuthMapper.toDTO(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password,@RequestParam(required = false) String deviceToken, @RequestParam(required = false) String language) {
        try {
            log.warning("Login attempt for email: " + email);
            log.info("Token: " + deviceToken + " Language: " + language);
            
            String token = authService.login(email, password);
            if (deviceToken != null && language != null) {
                deviceTokenService.save(email, deviceToken, language);
            }
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
            logger.warning("Verify /api/verify ERROR: " + e.getMessage());
            return HTMLTemplates.ERROR.formatted(e.getMessage());
        }
        if (isVerified) {
            logger.info("Verify /api/verify OK");
            return HTMLTemplates.VERIFIED;
        }

        return HTMLTemplates.BAD_REQUEST;
    }

    


}
