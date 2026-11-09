package es.iespuertodelacruz.routinefights.shared.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import es.iespuertodelacruz.routinefights.shared.exceptions.ImageNotFoundException;
import es.iespuertodelacruz.routinefights.shared.services.ImageService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = ImageController.class)
class ImageControllerTest {

    private static final UrlResource URL_RESOURCE;

    static {
        try {
            URL_RESOURCE = new UrlResource("file://test.jpg");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String ORIGINAL_FILE_NAME = "test.jpg";

    @InjectMocks
    @Autowired
    private ImageController imageController;

    @MockitoBean
    private ImageService imageService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void uploadFileOKTest() {
        MockMultipartFile file = new MockMultipartFile("file", ORIGINAL_FILE_NAME, "image/jpeg", "test".getBytes());
        when(imageService.save(file, null)).thenReturn(ORIGINAL_FILE_NAME);
        ResponseEntity<?> responseEntity = imageController.uploadFile(file);

        assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
        assertEquals(ORIGINAL_FILE_NAME, responseEntity.getBody());
    }

    @Test
    void uploadFileNotFileTest() {
        MockMultipartFile file = null;
        ResponseEntity<?> responseEntity = imageController.uploadFile(file);

        assertEquals(HttpStatusCode.valueOf(400), responseEntity.getStatusCode());
        assertEquals("File is empty", responseEntity.getBody());
    }

    @Test
    void uploadFileEmptyFileTst() {
        MockMultipartFile file = new MockMultipartFile("file", ORIGINAL_FILE_NAME, "image/jpeg", new byte[0]);
        ResponseEntity<?> responseEntity = imageController.uploadFile(file);

        assertEquals(HttpStatusCode.valueOf(400), responseEntity.getStatusCode());
        assertEquals("File is empty", responseEntity.getBody());
    }

    @Test
    void uploadFileNotContentTypeTest() {
        MockMultipartFile file = new MockMultipartFile("file", ORIGINAL_FILE_NAME, null, "test".getBytes());
        ResponseEntity<?> responseEntity = imageController.uploadFile(file);

        assertEquals(HttpStatusCode.valueOf(400), responseEntity.getStatusCode());
        assertEquals("File is empty", responseEntity.getBody());
    }

    @Test
    void uploadFileIsNotImageTest() {
        MockMultipartFile file = new MockMultipartFile("file", ORIGINAL_FILE_NAME, "text/plain", "test".getBytes());
        ResponseEntity<?> responseEntity = imageController.uploadFile(file);

        assertEquals(HttpStatusCode.valueOf(400), responseEntity.getStatusCode());
        assertEquals("File is not an image", responseEntity.getBody());
    }

    @Test
    void uploadFileTooLargeTest() {
        MockMultipartFile file = new MockMultipartFile("file", ORIGINAL_FILE_NAME, "image/jpeg",
                new byte[(int) ImageService.MAX_UPLOAD_BYTES + 1]);
        ResponseEntity<?> responseEntity = imageController.uploadFile(file);
        assertEquals(HttpStatusCode.valueOf(413), responseEntity.getStatusCode());
    }

    @Test
    void uploadFileWithCropTest() {
        MockMultipartFile file = new MockMultipartFile("file", ORIGINAL_FILE_NAME, "image/jpeg", "test".getBytes());
        es.iespuertodelacruz.routinefights.shared.dto.CropRequest crop =
                new es.iespuertodelacruz.routinefights.shared.dto.CropRequest(0.1, 0.1, 0.5, 0.5, 90);
        when(imageService.save(file, crop)).thenReturn(ORIGINAL_FILE_NAME);
        ResponseEntity<?> responseEntity = imageController.uploadFile(file, 0.1, 0.1, 0.5, 0.5, 90);
        assertEquals(ORIGINAL_FILE_NAME, responseEntity.getBody());
    }

    @Test
    void uploadFileInvalidCropTest() {
        MockMultipartFile file = new MockMultipartFile("file", ORIGINAL_FILE_NAME, "image/jpeg", "test".getBytes());
        ResponseEntity<?> responseEntity = imageController.uploadFile(file, 0.0, 0.0, 0.0, 1.0, 0);
        assertEquals(HttpStatusCode.valueOf(400), responseEntity.getStatusCode());
    }

    @Test
    void getImageOKTest() {
        when(imageService.findImage(ORIGINAL_FILE_NAME, null)).thenReturn(URL_RESOURCE);
        ResponseEntity<?> responseEntity = imageController.getImage(ORIGINAL_FILE_NAME);
        assertEquals(URL_RESOURCE, responseEntity.getBody());
    }

    @Test
    void getImageSizedTest() {
        when(imageService.findImage(ORIGINAL_FILE_NAME, es.iespuertodelacruz.routinefights.shared.dto.ImageSize.TINY))
                .thenReturn(URL_RESOURCE);
        ResponseEntity<?> responseEntity = imageController.getImage(ORIGINAL_FILE_NAME, "tiny");
        assertEquals(URL_RESOURCE, responseEntity.getBody());
        assertEquals("image/jpeg", responseEntity.getHeaders().getContentType().toString());
        org.junit.jupiter.api.Assertions.assertTrue(responseEntity.getHeaders().getCacheControl().contains("immutable"));
    }

    @Test
    void getImageUnknownSizeTest() {
        ResponseEntity<?> responseEntity = imageController.getImage(ORIGINAL_FILE_NAME, "huge");
        assertEquals(HttpStatusCode.valueOf(400), responseEntity.getStatusCode());
    }

    @Test
    void getImageErrorTest() {
        when(imageService.findImage(ORIGINAL_FILE_NAME, null)).thenThrow(new ImageNotFoundException("Image not found"));
        ResponseEntity<?> responseEntity = imageController.getImage(ORIGINAL_FILE_NAME);
        assertEquals(HttpStatusCode.valueOf(404), responseEntity.getStatusCode());
    }

}
