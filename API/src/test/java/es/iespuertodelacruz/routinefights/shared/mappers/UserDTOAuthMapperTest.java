package es.iespuertodelacruz.routinefights.shared.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import es.iespuertodelacruz.routinefights.shared.dto.UserDTOAuth;
import es.iespuertodelacruz.routinefights.user.domain.User;

@SpringBootTest
class UserDTOAuthMapperTest {
    private static final String VERIFICATION_TOKEN = "verificationToken";
    private static final boolean VERIFIED = true;
    private static final String ROLE = "role";
    private static final String IMAGE = "image";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String NATIONALITY = "nationality";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final String USERNAME = "username";
    private static final String ID = "id";
    @Autowired
    private UserDTOAuthMapper userDTOAuthMapper;

    @Test
    void toUserDTOAuthTestOK() {
        User user = new User();
        user.setId(ID);
        user.setUsername(USERNAME);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setNationality(NATIONALITY);
        user.setPhoneNumber(PHONE_NUMBER);
        user.setImage(IMAGE);
        user.setRole(ROLE);
        user.setVerified(VERIFIED);
        user.setVerificationToken(VERIFICATION_TOKEN);
        UserDTOAuth userDTOAuth = userDTOAuthMapper.toDTO(user);
        assertEquals(USERNAME, userDTOAuth.username());
        assertEquals(EMAIL, userDTOAuth.email());
        assertEquals(PASSWORD, userDTOAuth.password());
        assertEquals(NATIONALITY, userDTOAuth.nationality());
        assertEquals(PHONE_NUMBER, userDTOAuth.phoneNumber());
        assertEquals(IMAGE, userDTOAuth.image());
    }

    @Test
    void toUserDTOAuthTestUserNull() {
        User user = null;
        assertNull(userDTOAuthMapper.toDTO(user));
    }
}
