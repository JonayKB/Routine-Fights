package es.iespuertodelacruz.routinefights.post.infrastructure.adapters.primary.v2.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import es.iespuertodelacruz.routinefights.post.domain.Post;
import es.iespuertodelacruz.routinefights.post.domain.ports.primary.IPostService;
import es.iespuertodelacruz.routinefights.post.infrastructure.adapters.primary.v2.dtos.PostOutputDTOV2;
import es.iespuertodelacruz.routinefights.post.infrastructure.adapters.primary.v2.mappers.PostOutputV2Mapper;
import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.domain.ports.primary.IUserService;

/**
 * PostControllerV2
 */
@Controller
@CrossOrigin
public class PostControllerV2 {
    Logger logger = Logger.getLogger(PostControllerV2.class.getName());

    private IPostService postService;
    private PostOutputV2Mapper postOutputV2Mapper;
    private IUserService userService;

    public PostControllerV2(IPostService postService, PostOutputV2Mapper postOutputV2Mapper, IUserService userService) {
        this.postService = postService;
        this.postOutputV2Mapper = postOutputV2Mapper;
        this.userService = userService;
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @QueryMapping("postsV2")
    public List<PostOutputDTOV2> getPagination(@Argument String lastDate, @Argument int limit) {
        LocalDateTime date = LocalDateTime.parse(lastDate);
        List<Post> posts = postService.getPagination(date, limit);
        User user = userService.findByEmailOnlyBase(SecurityContextHolder.getContext().getAuthentication().getName());
        return postOutputV2Mapper.toDto(posts, user);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @MutationMapping("uploadPost")
    public PostOutputDTOV2 uploadPost(@Argument String image, @Argument String activityID) {

        User user = userService.findByEmailOnlyBase(SecurityContextHolder.getContext().getAuthentication().getName());

        Post post = postService.uploadPost(image, user, activityID);
        return postOutputV2Mapper.toDto(post);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @QueryMapping("postsByUserV2")
    public List<PostOutputDTOV2> getPaginationByUser(@Argument String lastDate, @Argument int limit,
            @Argument String userID) {
        LocalDateTime date = LocalDateTime.parse(lastDate);
        List<Post> posts = postService.getPaginationByUser(date, limit, userID);
        User user = userService.findByEmailOnlyBase(SecurityContextHolder.getContext().getAuthentication().getName());
        return postOutputV2Mapper.toDto(posts, user);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @QueryMapping("postsByActivityV2")
    public List<PostOutputDTOV2> getPaginationByActivity(@Argument String lastDate, @Argument int limit,
            @Argument String activityID) {
        LocalDateTime date = LocalDateTime.parse(lastDate);
        List<Post> posts = postService.getPaginationByActivity(date, limit, activityID);
        User user = userService.findByEmailOnlyBase(SecurityContextHolder.getContext().getAuthentication().getName());
        return postOutputV2Mapper.toDto(posts, user);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @QueryMapping("postsFollowingV2")
    public List<PostOutputDTOV2> getPaginationFollowing(@Argument String lastDate, @Argument int limit) {
        User user = userService.findByEmailOnlyBase(SecurityContextHolder.getContext().getAuthentication().getName());
        LocalDateTime date = LocalDateTime.parse(lastDate);
        List<Post> posts = postService.getPaginationFollowing(date, limit, user.getId());
        return postOutputV2Mapper.toDto(posts, user);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @QueryMapping("postsSubscribedActivitiesV2")
    public List<PostOutputDTOV2> getPaginationSubscribedActivities(@Argument String lastDate, @Argument int limit) {
        User user = userService.findByEmailOnlyBase(SecurityContextHolder.getContext().getAuthentication().getName());
        LocalDateTime date = LocalDateTime.parse(lastDate);
        List<Post> posts = postService.getPaginationSubscribedActivities(date, limit, user.getId());
        return postOutputV2Mapper.toDto(posts, user);
    }

}
