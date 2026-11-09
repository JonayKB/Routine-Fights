package es.iespuertodelacruz.routinefights.shared.controllers;

import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.core.io.UrlResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.iespuertodelacruz.routinefights.shared.dto.CropRequest;
import es.iespuertodelacruz.routinefights.shared.dto.ImageSize;
import es.iespuertodelacruz.routinefights.shared.exceptions.ImageNotFoundException;
import es.iespuertodelacruz.routinefights.shared.exceptions.ImageUploadException;
import es.iespuertodelacruz.routinefights.shared.services.ImageService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/images")
@CrossOrigin
@Tag(name = "IMAGES", description = "Images management")
public class ImageController {

    /** Stored names are random UUIDs and never change, so clients may cache forever. */
    private static final CacheControl IMMUTABLE = CacheControl.maxAge(Duration.ofDays(365)).cachePrivate()
            .immutable();

    private final ImageService imageService;

    Logger logger = Logger.getLogger(ImageController.class.getName());

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    public ResponseEntity<String> uploadFile(MultipartFile file) {
        return uploadFile(file, null, null, null, null, null);
    }

    /**
     * Uploads a picture. The optional crop is given as fractions (0..1) of the
     * upright picture after rotating it {@code rotation} degrees clockwise.
     */
    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file,
            @RequestParam(value = "cropX", required = false) Double cropX,
            @RequestParam(value = "cropY", required = false) Double cropY,
            @RequestParam(value = "cropWidth", required = false) Double cropWidth,
            @RequestParam(value = "cropHeight", required = false) Double cropHeight,
            @RequestParam(value = "rotation", required = false) Integer rotation) {
        try {
            if (file == null || file.isEmpty()) {
                throw new ImageUploadException("File is empty");
            }
            String contentType = file.getContentType();
            if (contentType == null) {
                logger.warning("Error uploading image: Content type is null");
                throw new ImageUploadException("File is empty");
            }
            if (!contentType.startsWith("image/")) {
                throw new ImageUploadException("File is not an image");
            }
            if (file.getSize() > ImageService.MAX_UPLOAD_BYTES) {
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("Image is too large");
            }
            CropRequest crop = CropRequest.of(cropX, cropY, cropWidth, cropHeight, rotation);
            String filename = imageService.save(file, crop);
            logger.log(Level.INFO, "Image uploaded: {0}", filename);
            return ResponseEntity.ok(filename);
        } catch (RuntimeException e) {
            logger.warning("Error uploading image: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ResponseEntity<UrlResource> getImage(String imageName) {
        return getImage(imageName, null);
    }

    @GetMapping(value = "/{imageName}")
    public ResponseEntity<UrlResource> getImage(@PathVariable("imageName") String imageName,
            @Parameter(description = "tiny | small | medium | large; omit for the full picture")
            @RequestParam(value = "size", required = false) String size) {
        ImageSize imageSize;
        try {
            imageSize = ImageSize.parse(size);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        try {
            UrlResource resource = imageService.findImage(imageName, imageSize);
            String served = resource.getFilename();
            if (served == null || served.isBlank()) {
                served = imageName;
            }
            return ResponseEntity.ok()
                    .cacheControl(IMMUTABLE)
                    .contentType(MediaType.parseMediaType(ImageService.contentType(served)))
                    .body(resource);
        } catch (ImageNotFoundException e) {
            logger.warning("Error getting image: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

}
