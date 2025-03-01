package es.iespuertodelacruz.routinefights.shared.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.nio.charset.MalformedInputException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import es.iespuertodelacruz.routinefights.shared.exceptions.ImageNotFoundException;
import es.iespuertodelacruz.routinefights.shared.exceptions.ImageUploadException;
import es.iespuertodelacruz.routinefights.shared.security.JwtFilter;
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
        when(imageService.save(file)).thenReturn(ORIGINAL_FILE_NAME);
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
    void getImageOKTest() {
        when(imageService.findImage(ORIGINAL_FILE_NAME)).thenReturn(URL_RESOURCE);
        ResponseEntity<?> responseEntity = imageController.getImage(ORIGINAL_FILE_NAME);
        assertEquals(URL_RESOURCE, responseEntity.getBody());
    }

    @Test
    void getImageErrorTest() {
        when(imageService.findImage(ORIGINAL_FILE_NAME)).thenThrow(new ImageNotFoundException("Image not found"));
        ResponseEntity<?> responseEntity = imageController.getImage(ORIGINAL_FILE_NAME);
        assertEquals(HttpStatusCode.valueOf(404), responseEntity.getStatusCode());
    }

}
