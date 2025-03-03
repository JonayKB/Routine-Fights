package es.iespuertodelacruz.routinefights.shared.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ImageNotFoundExceptionTest {

    private static final String MESSAGE = "message";

    @Test
    void imageNotFoundExceptionTest() {
        ImageNotFoundException imageNotFoundException = new ImageNotFoundException(MESSAGE);

        assertEquals(MESSAGE, imageNotFoundException.getMessage());
    }
}
