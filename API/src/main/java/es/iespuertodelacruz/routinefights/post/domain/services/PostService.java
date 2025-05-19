package es.iespuertodelacruz.routinefights.post.domain.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.activity.commons.TimeRates;
import es.iespuertodelacruz.routinefights.activity.domain.Activity;
import es.iespuertodelacruz.routinefights.activity.domain.ports.secondary.IActivityRepository;
import es.iespuertodelacruz.routinefights.post.domain.Post;
import es.iespuertodelacruz.routinefights.post.domain.ports.primary.IPostService;
import es.iespuertodelacruz.routinefights.post.domain.ports.secondary.IPostRepository;
import es.iespuertodelacruz.routinefights.post.exceptions.UploadPostException;
import es.iespuertodelacruz.routinefights.shared.utils.TimeRatesDate;
import es.iespuertodelacruz.routinefights.user.domain.User;

@Service
public class PostService implements IPostService {
    private IPostRepository postRepository;
    private IActivityRepository activityRepository;
    private TimeRatesDate timeRatesDate;

    public PostService(IPostRepository postRepository, IActivityRepository activityRepository) {
        this.postRepository = postRepository;
        this.activityRepository = activityRepository;
        timeRatesDate = new TimeRatesDate();

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
            throw new UploadPostException("Activity not found");
        }
        if (!activityRepository.userIsOnActivity(user.getId(), activityID)) {
            throw new UploadPostException("User is not on activity");
        }

        calculateStreak(user, activityID, post, activity);

        post.setActivity(activity);
        post.setPointsToAdd(getPointsToAdd(activity));

        return postRepository.save(post);
    }

    private void calculateStreak(User user, String activityID, Post post, Activity activity) {
        LocalDateTime[] dates = timeRatesDate.getActualIterationDates(activity.getTimeRate());
        List<Post> actualPeriodPosts = postRepository.getRangeByActivity(activityID, user.getId(), dates[0],
                dates[1]);
        if (actualPeriodPosts.size() >= activity.getTimesRequiered()) {
            throw new UploadPostException("You have already completed this activity");
        }

        if (actualPeriodPosts.isEmpty()) {
            LocalDateTime[] lastDates = timeRatesDate.getLastIterationDates(activity.getTimeRate());

            List<Post> lastPeriodPosts = postRepository.getRangeByActivity(activityID, user.getId(), lastDates[0],
                    lastDates[1]);
            if (lastPeriodPosts.size() >= activity.getTimesRequiered()) {
                post.setStreak(lastPeriodPosts.get(lastPeriodPosts.size() - 1).getStreak() + 1);
            } else {
                post.setStreak(1);
            }
        } else {
            post.setStreak(actualPeriodPosts.get(actualPeriodPosts.size() - 1).getStreak());
        }
    }

    private Integer getPointsToAdd(Activity activity) {
        Integer points;
        switch (activity.getTimeRate()) {
            case TimeRates.DAILY:
                points = 100 / activity.getTimesRequiered();
                break;
            case TimeRates.WEEKLY:
                points = 700 / activity.getTimesRequiered();
                break;
            case TimeRates.MONTHLY:
                points = 3000 / activity.getTimesRequiered();
                break;
            case TimeRates.YEARLY:
                points = 36500 / activity.getTimesRequiered();
                break;
            default:
                points = 0;
                break;
        }
        return points;

    }

    @Override
    public List<Post> getPaginationByUser(LocalDateTime lastDate, int limit, String userEmail) {
        return postRepository.getPaginationByUser(lastDate, limit, userEmail);
    }

    @Override
    public List<Post> getPaginationByActivity(LocalDateTime lastDate, int limit, String activityID) {
        return postRepository.getPaginationByActivity(lastDate, limit, activityID);
    }

    @Override
    public List<Post> getPaginationFollowing(LocalDateTime lastDate, int limit, String userID) {
        return postRepository.getPaginationFollowing(lastDate, limit, userID);
    }

    @Override
    public List<Post> getPaginationSubscribedActivities(LocalDateTime lastDate, int limit, String userID) {
        return postRepository.getPaginationSubscribedActivities(lastDate, limit, userID);
    }

    @Override
    public Set<String> findAllImages() {
        return postRepository.findAllImages();
    }

}
