package es.iespuertodelacruz.routinefights.post.infrastructure.adapters.secondary.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.post.domain.Post;
import es.iespuertodelacruz.routinefights.post.domain.ports.secondary.IPostRepository;
import es.iespuertodelacruz.routinefights.post.infrastructure.adapters.secondary.mappers.PostEntityMapper;
import es.iespuertodelacruz.routinefights.post.infrastructure.adapters.secondary.repositories.IPostEntityRepository;

@Service
public class PostEntityService implements IPostRepository {
    private IPostEntityRepository postEntityRepository;
    private PostEntityMapper postEntityMapper;

    public PostEntityService(IPostEntityRepository postEntityRepository, PostEntityMapper postEntityMapper) {
        this.postEntityRepository = postEntityRepository;
        this.postEntityMapper = postEntityMapper;
    }

    @Override
    public List<Post> getPagination(LocalDateTime lastDate, int limit) {
        return postEntityMapper.toDomain(postEntityRepository.getPagination(lastDate, limit));
    }

    @Override
    public Post save(Post post) {
        return postEntityMapper.toDomain(postEntityRepository.create(post.getImage(), post.getPointsToAdd(),
                post.getCreatedAt(), post.getStreak(), post.getActivity().getId(), post.getUser().getId()));
    }

    @Override
    public List<Post> getRangeByActivity(String activityID, String userID, LocalDateTime startDate,
            LocalDateTime endDate) {
        return postEntityMapper
                .toDomain(postEntityRepository.getRangeByActivity(activityID, userID, startDate, endDate));
    }

    @Override
    public Integer getRangeCountByActivity(String activityID, String userID, LocalDateTime startDate,
            LocalDateTime endDate) {
        return postEntityRepository.getRangeCountByActivity(activityID, userID, startDate, endDate);
    }

    @Override
    public List<Post> getPaginationByUser(LocalDateTime lastDate, int limit, String userID) {
        return postEntityMapper.toDomain(postEntityRepository.getPaginationByUser(lastDate, limit, userID));
    }

    @Override
    public List<Post> getPaginationByActivity(LocalDateTime lastDate, int limit, String activityID) {
        return postEntityMapper.toDomain(postEntityRepository.getPaginationByActivity(lastDate, limit, activityID));
    }

    @Override
    public List<Post> getPaginationFollowing(LocalDateTime lastDate, int limit, String userID) {
        return postEntityMapper.toDomain(postEntityRepository.getPaginationFollowing(lastDate, limit, userID));
    }

    @Override
    public List<Post> getPaginationSubscribedActivities(LocalDateTime lastDate, int limit, String userID) {
        return postEntityMapper
                .toDomain(postEntityRepository.getPaginationSubscribedActivities(lastDate, limit, userID));
    }

    @Override
    public Set<String> findAllImages() {
        return postEntityRepository.findAllImages();
    }

    @Override
    public Post findById(String id) {
        return postEntityMapper.toDomain(postEntityRepository.findById(id).orElse(null));
    }

}
