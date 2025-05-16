package es.iespuertodelacruz.routinefights.post.infrastructure.adapters.primary.v2.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import es.iespuertodelacruz.routinefights.post.domain.Post;
import es.iespuertodelacruz.routinefights.post.domain.ports.primary.IPostService;
import es.iespuertodelacruz.routinefights.post.infrastructure.adapters.primary.v2.dtos.PostOutputDTOV2;
import es.iespuertodelacruz.routinefights.post.infrastructure.adapters.primary.v2.mappers.PostOutputV2Mapper;
import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.domain.ports.primary.IUserService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = PostControllerV2.class)
class PostControllerV2Test {

    private static final String DUMMY_EMAIL = "dummy@example.com";
    private static final String IMAGE = "dummyImage";
    private static final String ACTIVITY_ID = "activity1";
    private static final String USER_ID = "user1";
    private static final String LAST_DATE_STRING = "2025-04-01T00:00:00";
    private static final int LIMIT = 10;

    @MockitoBean
    private IPostService postService;

    @MockitoBean
    private PostOutputV2Mapper postOutputV2Mapper;

    @MockitoBean
    private IUserService userService;

    @Autowired
    private PostControllerV2 postController;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testGetPaginationOK() {
        User user = new User();
        user.setId(USER_ID);
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(DUMMY_EMAIL, null));
        LocalDateTime date = LocalDateTime.parse(LAST_DATE_STRING);
        Post post = new Post();
        List<Post> posts = Arrays.asList(post);
        PostOutputDTOV2 dto = new PostOutputDTOV2(ACTIVITY_ID, IMAGE, null, null, null, null, null, null, null);

        List<PostOutputDTOV2> dtoList = Arrays.asList(dto);
        when(userService.findByEmailOnlyBase(DUMMY_EMAIL)).thenReturn(user);
        when(postService.getPagination(date, LIMIT)).thenReturn(posts);
        when(postOutputV2Mapper.toDto(posts,user)).thenReturn(dtoList);

        List<PostOutputDTOV2> response = postController.getPagination(LAST_DATE_STRING, LIMIT);
        assertEquals(dtoList, response);
    }

    @Test
    void testUploadPostOK() {
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(DUMMY_EMAIL, null));
        User user = new User();
        user.setId(USER_ID);
        Post post = new Post();
        PostOutputDTOV2 dto = new PostOutputDTOV2(ACTIVITY_ID, IMAGE, null, null, null, null, null, null, null);

        when(userService.findByEmailOnlyBase(DUMMY_EMAIL)).thenReturn(user);
        when(postService.uploadPost(IMAGE, user, ACTIVITY_ID)).thenReturn(post);
        when(postOutputV2Mapper.toDto(post)).thenReturn(dto);

        PostOutputDTOV2 response = postController.uploadPost(IMAGE, ACTIVITY_ID);
        assertEquals(dto, response);
    }

    @Test
    void testGetPaginationByUserOK() {
        User user = new User();
        user.setId(USER_ID);
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(DUMMY_EMAIL, null));
        LocalDateTime date = LocalDateTime.parse(LAST_DATE_STRING);
        Post post = new Post();
        List<Post> posts = Arrays.asList(post);
        PostOutputDTOV2 dto = new PostOutputDTOV2(ACTIVITY_ID, IMAGE, null, date, date, null, null, null, null);

        List<PostOutputDTOV2> dtoList = Arrays.asList(dto);
        when(userService.findByEmailOnlyBase(anyString())).thenReturn(user);
        when(postService.getPaginationByUser(date, LIMIT, USER_ID)).thenReturn(posts);
        when(postOutputV2Mapper.toDto(posts,user)).thenReturn(dtoList);

        List<PostOutputDTOV2> response = postController.getPaginationByUser(LAST_DATE_STRING, LIMIT, USER_ID);
        assertEquals(dtoList, response);
    }

    @Test
    void testGetPaginationByActivityOK() {
        User user = new User();
        user.setId(USER_ID);
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(DUMMY_EMAIL, null));
        LocalDateTime date = LocalDateTime.parse(LAST_DATE_STRING);
        Post post = new Post();
        List<Post> posts = Arrays.asList(post);
        PostOutputDTOV2 dto = new PostOutputDTOV2(ACTIVITY_ID, IMAGE, null, date, date, null, null, null, null);

        List<PostOutputDTOV2> dtoList = Arrays.asList(dto);
        when(userService.findByEmailOnlyBase(anyString())).thenReturn(user);
        when(postService.getPaginationByActivity(date, LIMIT, ACTIVITY_ID)).thenReturn(posts);
        when(postOutputV2Mapper.toDto(posts,user)).thenReturn(dtoList);

        List<PostOutputDTOV2> response = postController.getPaginationByActivity(LAST_DATE_STRING, LIMIT, ACTIVITY_ID);
        assertEquals(dtoList, response);
    }

    @Test
    void testGetPaginationFollowingOK() {
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(DUMMY_EMAIL, null));
        User user = new User();
        user.setId(USER_ID);
        LocalDateTime date = LocalDateTime.parse(LAST_DATE_STRING);
        Post post = new Post();
        List<Post> posts = Arrays.asList(post);
        PostOutputDTOV2 dto = new PostOutputDTOV2(ACTIVITY_ID, IMAGE, null, date, date, null, null, null, null);
        List<PostOutputDTOV2> dtoList = Arrays.asList(dto);

        when(userService.findByEmailOnlyBase(DUMMY_EMAIL)).thenReturn(user);
        when(postService.getPaginationFollowing(date, LIMIT, USER_ID)).thenReturn(posts);
        when(postOutputV2Mapper.toDto(posts,user)).thenReturn(dtoList);

        List<PostOutputDTOV2> response = postController.getPaginationFollowing(LAST_DATE_STRING, LIMIT);
        assertEquals(dtoList, response);
    }

    @Test
    void testGetPaginationSubscribedActivitiesOK() {
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(DUMMY_EMAIL, null));
        User user = new User();
        user.setId(USER_ID);
        LocalDateTime date = LocalDateTime.parse(LAST_DATE_STRING);
        Post post = new Post();
        List<Post> posts = Arrays.asList(post);
        PostOutputDTOV2 dto = new PostOutputDTOV2(ACTIVITY_ID, IMAGE, null, date, date, null, null, null, null);
        List<PostOutputDTOV2> dtoList = Arrays.asList(dto);

        when(userService.findByEmailOnlyBase(anyString())).thenReturn(user);
        when(postService.getPaginationSubscribedActivities(date, LIMIT, USER_ID)).thenReturn(posts);
        when(postOutputV2Mapper.toDto(posts, user)).thenReturn(dtoList);

        List<PostOutputDTOV2> response = postController.getPaginationSubscribedActivities(LAST_DATE_STRING, LIMIT);
        assertEquals(dtoList, response);
    }
}
