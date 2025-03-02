package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserUpdateExceptionTest {
    private static final String MESSAGE = "message";

    @Test
    void UserUpdateException() {
        UserUpdateException userUpdateException = new UserUpdateException(MESSAGE);
        assertEquals(MESSAGE, userUpdateException.getMessage());
    }
}
