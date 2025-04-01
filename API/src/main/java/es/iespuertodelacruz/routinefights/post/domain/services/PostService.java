package es.iespuertodelacruz.routinefights.post.domain.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.activity.commons.TimeRates;
import es.iespuertodelacruz.routinefights.activity.domain.Activity;
import es.iespuertodelacruz.routinefights.activity.domain.ports.secondary.IActivityRepository;
import es.iespuertodelacruz.routinefights.post.domain.Post;
import es.iespuertodelacruz.routinefights.post.domain.ports.primary.IPostService;
import es.iespuertodelacruz.routinefights.post.domain.ports.secondary.IPostRepository;
import es.iespuertodelacruz.routinefights.post.exceptions.UploadPostException;
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
        LocalDateTime startOfActualPeriod;
        LocalDateTime endOfActualPeriod;

        switch (activity.getTimeRate()) {
            case TimeRates.DAILY:
                startOfActualPeriod = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
                endOfActualPeriod = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);
                break;
            case TimeRates.WEEKLY:
                startOfActualPeriod = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
                        .with(java.time.DayOfWeek.MONDAY);
                endOfActualPeriod = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999)
                        .with(java.time.DayOfWeek.SUNDAY);
                break;
            case TimeRates.MONTHLY:
                startOfActualPeriod = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
                        .withDayOfMonth(1);
                endOfActualPeriod = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999)
                        .withDayOfMonth(LocalDateTime.now().toLocalDate().lengthOfMonth());
                break;
            case TimeRates.YEARLY:
                startOfActualPeriod = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
                        .withDayOfYear(1);
                endOfActualPeriod = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999)
                        .withDayOfYear(LocalDateTime.now().toLocalDate().lengthOfYear());
                break;
            default:
                throw new UploadPostException("Invalid time rate");
        }

        List<Post> actualPeriodPosts = postRepository.getRangeByActivity(activityID, user.getId(), startOfActualPeriod,
                endOfActualPeriod);
        if (actualPeriodPosts.size() >= activity.getTimesRequiered()) {
            throw new UploadPostException("You have already completed this activity");
        }

        if (actualPeriodPosts.isEmpty()) {
            LocalDateTime startOfPeriod;
            LocalDateTime endOfPeriod;
            switch (activity.getTimeRate()) {
                case TimeRates.DAILY:
                    startOfPeriod = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
                            .minusDays(1);
                    endOfPeriod = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999)
                            .minusDays(1);
                    break;
                case TimeRates.WEEKLY:
                    startOfPeriod = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
                            .minusWeeks(1).with(java.time.DayOfWeek.MONDAY);
                    endOfPeriod = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999)
                            .minusWeeks(1).with(java.time.DayOfWeek.SUNDAY);
                    break;
                case TimeRates.MONTHLY:
                    startOfPeriod = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
                            .minusMonths(1).withDayOfMonth(1);
                    endOfPeriod = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999)
                            .minusMonths(1)
                            .withDayOfMonth(LocalDateTime.now().minusMonths(1).toLocalDate().lengthOfMonth());
                    break;
                case TimeRates.YEARLY:
                    startOfPeriod = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
                            .minusYears(1).withDayOfYear(1);
                    endOfPeriod = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999)
                            .minusYears(1)
                            .withDayOfYear(LocalDateTime.now().minusYears(1).toLocalDate().lengthOfYear());
                    break;
                default:
                    throw new UploadPostException("Invalid time rate");
            }

            List<Post> lastPeriodPosts = postRepository.getRangeByActivity(activityID, user.getId(), startOfPeriod,
                    endOfPeriod);
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
    public List<Post> getPaginationByUser(LocalDateTime lastDate, int limit, String userID) {
        return postRepository.getPaginationByUser(lastDate, limit, userID);
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



}
