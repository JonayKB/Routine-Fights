package es.iespuertodelacruz.routinefights.shared.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class UserDTOAuthTest {
    private static final String IMAGE = "image";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String NATIONALITY = "nationality";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final String USERNAME = "username";

    @Test
    void dtoTest(){
        UserDTOAuth userDTOAuth = new UserDTOAuth(USERNAME, EMAIL, PASSWORD, NATIONALITY, PHONE_NUMBER, IMAGE);
        assertEquals(USERNAME, userDTOAuth.username());
        assertEquals(EMAIL, userDTOAuth.email());
        assertEquals(PASSWORD, userDTOAuth.password());
        assertEquals(NATIONALITY,userDTOAuth.nationality());
        assertEquals(PHONE_NUMBER, userDTOAuth.phoneNumber());
        assertEquals(IMAGE, userDTOAuth.image());
    }

}
