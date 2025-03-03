package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserSaveExceptionTest {
    private static final String MESSAGE = "message";

    @Test
    void UserSaveException() {
        UserSaveException userSaveException = new UserSaveException(MESSAGE);
        assertEquals(MESSAGE, userSaveException.getMessage());
    }
}
