package es.iespuertodelacruz.routinefights.shared.graphql;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import es.iespuertodelacruz.routinefights.device_token.domain.ports.primary.IDeviceTokenService;
import es.iespuertodelacruz.routinefights.shared.dto.UserDTOAuth;
import es.iespuertodelacruz.routinefights.shared.exceptions.MailException;
import es.iespuertodelacruz.routinefights.shared.mappers.UserDTOAuthMapper;
import es.iespuertodelacruz.routinefights.shared.services.AuthService;
import es.iespuertodelacruz.routinefights.shared.services.MailService;
import es.iespuertodelacruz.routinefights.user.domain.User;
import lombok.extern.slf4j.Slf4j;

/**
 * Public GraphQL mutations for authentication (replaces POST /auth/login and
 * POST /auth/register).
 */
@Controller
@Slf4j
public class AuthResolver {

    private final MailService mailService;
    private final AuthService authService;
    private final UserDTOAuthMapper userDTOAuthMapper;
    private final IDeviceTokenService deviceTokenService;

    public AuthResolver(MailService mailService, AuthService authService, UserDTOAuthMapper userDTOAuthMapper,
            IDeviceTokenService deviceTokenService) {
        this.mailService = mailService;
        this.authService = authService;
        this.userDTOAuthMapper = userDTOAuthMapper;
        this.deviceTokenService = deviceTokenService;
    }

    @MutationMapping("login")
    public String login(@Argument String email, @Argument String password, @Argument String deviceToken,
            @Argument String language) {
        String token = authService.login(email, password);
        if (deviceToken != null && language != null) {
            deviceTokenService.save(email, deviceToken, language);
        }
        return token;
    }

    @MutationMapping("register")
    public UserDTOAuth register(@Argument UserDTOAuth user, @Argument String language) {
        User saved = authService.register(user.username(), user.email(), user.password(),
                user.nationality(), user.phoneNumber(), user.image());
        try {
            mailService.sendVerificationEmail(saved.getEmail(), saved.getUsername(),
                    saved.getVerificationToken(), language, false);
        } catch (RuntimeException e) {
            // Without the e-mail the account could never be verified: undo it so the
            // user can simply try again instead of hitting "Email already exists".
            log.warn("Verification e-mail failed for {}, rolling back registration", saved.getEmail(), e);
            authService.rollbackRegistration(saved);
            throw new MailException("We could not send the verification e-mail. Please try again later.");
        }
        return userDTOAuthMapper.toDTO(saved);
    }
}
