package es.iespuertodelacruz.routinefights.shared.soap;

import java.util.logging.Logger;


import es.iespuertodelacruz.routinefights.shared.dto.UserDTOAuth;
import es.iespuertodelacruz.routinefights.shared.mappers.UserDTOAuthMapper;
import es.iespuertodelacruz.routinefights.shared.services.AuthService;
import es.iespuertodelacruz.routinefights.shared.services.MailService;
import es.iespuertodelacruz.routinefights.shared.utils.HTMLTemplates;
import es.iespuertodelacruz.routinefights.user.domain.User;
import jakarta.jws.WebService;

@WebService(endpointInterface = "es.iespuertodelacruz.routinefights.shared.soap.AuthSOAPInterface")
public class AuthSOAPImpl implements AuthSOAPInterface {
    private final MailService mailService;
    private final AuthService authService;
    private final UserDTOAuthMapper userDTOAuthMapper;
    Logger logger = Logger.getLogger(AuthSOAPImpl.class.getName());

    public AuthSOAPImpl(MailService mailService, AuthService authService, UserDTOAuthMapper userDTOAuthMapper) {
        this.mailService = mailService;
        this.authService = authService;
        this.userDTOAuthMapper = userDTOAuthMapper;
    }

    @Override
    public String login(String email, String password) {
        return authService.login(email, password);
    }

    @Override
    public String verify(String email, String token) {
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

    @Override
    public UserDTOAuth register(UserDTOAuth userDTOAuth) {
        User user = authService.register(userDTOAuth.username(), userDTOAuth.email(),
                userDTOAuth.password(),
                userDTOAuth.nationality(), userDTOAuth.phoneNumber(), userDTOAuth.image());
        mailService.sentVerifyToken(user.getEmail(), "Verify your email: " + user.getUsername(),
                user.getVerificationToken());
        return userDTOAuthMapper.toDTO(user);
    }

}
