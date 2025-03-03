package es.iespuertodelacruz.routinefights.shared.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MailExceptionTest {
    private static final String MESSAGE = "message";

    @Test
    void mailExceptionTest() {
        MailException mailException = new MailException(MESSAGE);

        assertEquals(MESSAGE, mailException.getMessage());
    }

}
