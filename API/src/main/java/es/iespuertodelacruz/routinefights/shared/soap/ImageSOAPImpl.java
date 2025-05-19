package es.iespuertodelacruz.routinefights.shared.soap;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.core.io.UrlResource;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import es.iespuertodelacruz.routinefights.shared.exceptions.ImageUploadException;
import es.iespuertodelacruz.routinefights.shared.services.ImageService;
import jakarta.jws.WebService;

@WebService(endpointInterface = "es.iespuertodelacruz.routinefights.shared.soap.ImageSOAPInterface")
public class ImageSOAPImpl implements ImageSOAPInterface {
    private static final Logger logger = java.util.logging.Logger.getLogger(ImageSOAPImpl.class.getName());

    private final ImageService imageService;

    public ImageSOAPImpl(ImageService imageService) {
        this.imageService = imageService;
    }

    @Override
    public UrlResource getImage(String imageName) {
        return imageService.findImage(imageName);
    }

    @Override
    public String uploadFile(byte[] bytes, String fileName, String contentType) {
        MultipartFile file = new MultipartFile() {
            @Override
            @NonNull
            public String getName() {
                return fileName;
            }

            @Override
            public String getOriginalFilename() {
                return fileName;
            }

            @Override
            public String getContentType() {
                return contentType;
            }

            @Override
            public boolean isEmpty() {
                return bytes.length == 0;
            }

            @Override
            public long getSize() {
                return bytes.length;
            }

            @Override
            @NonNull
            public byte[] getBytes() {
                return bytes;
            }

            @Override
            @NonNull
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(bytes);
            }

            @Override
            public void transferTo(@NonNull File dest) throws IOException, IllegalStateException {
                try (InputStream inputStream = getInputStream()) {
                    Files.copy(inputStream, dest.toPath());
                }
            }

        };
        if (file.isEmpty()) {
            throw new ImageUploadException("File is empty");
        }
        if (contentType != null) {
            if (!contentType.startsWith("image/")) {
                throw new ImageUploadException("File is not an image");
            }
            String filename = imageService.save(file);
            logger.log(Level.INFO, "Image uploaded: {0}", filename);
            return filename;
        }
        logger.warning("Error uploading image: Content type is null");
        throw new ImageUploadException("File is empty");
    }
}
