package es.iespuertodelacruz.routinefights.shared.services;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.shared.exceptions.AuthException;
import es.iespuertodelacruz.routinefights.shared.security.JwtService;
import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.domain.ports.primary.IUserService;

@Service
public class AuthService {

    private final JwtService jwtService;
    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(JwtService jwtService, IUserService userService, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(String mail, String password) {
        User user = userService.findByEmailOnlyBase(mail);

        if (user != null && user.getDeletedAt() == null && passwordEncoder.matches(password, user.getPassword())) {
            if (user.getVerified()) {
                return jwtService.generateToken(user.getEmail(), user.getRole());
            } else {
                throw new AuthException("User not verified");
            }
        }
        throw new AuthException("User not found or Invalid Credentials");
    }

    public static final int MIN_PASSWORD_LENGTH = 8;

    public void rollbackRegistration(User user) {
        if (user != null && user.getId() != null) {
            userService.hardDelete(user.getId());
        }
    }

    public User register(String username, String email, String password, String nationality, String phoneNumber,
            String image) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            throw new AuthException("Password must be at least " + MIN_PASSWORD_LENGTH + " characters");
        }
        User user;
        user = userService.post(username, email, password, nationality, phoneNumber, image, "ROLE_USER", false,
                UUID.randomUUID().toString(), LocalDateTime.now(), LocalDateTime.now(), null);
        if (user == null) {
            throw new AuthException("Something happened");
        }
        user.setPassword("HIDDEN");

        return user;
    }

    public boolean verify(String email, String token) {
        User user = userService.findByEmail(email);
        if(user != null){
            if(user.getVerified()){
                return true;
            }
        if (user.getVerificationToken().equals(token)) {
            userService.put(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(),
                    user.getNationality(), user.getPhoneNumber(), user.getImage(), user.getRole(), true, null,
                    user.getCreatedAt(),
                    LocalDateTime.now(), null);
            return true;
        }
    }
        return false;
    }

}
