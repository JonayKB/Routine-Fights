package es.iespuertodelacruz.routinefights.shared.services;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.iespuertodelacruz.routinefights.shared.dto.CropRequest;
import es.iespuertodelacruz.routinefights.shared.dto.ImageSize;
import es.iespuertodelacruz.routinefights.shared.exceptions.ImageNotFoundException;
import es.iespuertodelacruz.routinefights.shared.exceptions.ImageSaveException;
import es.iespuertodelacruz.routinefights.shared.exceptions.ImageUploadException;
import es.iespuertodelacruz.routinefights.shared.utils.ImageProcessor;

/**
 * Stores pictures on disk.
 * <p>
 * Every upload is decoded, oriented, optionally cropped and re-encoded (which
 * also strips metadata such as GPS). The result is kept as
 * {@code uploads/<uuid>.jpg} (or {@code .png} when it has transparency), capped
 * at {@link #MAX_STORED_SIDE} pixels, and that name is what the rest of the
 * application references. Smaller renditions ({@link ImageSize}) live in
 * {@code uploads/.variants/} and are created on upload, or lazily the first
 * time they are requested for pictures uploaded before they existed.
 */
@Service
public class ImageService {
    private static final String ERROR = "Error: ";
    private static final String VARIANTS_DIR = ".variants";
    private static final Logger LOGGER = Logger.getLogger(ImageService.class.getName());

    /** Largest upload accepted, in bytes. Keep in sync with spring.servlet.multipart.max-file-size. */
    public static final long MAX_UPLOAD_BYTES = 10L * 1024 * 1024;
    /** Longest side of the stored "original". */
    public static final int MAX_STORED_SIDE = 2048;
    private static final float STORED_QUALITY = 0.86f;

    private Path uploads = Paths.get("uploads");
    private final ConcurrentHashMap<String, Object> locks = new ConcurrentHashMap<>();

    private Path variants() {
        return uploads.resolve(VARIANTS_DIR);
    }

    /**
     * Resolves a stored image name inside the uploads folder, rejecting anything
     * that could escape it (separators, "..", absolute paths, hidden files).
     */
    Path safeResolve(String imageName) {
        if (imageName == null || imageName.isBlank() || imageName.contains("/") || imageName.contains("\\")
                || imageName.contains("..") || imageName.startsWith(".")) {
            throw new ImageNotFoundException(ERROR + "invalid image name");
        }
        Path root = uploads.toAbsolutePath().normalize();
        Path resolved = root.resolve(imageName).normalize();
        if (!resolved.startsWith(root)) {
            throw new ImageNotFoundException(ERROR + "invalid image name");
        }
        return resolved;
    }

    public boolean exists(String imageName) {
        try {
            return Files.isRegularFile(safeResolve(imageName));
        } catch (ImageNotFoundException e) {
            return false;
        }
    }

    /** The full stored picture. */
    public UrlResource findImage(String imageName) {
        return findImage(imageName, null);
    }

    /**
     * @param size rendition to return, or {@code null} for the stored picture.
     *             Missing renditions are generated on the fly and cached.
     */
    public UrlResource findImage(String imageName, ImageSize size) {
        Path original = safeResolve(imageName);
        if (!Files.isRegularFile(original)) {
            throw new ImageNotFoundException(ERROR + imageName + " not found");
        }
        Path target = original;
        if (size != null) {
            try {
                target = variant(imageName, original, size);
            } catch (RuntimeException e) {
                // A legacy file we can't decode: serve it as it is rather than fail.
                LOGGER.log(Level.WARNING, "Could not build {0} variant of {1}: {2}",
                        new Object[] { size, imageName, e.getMessage() });
            }
        }
        try {
            return new UrlResource(target.toUri());
        } catch (MalformedURLException e) {
            throw new ImageNotFoundException(ERROR + e.getMessage());
        }
    }

    public String save(MultipartFile file) {
        return save(file, null);
    }

