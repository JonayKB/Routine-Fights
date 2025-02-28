package es.iespuertodelacruz.routinefights.shared.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.shared.dto.UserDTOAuth;
import es.iespuertodelacruz.routinefights.shared.mappers.UserDTOAuthMapper;
import es.iespuertodelacruz.routinefights.shared.security.JwtService;
import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.domain.ports.primary.IUserService;
import es.iespuertodelacruz.routinefights.user.domain.services.UserService;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v3.dtos.UserOutputDTOV3;

@Service
public class AuthService {
    private final JwtService jwtService;
    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(JwtService jwtService, UserService userService, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;

    }

    public String login(String mail, String password) {
        User user = userService.findByEmail(mail);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            if (user.getVerified()) {
                return jwtService.generateToken(user.getEmail(), user.getRole());
            } else {
                throw new RuntimeException("User not verified");
            }
        }
        throw new RuntimeException("User not found or Invalid Credentials");
    }

    public User register(String username, String email, String password, String nationality, String phoneNumber,
            String image) {
        User user;
        // if (userService.existsByEmail(email)) {
        // throw new RuntimeException("Email already exists");
        // }
        user = userService.post(username, email, password, nationality, phoneNumber, image, "ROLE_USER", false,
                UUID.randomUUID().toString(), LocalDateTime.now(), LocalDateTime.now(), null);
        if (user == null) {
            throw new RuntimeException("Something happened");
        }
        user.setPassword("HIDDEN");

        return user;
    }

    public boolean verify(String email, String token) {
        User user = userService.findByEmail(email);
        if (user != null && user.getVerificationToken().equals(token)) {
            userService.put(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(),
                    user.getNationality(), user.getPhoneNumber(), user.getImage(), user.getRole(), true, null,
                    user.getCreatedAt(),
                    LocalDateTime.now(), null);
            return true;
        }
        return false;
    }

}
