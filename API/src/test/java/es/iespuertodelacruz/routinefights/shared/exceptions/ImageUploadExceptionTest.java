package es.iespuertodelacruz.routinefights.shared.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ImageUploadExceptionTest {

    private static final String MESSAGE = "message";

    @Test
    void imageUploadExceptionTest() {
        ImageUploadException imageUploadException = new ImageUploadException(MESSAGE);

        assertEquals(MESSAGE, imageUploadException.getMessage());
    }
}