    /**
     * Validates, optionally crops and stores an uploaded picture together with
     * all of its renditions.
     *
     * @return the stored name to reference from posts, users, etc.
     */
    public String save(MultipartFile file, CropRequest crop) {
        if (file == null || file.isEmpty()) {
            throw new ImageUploadException("File is empty");
        }
        if (file.getSize() > MAX_UPLOAD_BYTES) {
            throw new ImageUploadException("Image is too large (max " + MAX_UPLOAD_BYTES / (1024 * 1024) + " MB)");
        }
        byte[] data;
        try {
            data = file.getBytes();
        } catch (IOException e) {
            throw new ImageSaveException("Could not read the file");
        }

        ImageProcessor.Decoded decoded = ImageProcessor.decode(data);
        BufferedImage image = ImageProcessor.crop(decoded.image(), crop);
        boolean alpha = decoded.alpha();

        try {
            Files.createDirectories(variants());
        } catch (IOException e) {
            throw new ImageSaveException("Could not initialize folder for upload");
        }

        // Never trust the client's file name.
        String filename = UUID.randomUUID() + "." + ImageProcessor.extension(alpha);
        Path path = uploads.resolve(filename);
        BufferedImage stored = ImageProcessor.fit(image, MAX_STORED_SIDE);
        writeAtomically(path, stored, alpha, STORED_QUALITY);

        // Build from the largest to the smallest so each step downsizes a smaller image.
        BufferedImage source = stored;
        for (int i = ImageSize.values().length - 1; i >= 0; i--) {
            ImageSize size = ImageSize.values()[i];
            source = ImageProcessor.fit(source, size.getMaxSide());
            writeAtomically(variantPath(filename, size, alpha), source, alpha, size.getQuality());
        }
        return filename;
    }

    public void delete(String imageName) {
        Path pathForFilename = safeResolve(imageName);
        try {
            Files.delete(pathForFilename);
        } catch (IOException e) {
            throw new ImageNotFoundException(ERROR + e.getMessage());
        }
        for (ImageSize size : ImageSize.values()) {
            for (boolean alpha : new boolean[] { false, true }) {
                try {
                    Files.deleteIfExists(variantPath(imageName, size, alpha));
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "Could not delete variant of {0}", imageName);
                }
            }
        }
    }

    /** Names of the stored pictures (renditions excluded). */
    public List<String> getAll() {
        try (var stream = Files.list(uploads)) {
            return stream.filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .filter(name -> !name.startsWith("."))
                    .toList();
        } catch (IOException e) {
            throw new ImageNotFoundException(ERROR + e.getMessage());
        }
    }

    /** MIME type for a stored file, based on its extension. */
    public static String contentType(String filename) {
        String name = filename.toLowerCase(Locale.ROOT);
        if (name.endsWith(".png")) {
            return "image/png";
        }
        if (name.endsWith(".webp")) {
            return "image/webp";
        }
        if (name.endsWith(".gif")) {
            return "image/gif";
        }
        if (name.endsWith(".jpg") || name.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        return "application/octet-stream";
    }

    // ------------------------------------------------------------------ variants

    private Path variantPath(String imageName, ImageSize size, boolean alpha) {
        return variants().resolve(imageName + "." + size.key() + "." + ImageProcessor.extension(alpha));
    }

    private Path variant(String imageName, Path original, ImageSize size) {
        for (boolean alpha : new boolean[] { false, true }) {
            Path existing = variantPath(imageName, size, alpha);
            if (Files.isRegularFile(existing)) {
                return existing;
            }
        }
        // Only one thread builds the renditions of a given picture.
        Object lock = locks.computeIfAbsent(imageName, k -> new Object());
        try {
            synchronized (lock) {
                for (boolean alpha : new boolean[] { false, true }) {
                    Path existing = variantPath(imageName, size, alpha);
                    if (Files.isRegularFile(existing)) {
                        return existing;
                    }
                }
                byte[] data = Files.readAllBytes(original);
                ImageProcessor.Decoded decoded = ImageProcessor.decode(data);
                Files.createDirectories(variants());
                BufferedImage resized = ImageProcessor.fit(decoded.image(), size.getMaxSide());
                Path path = variantPath(imageName, size, decoded.alpha());
                writeAtomically(path, resized, decoded.alpha(), size.getQuality());
                return path;
            }
        } catch (IOException e) {
            throw new ImageSaveException("Could not create variant: " + e.getMessage());
        } finally {
            locks.remove(imageName);
        }
    }

    private void writeAtomically(Path target, BufferedImage image, boolean alpha, float quality) {
        Path temp = null;
        try {
            temp = Files.createTempFile(target.getParent(), ".tmp-", ".part");
            try (OutputStream out = Files.newOutputStream(temp)) {
                ImageProcessor.write(image, alpha, quality, out);
            }
            moveIntoPlace(temp, target);
        } catch (IOException e) {
            throw new ImageSaveException("Could not store the file. Error: " + e.getMessage());
        } finally {
            deleteQuietly(temp);
        }
    }

    /** Replaces {@code target} atomically when the file system allows it. */
    static void moveIntoPlace(Path source, Path target) throws IOException {
        try {
            Files.move(source, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static void deleteQuietly(Path path) {
        if (path == null) {
            return;
        }
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            LOGGER.log(Level.FINE, "Could not delete temporary file {0}", path);
        }
    }
}
