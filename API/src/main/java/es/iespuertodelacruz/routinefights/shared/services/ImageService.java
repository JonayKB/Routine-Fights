package es.iespuertodelacruz.routinefights.shared.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.iespuertodelacruz.routinefights.shared.exceptions.ImageNotFoundException;
import es.iespuertodelacruz.routinefights.shared.exceptions.ImageSaveException;

@Service
public class ImageService {
    private static final String ERROR = "Error: ";
    private final Path uploads = Paths.get("uploads");

    private Path getFreePath(String filename, Path filePath) {
        int counter = 1;
        while (Files.exists(filePath)) {
            String newFilename = filename.replaceFirst("(\\.[^.]+$|$)", "_" + counter + "$1");
            filePath = this.uploads.resolve(newFilename);
            counter++;
        }
        return filePath;
    }

    /**
     * Resolves a stored image name inside the uploads folder, rejecting anything
     * that could escape it (separators, "..", absolute paths).
     */
    Path safeResolve(String imageName) {
        if (imageName == null || imageName.isBlank() || imageName.contains("/") || imageName.contains("\\")
                || imageName.contains("..")) {
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

    public UrlResource findImage(String imageName) {
        try {
            Path pathForFilename = safeResolve(imageName);
            UrlResource resource = new UrlResource(pathForFilename.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new ImageNotFoundException(ERROR + imageName + " not found");
            }
        } catch (MalformedURLException e) {
            throw new ImageNotFoundException(ERROR + e.getMessage());
        }
    }

    public String save(MultipartFile file) {
        try {
            Files.createDirectories(uploads);
        } catch (IOException e) {
            throw new ImageSaveException("Could not initialize folder for upload");
        }
        String original = file.getOriginalFilename();
        if (original == null) {
            throw new ImageSaveException("Filename is null");
        }
        // Never trust the client's file name: keep only a short alphanumeric extension.
        String extension = "";
        int dot = original.lastIndexOf('.');
        if (dot >= 0) {
            String candidate = original.substring(dot + 1).toLowerCase();
            if (candidate.matches("[a-z0-9]{1,5}")) {
                extension = "." + candidate;
            }
        }
        String filename = java.util.UUID.randomUUID() + extension;
        Path filePath = this.uploads.resolve(filename);
        filePath = getFreePath(filename, filePath);
        try {
            Files.copy(file.getInputStream(), filePath);
            return filePath.getFileName().toString();
        } catch (IOException e) {
            throw new ImageSaveException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public void delete(String imageName) {
        Path pathForFilename = safeResolve(imageName);
        try {
            Files.delete(pathForFilename);
        } catch (IOException e) {
            throw new ImageNotFoundException(ERROR + e.getMessage());
        }
    }

    public List<String> getAll() {
        try (var stream = Files.list(uploads)) {
            return stream.map(path -> path.getFileName().toString()).toList();
        } catch (IOException e) {
            throw new ImageNotFoundException(ERROR + e.getMessage());
        }

    }

}
