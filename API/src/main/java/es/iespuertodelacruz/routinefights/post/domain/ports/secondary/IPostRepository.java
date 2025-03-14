package es.iespuertodelacruz.routinefights.post.domain.ports.secondary;

import java.time.LocalDateTime;
import java.util.List;

import es.iespuertodelacruz.routinefights.post.domain.Post;

public interface IPostRepository {
    List<Post> getPagination(LocalDateTime lastDate, int limit);
    Post save(Post post);
}
