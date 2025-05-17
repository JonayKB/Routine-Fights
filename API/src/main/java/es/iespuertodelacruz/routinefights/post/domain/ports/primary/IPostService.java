package es.iespuertodelacruz.routinefights.post.domain.ports.primary;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import es.iespuertodelacruz.routinefights.post.domain.Post;
import es.iespuertodelacruz.routinefights.user.domain.User;

public interface IPostService {

    List<Post> getPagination(LocalDateTime lastDate, int limit);

    Post uploadPost(String image, User user, String activityID);

    List<Post> getPaginationByUser(LocalDateTime lastDate, int limit, String userID);

    List<Post> getPaginationByActivity(LocalDateTime lastDate, int limit, String activityID);

    List<Post> getPaginationFollowing(LocalDateTime lastDate, int limit, String userID);

    List<Post> getPaginationSubscribedActivities(LocalDateTime lastDate, int limit, String userID);

    Set<String> findAllImages();

}
