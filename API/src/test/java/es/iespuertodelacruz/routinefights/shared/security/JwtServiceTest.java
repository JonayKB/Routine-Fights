package es.iespuertodelacruz.routinefights.shared.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtServiceTest {

    private static final String ROLE = "ROLE_ADMIN";
    private static final String MAIL = "jonaykb@gmail.com";
    JwtService jwtService;

    @BeforeEach
    void setUp() {
        if (jwtService == null) {
            jwtService = new JwtService();
        }
    }
    @Test
    void testJwtToken(){
        String token = jwtService.generateToken(MAIL, ROLE);
        assertEquals(MAIL, jwtService.validateAndGetClaims(token).get("mail"));
        assertEquals(ROLE, jwtService.validateAndGetClaims(token).get("role"));

    }

}
