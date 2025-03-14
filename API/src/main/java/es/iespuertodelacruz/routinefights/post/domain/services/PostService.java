package es.iespuertodelacruz.routinefights.post.domain.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.activity.domain.Activity;
import es.iespuertodelacruz.routinefights.activity.domain.ports.secondary.IActivityRepository;
import es.iespuertodelacruz.routinefights.post.domain.Post;
import es.iespuertodelacruz.routinefights.post.domain.ports.primary.IPostService;
import es.iespuertodelacruz.routinefights.post.domain.ports.secondary.IPostRepository;
import es.iespuertodelacruz.routinefights.user.domain.User;

@Service
public class PostService implements IPostService {
    private IPostRepository postRepository;
    private IActivityRepository activityRepository;

    public PostService(IPostRepository postRepository, IActivityRepository activityRepository) {
        this.postRepository = postRepository;
        this.activityRepository = activityRepository;
    }

    @Override
    public List<Post> getPagination(LocalDateTime lastDate, int limit) {
        return postRepository.getPagination(lastDate, limit);
    }

    @Override
    public Post uploadPost(String image, User user, String activityID) {
        Post post = new Post();
        post.setImage(image);
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());
        Activity activity = activityRepository.findById(activityID);
        if (activity == null) {
            throw new RuntimeException("Activity not found");
        }
        //TODO LIMIT POINTS
        //TODO GET STREAK
        post.setActivity(activity);
        post.setPointsToAdd(getPointsToAdd(activity));

        return postRepository.save(post);
    }

    private Integer getPointsToAdd(Activity activity) {
        Integer points;
        switch (activity.getTimeRate()) {
            case "daily":
                points = 100 / activity.getTimesRequiered();
                break;
            case "weekly":
                points = 700 / activity.getTimesRequiered();
                break;
            case "monthly":
                points = 3000 / activity.getTimesRequiered();
                break;
            default:
                points = 0;
                break;
        }
        return points;

    }

}
