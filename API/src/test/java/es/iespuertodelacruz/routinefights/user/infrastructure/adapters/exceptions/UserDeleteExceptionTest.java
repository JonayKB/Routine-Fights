package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserDeleteExceptionTest {
    private static final String MESSAGE = "message";

    @Test
    void userDeleteExceptionTest() {
        UserDeleteException userDeleteException = new UserDeleteException(MESSAGE);
        assertEquals(MESSAGE, userDeleteException.getMessage());
    }
}
