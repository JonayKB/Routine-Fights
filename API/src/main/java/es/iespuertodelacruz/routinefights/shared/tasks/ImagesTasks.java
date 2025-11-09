package es.iespuertodelacruz.routinefights.shared.tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import es.iespuertodelacruz.routinefights.activity.domain.ports.primary.IActivityService;
import es.iespuertodelacruz.routinefights.community_event.domain.ports.primary.ICommunityEventService;
import es.iespuertodelacruz.routinefights.post.domain.ports.primary.IPostService;
import es.iespuertodelacruz.routinefights.shared.services.ImageService;
import es.iespuertodelacruz.routinefights.user.domain.ports.primary.IUserService;

@Component
public class ImagesTasks {
    Logger logger = Logger.getLogger(ImagesTasks.class.getName());
    private ImageService imageService;
    private IUserService userService;
    private IPostService postService;
    private ICommunityEventService communityEventService;
    private IActivityService activityService;

    public ImagesTasks(ImageService imageService, IUserService userService, IPostService postService,
            ICommunityEventService communityEventService, IActivityService activityService) {
        this.communityEventService = communityEventService;
        this.imageService = imageService;
        this.userService = userService;
        this.postService = postService;
        this.activityService = activityService;
    }

    @Scheduled(cron = "0 0 3 * * MON")
    /**
     * Delete not used images
     * 
     * @return List of images deleted
     */
    List<String> deleteNotUsedImages() {
        List<String> images = new ArrayList<>(imageService.getAll());
        userService.findAllImages().forEach(images::remove);
        postService.findAllImages().forEach(images::remove);
        communityEventService.findAllImages().forEach(images::remove);
        activityService.findAllImages().forEach(images::remove);

        // ADD ACTIVITY IMAGES
        // CHANGE TO SET
        logger.log(Level.INFO, "Deleted not used images: {0} images going to be deleted", images.size());
        images.forEach(image -> imageService.delete(image));
        logger.log(Level.INFO, "Deleted images executed successfully at {0}", LocalDateTime.now());
        return images;
    }

}
