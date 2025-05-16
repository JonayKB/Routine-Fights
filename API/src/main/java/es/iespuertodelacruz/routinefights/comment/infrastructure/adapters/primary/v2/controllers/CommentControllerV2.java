package es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.primary.v2.controllers;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import es.iespuertodelacruz.routinefights.comment.domain.ports.primary.ICommentService;
import es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.primary.v2.dtos.CommentInputV2;
import es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.primary.v2.dtos.CommentOutputV2;
import es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.primary.v2.mapper.CommentOutputV2Mapper;
import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.domain.ports.primary.IUserService;

@Controller
@CrossOrigin
public class CommentControllerV2 {

    private ICommentService commentService;
    private CommentOutputV2Mapper commentOutputV2Mapper;
    private IUserService userService;

    public CommentControllerV2(ICommentService commentService,
            CommentOutputV2Mapper commentOutputV2Mapper, IUserService userService) {
        this.commentService = commentService;
        this.commentOutputV2Mapper = commentOutputV2Mapper;
        this.userService = userService;
    }

    @Secured({ "ROLE_USER","ROLE_ADMIN" })
    @MutationMapping("postComment")
    public CommentOutputV2 postComment(@Argument("commentInput") CommentInputV2 commentInput) {
        User user = userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return commentOutputV2Mapper.toDTO(commentService.save(commentInput.message(), user,
                commentInput.postID(), commentInput.replingID()));
    }

    @Secured({ "ROLE_USER","ROLE_ADMIN" })
    @QueryMapping("getComments")
    public List<CommentOutputV2> getComments(@Argument("postID") String postID) {
        return commentOutputV2Mapper.toDTO(commentService.findByPostID(postID));
    }

}
