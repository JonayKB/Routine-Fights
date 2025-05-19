package es.iespuertodelacruz.routinefights.shared.soap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.UrlResource;
import es.iespuertodelacruz.routinefights.shared.exceptions.ImageUploadException;
import es.iespuertodelacruz.routinefights.shared.services.ImageService;

class ImageSOAPImplTest {

    private static final String VALID_IMAGE_NAME = "testImage.jpg";
    private static final String INVALID_CONTENT_TYPE = "text/plain";
    private static final String VALID_CONTENT_TYPE = "image/jpeg";
    private static final String VALID_FILE_NAME = "image.jpg";
    private static final byte[] VALID_BYTES = new byte[] { 1, 2, 3 };
    private static final byte[] EMPTY_BYTES = new byte[] {};

    @Mock
    private ImageService imageService;

    private ImageSOAPImpl imageSOAP;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        imageSOAP = new ImageSOAPImpl(imageService);
    }

    @Test
    void testGetImage_Success() {
        UrlResource mockResource = mock(UrlResource.class);
        when(imageService.findImage(VALID_IMAGE_NAME)).thenReturn(mockResource);

        UrlResource result = imageSOAP.getImage(VALID_IMAGE_NAME);

        assertNotNull(result);
        verify(imageService, times(1)).findImage(VALID_IMAGE_NAME);
    }

    @Test
    void testUploadFile_Success() {
        when(imageService.save(any())).thenReturn(VALID_FILE_NAME);

        String result = imageSOAP.uploadFile(VALID_BYTES, VALID_FILE_NAME, VALID_CONTENT_TYPE);

        assertEquals(VALID_FILE_NAME, result);
        verify(imageService, times(1)).save(any());
    }

    @Test
    void testUploadFile_EmptyFile() {
        ImageUploadException exception = assertThrows(ImageUploadException.class, () -> {
            imageSOAP.uploadFile(EMPTY_BYTES, VALID_FILE_NAME, VALID_CONTENT_TYPE);
        });

        assertEquals("File is empty", exception.getMessage());
    }

    @Test
    void testUploadFile_InvalidContentType() {
        ImageUploadException exception = assertThrows(ImageUploadException.class, () -> {
            imageSOAP.uploadFile(VALID_BYTES, VALID_FILE_NAME, INVALID_CONTENT_TYPE);
        });

        assertEquals("File is not an image", exception.getMessage());
    }

    @Test
    void testUploadFile_NullContentType() {
        ImageUploadException exception = assertThrows(ImageUploadException.class, () -> {
            imageSOAP.uploadFile(VALID_BYTES, VALID_FILE_NAME, null);
        });

        assertEquals("File is empty", exception.getMessage());
    }
}
