package es.iespuertodelacruz.routinefights.post.infrastructure.adapters.secondary.services;

import java.time.LocalDateTime;
import java.util.List;

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
        return postEntityMapper.toDomain(postEntityRepository.save(postEntityMapper.toEntity(post)));
    }

}
