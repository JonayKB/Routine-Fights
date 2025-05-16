package es.iespuertodelacruz.routinefights.comment.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import es.iespuertodelacruz.routinefights.comment.domain.Comment;
import es.iespuertodelacruz.routinefights.comment.domain.ports.secondary.ICommentRepository;
import es.iespuertodelacruz.routinefights.post.domain.Post;
import es.iespuertodelacruz.routinefights.post.domain.ports.secondary.IPostRepository;
import es.iespuertodelacruz.routinefights.user.domain.User;

class CommentServiceTest {

    private static final String POST_ID = "postId";

    private static final String MESSAGE = "message";

    @Mock
    private ICommentRepository commentRepository;

    @Mock
    private IPostRepository postRepository;

    private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commentService = new CommentService(commentRepository, postRepository);
    }

    @Test
    void saveTest() {
        Post post = new Post();
        post.setId(POST_ID);
        when(postRepository.findById(POST_ID)).thenReturn(post);
        Comment expectedComment = new Comment();
        expectedComment.setMessage(MESSAGE);
        expectedComment.setPost(post);
        expectedComment.setUser(new User());
        expectedComment.setCreatedAt(LocalDateTime.now());
        expectedComment.setReplingComment(null);
        when(commentRepository.comment(any(),any(),any(),any())).thenReturn(expectedComment);

        Comment comment = commentService.save(MESSAGE, new User(), POST_ID, null);

        assertNotNull(comment);
        assertEquals(MESSAGE, comment.getMessage());
        assertEquals(post, comment.getPost());
        assertEquals(new User(), comment.getUser());
        assertNotNull(comment.getCreatedAt());
    }

    @Test
    void saveTestWithNullPost() {
        when(postRepository.findById(POST_ID)).thenReturn(null);

        try {
            commentService.save(MESSAGE, new User(), POST_ID, null);
        } catch (IllegalArgumentException e) {
            assertEquals("Post not found", e.getMessage());
        }
    }

    @Test
    void saveTestWithReplingComment() {
        Post post = new Post();
        post.setId(POST_ID);
        when(postRepository.findById(POST_ID)).thenReturn(post);
        Comment replingComment = new Comment();
        replingComment.setId("replingId");
        when(commentRepository.findById("replingId")).thenReturn(replingComment);
        Comment expectedComment = new Comment();
        expectedComment.setMessage(MESSAGE);
        expectedComment.setPost(post);
        expectedComment.setUser(new User());
        expectedComment.setCreatedAt(LocalDateTime.now());
        expectedComment.setReplingComment(replingComment);
        when(commentRepository.comment(any(),any(),any(),any())).thenReturn(expectedComment);

        Comment comment = commentService.save(MESSAGE, new User(), POST_ID, "replingId");

        assertNotNull(comment);
        assertEquals(MESSAGE, comment.getMessage());
        assertEquals(post, comment.getPost());
        assertEquals(new User(), comment.getUser());
        assertNotNull(comment.getCreatedAt());
    }

    @Test
    void findByPostIDTest() {
        Post post = new Post();
        post.setId(POST_ID);
        Comment comment = new Comment();
        comment.setPost(post);
        when(commentRepository.findByPostID(POST_ID)).thenReturn(List.of(comment));

        List<Comment> comments = commentService.findByPostID(POST_ID);

        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals(post, comments.get(0).getPost());
    }

}
