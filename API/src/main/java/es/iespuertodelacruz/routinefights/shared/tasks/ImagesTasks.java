package es.iespuertodelacruz.routinefights.shared.tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import es.iespuertodelacruz.routinefights.shared.services.ImageService;
import es.iespuertodelacruz.routinefights.user.domain.ports.primary.IUserService;

@Component
public class ImagesTasks {
    Logger logger = Logger.getLogger(ImagesTasks.class.getName());
    private ImageService imageService;
    private IUserService userService;

    public ImagesTasks(ImageService imageService, IUserService userService) {
        this.imageService = imageService;
        this.userService = userService;
    }

    @Scheduled(cron = "0 0 3 * * MON")
    void deleteNotUsedImages() {
        List<String> images = new ArrayList<>(imageService.getAll());
        userService.findAllImages().forEach(images::remove);
        logger.log(Level.INFO, "Deleted not used images: {0} images going to be deleted", images.size());
        images.forEach(image -> imageService.delete(image));
        logger.log(Level.INFO, "Deleted images executed successfully at {0}", LocalDateTime.now());
    }

}
