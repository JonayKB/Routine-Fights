package es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.secondary.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import es.iespuertodelacruz.routinefights.comment.domain.Comment;
import es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.secondary.entities.CommentEntity;
import es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.secondary.mappers.CommentEntityMapper;
import es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.secondary.repositories.ICommentEntityRepository;
import es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.secondary.services.CommentEntityService;

class CommentEntityServiceTest {
    private CommentEntityService commentEntityService;
    @Mock
    private ICommentEntityRepository commentRepository;
    @Mock
    private CommentEntityMapper commentEntityMapper;
    private Comment comment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commentEntityService = new CommentEntityService(commentRepository, commentEntityMapper);
        comment = new Comment();
    }

    @Test
    void commentTest() {
        when(commentEntityMapper.toEntity(any(Comment.class))).thenReturn(new CommentEntity());
        when(commentRepository.save(any())).thenReturn(new CommentEntity());
        when(commentEntityMapper.toDomain(any(CommentEntity.class))).thenReturn(comment);
        Comment savedComment = commentEntityService.comment(comment);
        assertNotNull(savedComment);
    }

    @Test
    void findByPostIDTest() {
        when(commentEntityMapper.toDomain(any(CommentEntity.class))).thenReturn(comment);
        when(commentRepository.findByPostId(any())).thenReturn(List.of(new CommentEntity()));
        List<Comment> comments = commentEntityService.findByPostID("postId");
        assertNotNull(comments);
    }
    @Test
    void findByIdTest() {
        when(commentEntityMapper.toDomain(any(CommentEntity.class))).thenReturn(comment);
        when(commentRepository.findById(any())).thenReturn(java.util.Optional.of(new CommentEntity()));
        Comment foundComment = commentEntityService.findById("commentId");
        assertNotNull(foundComment);
    }

}
