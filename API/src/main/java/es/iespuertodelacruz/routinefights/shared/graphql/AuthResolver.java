package es.iespuertodelacruz.routinefights.shared.graphql;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import es.iespuertodelacruz.routinefights.device_token.domain.ports.primary.IDeviceTokenService;
import es.iespuertodelacruz.routinefights.shared.dto.UserDTOAuth;
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
    public UserDTOAuth register(@Argument UserDTOAuth user) {
        User saved = authService.register(user.username(), user.email(), user.password(),
                user.nationality(), user.phoneNumber(), user.image());
        mailService.sentVerifyToken(saved.getEmail(), "Verify your email: " + saved.getUsername(),
                saved.getVerificationToken());
        return userDTOAuthMapper.toDTO(saved);
    }
}
