package es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.primary.contontrollers;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import es.iespuertodelacruz.routinefights.comment.domain.Comment;
import es.iespuertodelacruz.routinefights.comment.domain.ports.primary.ICommentService;
import es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.primary.v2.controllers.CommentControllerV2;
import es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.primary.v2.dtos.CommentInputV2;
import es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.primary.v2.dtos.CommentOutputV2;
import es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.primary.v2.mapper.CommentOutputV2Mapper;
import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.domain.ports.primary.IUserService;

@SpringBootTest
class CommentControllerV2Test {
    private CommentControllerV2 commentController;
    @Mock
    private ICommentService commentService;
    @Mock
    private CommentOutputV2Mapper commentOutputV2Mapper;
    @Mock
    private IUserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commentController = new CommentControllerV2(commentService, commentOutputV2Mapper, userService);
    }

    @Test
    void postCommentTest(){
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(commentService.save(any(), any(), any(), any())).thenReturn(new Comment());
        when(userService.findByEmailOnlyBase(any())).thenReturn(new User());
        when(commentOutputV2Mapper.toDTO(any(Comment.class))).thenReturn(new CommentOutputV2("id","message",LocalDateTime.now(),null));
        CommentOutputV2 commentOutputV2 = commentController.postComment(new CommentInputV2("message", "postID", "replingID"));
        assertNotNull(commentOutputV2);
    }

    @Test
    void getCommentsTest(){
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(commentService.findByPostID(any())).thenReturn(List.of(new Comment()));
        when(commentOutputV2Mapper.toDTO(anyList())).thenReturn(List.of(new CommentOutputV2("id","message",LocalDateTime.now(),null)));
        List<CommentOutputV2> commentOutputV2List = commentController.getComments("postID");
        assertNotNull(commentOutputV2List);
    }


}
