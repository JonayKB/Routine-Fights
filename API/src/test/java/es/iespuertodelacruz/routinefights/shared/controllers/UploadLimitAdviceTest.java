package es.iespuertodelacruz.routinefights.shared.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

class UploadLimitAdviceTest {

    @Test
    void answers413() {
        var response = new UploadLimitAdvice().tooLarge(new MaxUploadSizeExceededException(10));
        assertEquals(HttpStatus.PAYLOAD_TOO_LARGE, response.getStatusCode());
        assertEquals("Image is too large", response.getBody());
    }
}
