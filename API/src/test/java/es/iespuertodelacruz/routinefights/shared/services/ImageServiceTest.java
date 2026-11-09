package es.iespuertodelacruz.routinefights.shared.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import es.iespuertodelacruz.routinefights.shared.dto.CropRequest;
import es.iespuertodelacruz.routinefights.shared.dto.ImageSize;
import es.iespuertodelacruz.routinefights.shared.exceptions.ImageNotFoundException;
import es.iespuertodelacruz.routinefights.shared.exceptions.ImageUploadException;

class ImageServiceTest {
    private ImageService imageService;
    private Path uploadsDirectory;

    @BeforeEach
    void setup() throws IOException {
        uploadsDirectory = Files.createTempDirectory("uploads_test");
        imageService = new ImageService();
        ReflectionTestUtils.setField(imageService, "uploads", uploadsDirectory);
    }

    @AfterEach
    void tearDown() throws IOException {
        try (var walk = Files.walk(uploadsDirectory)) {
            walk.sorted(Comparator.reverseOrder()).forEach(path -> path.toFile().delete());
        }
    }

    private static byte[] image(int width, int height, String format, boolean transparent) throws IOException {
        BufferedImage img = new BufferedImage(width, height,
                transparent ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.RED);
        g.fillRect(0, 0, width / 2, height);
        g.setColor(Color.BLUE);
        g.fillRect(width / 2, 0, width - width / 2, height);
        if (transparent) {
            // Punch a fully transparent hole in the middle.
            g.setComposite(java.awt.AlphaComposite.Clear);
            g.fillRect(width / 4, height / 4, width / 2, height / 2);
        }
        g.dispose();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(img, format, out);
        return out.toByteArray();
    }

    private static MockMultipartFile jpeg(int width, int height) throws IOException {
        return new MockMultipartFile("file", "photo.jpg", "image/jpeg", image(width, height, "jpg", false));
    }

    private BufferedImage read(Path path) throws IOException {
        return ImageIO.read(path.toFile());
    }

    @Test
    void testSaveAndFindImage() throws IOException {
        String savedFilename = imageService.save(jpeg(400, 300));
        assertNotNull(savedFilename);
        assertTrue(Files.exists(uploadsDirectory.resolve(savedFilename)));

        UrlResource resource = imageService.findImage(savedFilename);
        assertTrue(resource.exists());
    }

    @Test
    void storesEveryVariantNeverUpscaling() throws IOException {
        String saved = imageService.save(jpeg(3000, 2000));
        BufferedImage stored = read(uploadsDirectory.resolve(saved));
        assertEquals(ImageService.MAX_STORED_SIDE, stored.getWidth());

        for (ImageSize size : ImageSize.values()) {
            UrlResource variant = imageService.findImage(saved, size);
            BufferedImage img = ImageIO.read(variant.getFile());
            assertEquals(size.getMaxSide(), Math.max(img.getWidth(), img.getHeight()), size.name());
        }

        String small = imageService.save(jpeg(200, 100));
        BufferedImage large = ImageIO.read(imageService.findImage(small, ImageSize.LARGE).getFile());
        assertEquals(200, large.getWidth());
    }

    @Test
    void cropsAndRotates() throws IOException {
        // Left half red, right half blue. Keep the right half, then rotate it.
        String saved = imageService.save(jpeg(400, 200), new CropRequest(0.5, 0, 0.5, 1, 0));
        BufferedImage img = read(uploadsDirectory.resolve(saved));
        assertEquals(200, img.getWidth());
        assertEquals(200, img.getHeight());
        assertTrue(new Color(img.getRGB(100, 100)).getBlue() > 200);

        String rotated = imageService.save(jpeg(400, 200), new CropRequest(0, 0, 1, 1, 90));
        BufferedImage r = read(uploadsDirectory.resolve(rotated));
        assertEquals(200, r.getWidth());
        assertEquals(400, r.getHeight());
        // Rotating clockwise moves the (red) left half to the top.
        assertTrue(new Color(r.getRGB(100, 50)).getRed() > 200);
    }

    @Test
    void keepsTransparencyAsPng() throws IOException {
        MockMultipartFile png = new MockMultipartFile("file", "badge.png", "image/png", image(100, 100, "png", true));
        String saved = imageService.save(png);
        assertTrue(saved.endsWith(".png"));
        assertTrue(imageService.findImage(saved, ImageSize.TINY).getFilename().endsWith(".png"));
    }

    @Test
    void opaquePngIsStoredAsJpeg() throws IOException {
        MockMultipartFile png = new MockMultipartFile("file", "x.png", "image/png", image(100, 100, "png", false));
        assertTrue(imageService.save(png).endsWith(".jpg"));
    }

    @Test
    void rejectsFilesThatAreNotImages() {
        MockMultipartFile fake = new MockMultipartFile("file", "x.jpg", "image/jpeg", "not an image".getBytes());
        assertThrows(ImageUploadException.class, () -> imageService.save(fake));
        assertTrue(imageService.getAll().isEmpty());
    }

