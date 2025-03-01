package es.iespuertodelacruz.routinefights.shared.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.iespuertodelacruz.routinefights.shared.exceptions.ImageNotFoundException;
import es.iespuertodelacruz.routinefights.shared.exceptions.ImageSaveException;

@Service
public class ImageService {
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

    public UrlResource findImage(String imageName) {
        try {
            Path pathForFilename = uploads.resolve(imageName);
            UrlResource resource = new UrlResource(pathForFilename.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new ImageNotFoundException("Error: " + imageName + " not found");
            }
        } catch (MalformedURLException e) {
            throw new ImageNotFoundException("Error: " + e.getMessage());
        }
    }

    public String save(MultipartFile file) {
        try {
            Files.createDirectories(uploads);
        } catch (IOException e) {
            throw new ImageSaveException("Could not initialize folder for upload");
        }
        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new ImageSaveException("Filename is null");
        }
        Path filePath = this.uploads.resolve(filename);
        filePath = getFreePath(filename, filePath);
        try {
            Files.copy(file.getInputStream(), filePath);
            return filePath.getFileName().toString();
        } catch (IOException e) {
            throw new ImageSaveException("Could not store the file. Error: " + e.getMessage());
        }
    }

}
