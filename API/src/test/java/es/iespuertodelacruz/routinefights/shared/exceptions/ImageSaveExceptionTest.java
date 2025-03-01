package es.iespuertodelacruz.routinefights.shared.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ImageSaveExceptionTest {

    private static final String MESSAGE = "message";

    @Test
    void imageSaveExceptionTest() {
        ImageSaveException imageSaveException = new ImageSaveException(MESSAGE);

        assertEquals(MESSAGE, imageSaveException.getMessage());
    }
}