    @Test
    void rejectsOversizedUploads() {
        MockMultipartFile big = new MockMultipartFile("file", "x.jpg", "image/jpeg",
                new byte[(int) ImageService.MAX_UPLOAD_BYTES + 1]);
        assertThrows(ImageUploadException.class, () -> imageService.save(big));
    }

    @Test
    void createsVariantsLazilyForLegacyImages() throws IOException {
        Files.write(uploadsDirectory.resolve("legacy.png"), image(1000, 500, "png", false));
        UrlResource medium = imageService.findImage("legacy.png", ImageSize.MEDIUM);
        assertEquals(720, ImageIO.read(medium.getFile()).getWidth());
        assertNotEquals(uploadsDirectory.resolve("legacy.png").toUri(), medium.getURI());
    }

    @Test
    void testFindImageNotFound() {
        Exception exception = assertThrows(ImageNotFoundException.class,
                () -> imageService.findImage("nonexistent.jpg"));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void testSaveDuplicateFileNames() throws IOException {
        String savedFilename1 = imageService.save(jpeg(10, 10));
        String savedFilename2 = imageService.save(jpeg(10, 10));
        assertTrue(savedFilename1.endsWith(".jpg"));
        assertNotEquals(savedFilename1, savedFilename2);
    }

    @Test
    void getAllListsOnlyStoredPictures() throws IOException {
        String savedFilename = imageService.save(jpeg(50, 50));
        List<String> images = imageService.getAll();
        assertEquals(List.of(savedFilename), images);
    }

    @Test
    void deleteRemovesVariants() throws IOException {
        String savedFilename = imageService.save(jpeg(50, 50));
        imageService.delete(savedFilename);
        assertTrue(Files.notExists(uploadsDirectory.resolve(savedFilename)));
        try (var stream = Files.list(uploadsDirectory.resolve(".variants"))) {
            assertEquals(0, stream.count());
        }
    }

    @Test
    void testDeleteException() {
        Exception exception = assertThrows(ImageNotFoundException.class,
                () -> imageService.delete("nonexistent.jpg"));
        assertTrue(exception.getMessage().contains("Error"));
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

    @Test
    void rejectsPathTraversal() {
        assertFalse(imageService.exists("../../etc/passwd"));
        assertFalse(imageService.exists(".variants"));
        assertThrows(ImageNotFoundException.class, () -> imageService.findImage("../secret.png"));
    }

    @Test
    void savedNamesIgnoreClientFileName() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "../../evil.PNG", "image/png",
                image(20, 20, "png", false));
        String saved = imageService.save(file);
        assertTrue(saved.matches("[0-9a-f-]{36}\\.jpg"), saved);
        assertTrue(imageService.exists(saved));
    }

    @Test
    void servesLegacyFileAsIsWhenItCannotBeResized() throws IOException {
        Files.write(uploadsDirectory.resolve("broken.jpg"), "not really a jpeg".getBytes());
        UrlResource resource = imageService.findImage("broken.jpg", ImageSize.SMALL);
        assertEquals(uploadsDirectory.resolve("broken.jpg").toUri(), resource.getURI());
    }

    @Test
    void rejectsInvalidNames() {
        assertFalse(imageService.exists(null));
        assertFalse(imageService.exists(" "));
        assertFalse(imageService.exists("a\\b.png"));
        assertFalse(imageService.exists("missing.png"));
        assertThrows(ImageNotFoundException.class, () -> imageService.findImage("missing.png", ImageSize.TINY));
    }

    @Test
    void rejectsEmptyUploads() {
        MockMultipartFile empty = new MockMultipartFile("file", "x.jpg", "image/jpeg", new byte[0]);
        assertThrows(ImageUploadException.class, () -> imageService.save(empty));
        assertThrows(ImageUploadException.class, () -> imageService.save(null, null));
    }

    @Test
    void contentTypesByExtension() {
        assertEquals("image/png", ImageService.contentType("a.PNG"));
        assertEquals("image/jpeg", ImageService.contentType("a.jpg"));
        assertEquals("image/jpeg", ImageService.contentType("a.jpeg"));
        assertEquals("image/webp", ImageService.contentType("a.webp"));
        assertEquals("image/gif", ImageService.contentType("a.gif"));
        assertEquals("application/octet-stream", ImageService.contentType("a.bin"));
    }

    @Test
    void moveIntoPlaceReplacesTarget() throws IOException {
        Path source = Files.writeString(uploadsDirectory.resolve("src.part"), "new");
        Path target = Files.writeString(uploadsDirectory.resolve("dst.jpg"), "old");
        ImageService.moveIntoPlace(source, target);
        assertEquals("new", Files.readString(target));
        assertTrue(Files.notExists(source));
    }

    @Test
    void reusesExistingVariant() throws IOException {
        String saved = imageService.save(jpeg(800, 600));
        UrlResource first = imageService.findImage(saved, ImageSize.MEDIUM);
        UrlResource second = imageService.findImage(saved, ImageSize.MEDIUM);
        assertEquals(first.getURI(), second.getURI());
    }
}
