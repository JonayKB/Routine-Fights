package es.iespuertodelacruz.routinefights.post.domain.ports.secondary;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import es.iespuertodelacruz.routinefights.post.domain.Post;

public interface IPostRepository {
    List<Post> getPagination(LocalDateTime lastDate, int limit);
    Post save(Post post);
    List<Post> getRangeByActivity(String activityID, String userID, LocalDateTime startDate, LocalDateTime endDate);
    Integer getRangeCountByActivity(String activityID, String userID, LocalDateTime startDate, LocalDateTime endDate);
    List<Post> getPaginationByUser(LocalDateTime lastDate, int limit, String userEmail);
    List<Post> getPaginationByActivity(LocalDateTime lastDate, int limit, String activityID);
    List<Post> getPaginationFollowing(LocalDateTime lastDate, int limit, String userID);
    List<Post> getPaginationSubscribedActivities(LocalDateTime lastDate, int limit, String userID);
    Set<String> findAllImages();
    Post findById(String postID);
}
