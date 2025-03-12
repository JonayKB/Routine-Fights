package es.iespuertodelacruz.routinefights.shared.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import es.iespuertodelacruz.routinefights.shared.exceptions.ImageNotFoundException;

class ImageServiceTest {
    private ImageService imageService;
    private Path uploadsDirectory;

    @BeforeEach
    void setup() throws IOException, NoSuchFieldException, IllegalAccessException {
        uploadsDirectory = Files.createTempDirectory("uploads_test");
        imageService = new ImageService();
        Field uploadsField = ImageService.class.getDeclaredField("uploads");
        uploadsField.setAccessible(true);
        uploadsField.set(imageService, uploadsDirectory);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.walk(uploadsDirectory)
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    @Test
    void testSaveAndFindImage() {
        String originalFilename = "test.jpg";
        byte[] content = "dummy image content".getBytes();
        MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename, "image/jpeg", content);

        String savedFilename = imageService.save(multipartFile);
        assertNotNull(savedFilename, "Saved filename should not be null");
        Path savedFilePath = uploadsDirectory.resolve(savedFilename);
        assertTrue(Files.exists(savedFilePath), "Saved file should exist");

        UrlResource resource = imageService.findImage(savedFilename);
        assertNotNull(resource, "Returned resource should not be null");
        assertTrue(resource.exists(), "Returned resource should exist");
    }

    @Test
    void testFindImageNotFound() {
        String nonExistentFilename = "nonexistent.jpg";
        Exception exception = assertThrows(ImageNotFoundException.class, () -> {
            imageService.findImage(nonExistentFilename);
        });
        assertTrue(exception.getMessage().contains("not found"),
                "Exception message should indicate file was not found");
    }

    @Test
    void testSaveDuplicateFileNames() {
        String originalFilename = "duplicate.jpg";
        byte[] content = "dummy content".getBytes();
        MockMultipartFile file1 = new MockMultipartFile("file", originalFilename, "image/jpeg", content);
        MockMultipartFile file2 = new MockMultipartFile("file", originalFilename, "image/jpeg", content);

        String savedFilename1 = imageService.save(file1);
        String savedFilename2 = imageService.save(file2);

        assertEquals("duplicate.jpg", savedFilename1, "The first file should keep its original name");

        assertNotEquals(savedFilename1, savedFilename2, "Duplicate file should have a different name");
        assertTrue(savedFilename2.matches("duplicate_\\d+\\.jpg"), "Filename should be appended with a counter");
    }

    @Test
    void testGetAll() {
        String originalFilename = "test.jpg";
        byte[] content = "dummy image content".getBytes();
        MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename, "image/jpeg", content);

        String savedFilename = imageService.save(multipartFile);
        assertNotNull(savedFilename, "Saved filename should not be null");
        Path savedFilePath = uploadsDirectory.resolve(savedFilename);
        assertTrue(Files.exists(savedFilePath), "Saved file should exist");

        List<String> images = imageService.getAll();
        assertEquals(1, images.size(), "There should be one image");
        assertEquals(savedFilename, images.get(0), "The saved filename should be in the list");
    }

    @Test
    void testDelete() {
        String originalFilename = "test.jpg";
        byte[] content = "dummy image content".getBytes();
        MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename, "image/jpeg", content);

        String savedFilename = imageService.save(multipartFile);
        assertNotNull(savedFilename, "Saved filename should not be null");
        Path savedFilePath = uploadsDirectory.resolve(savedFilename);
        assertTrue(Files.exists(savedFilePath), "Saved file should exist");

        imageService.delete(savedFilename);
        assertTrue(Files.notExists(savedFilePath), "Deleted file should not exist");
    }

    @Test
    void testDeleteException() {
        Exception exception = assertThrows(ImageNotFoundException.class, () -> {
            imageService.delete("nonexistent.jpg");
        });
        assertTrue(exception.getMessage().contains("Error"),
                "Exception message should indicate file was not found");
    }

    @Test
    void testGetAllException() throws IOException {
        Path tempFile = Files.createTempFile("test", ".tmp");
        try {
            ReflectionTestUtils.setField(imageService, "uploads", tempFile);

            assertThrows(ImageNotFoundException.class, () -> imageService.getAll());
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

}
