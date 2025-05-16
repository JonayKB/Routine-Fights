package es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.secondary.services;

import java.util.List;

import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.comment.domain.Comment;
import es.iespuertodelacruz.routinefights.comment.domain.ports.secondary.ICommentRepository;
import es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.secondary.entities.CommentEntity;
import es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.secondary.mappers.CommentEntityMapper;
import es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.secondary.repositories.ICommentEntityRepository;
import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.repositories.IUserEntityRepository;

@Service
public class CommentEntityService implements ICommentRepository {

    private ICommentEntityRepository commentRepository;
    private CommentEntityMapper commentEntityMapper;
    private IUserEntityRepository userRepository;

    public CommentEntityService(ICommentEntityRepository commentRepository, CommentEntityMapper commentEntityMapper,
            IUserEntityRepository userRepository) {
        this.userRepository = userRepository;
        this.commentEntityMapper = commentEntityMapper;
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment comment(Comment comment) {
        CommentEntity commentEntity = commentEntityMapper.toEntity(comment);

        return commentEntityMapper.toDomain(commentRepository.save(commentEntity));
    }

    @Override
    public List<Comment> findByPostID(String postID) {
        return commentEntityMapper.toDomain(commentRepository.findByPostId(postID));
    }

    @Override
    public Comment findById(String id) {
        return commentEntityMapper.toDomain(commentRepository.findById(id).orElse(null));
    }

}
