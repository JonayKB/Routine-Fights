package es.iespuertodelacruz.routinefights.shared.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AuthExceptionTest {

    private static final String MESSAGE = "message";

    @Test
    void authExceptionTest() {
        AuthException authException = new AuthException(MESSAGE);

        assertEquals(MESSAGE, authException.getMessage());
    }

}
