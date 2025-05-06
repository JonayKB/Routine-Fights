package es.iespuertodelacruz.routinefights.post.infrastructure.adapters.primary.mappers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import es.iespuertodelacruz.routinefights.post.domain.Post;
import es.iespuertodelacruz.routinefights.post.infrastructure.adapters.primary.v2.dtos.PostOutputDTOV2;
import es.iespuertodelacruz.routinefights.post.infrastructure.adapters.primary.v2.mappers.PostOutputV2Mapper;
import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.dtos.UserOutputDTOV2;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.mappers.UserOutputV2Mapper;


@SpringBootTest
 class PostOutputV2MapperTest {

    private static final String CONSTANT_ID = "test-id";
    private static final String CONSTANT_IMAGE = "test-image.png";
    private static final int CONSTANT_STREAK = 10;
    private static final LocalDateTime CONSTANT_FILED_AT = LocalDateTime.of(2025, 3, 16, 10, 0);
    private static final LocalDateTime CONSTANT_CREATED_AT = LocalDateTime.of(2025, 3, 16, 9, 0);

    @Autowired
    private PostOutputV2Mapper postOutputV2Mapper;

    @MockitoBean
    private UserOutputV2Mapper userOutputV2Mapper;

    @Test
     void testToDto() {
        User dummyUser = new User();
        dummyUser.setUsername("Test User");

        UserOutputDTOV2 expectedUserDto = new UserOutputDTOV2(null, "Test User", null, null, null, null, null, 0, 0, false);
        when(userOutputV2Mapper.toOutputDTOV2(dummyUser)).thenReturn(expectedUserDto);

        Post post = new Post();
        post.setId(CONSTANT_ID);
        post.setImage(CONSTANT_IMAGE);
        post.setStreak(CONSTANT_STREAK);
        post.setFiledAt(CONSTANT_FILED_AT);
        post.setCreatedAt(CONSTANT_CREATED_AT);
        post.setUser(dummyUser);

        PostOutputDTOV2 result = postOutputV2Mapper.toDto(post);

        assertNotNull(result);
        assertEquals(CONSTANT_ID, result.id());
        assertEquals(CONSTANT_IMAGE, result.image());
        assertEquals(CONSTANT_STREAK, result.streak());
        assertEquals(CONSTANT_FILED_AT, result.filedAt());
        assertEquals(CONSTANT_CREATED_AT, result.createdAt());
        assertEquals(expectedUserDto, result.user());
    }

    @Test
     void testToDtoList() {
        User dummyUser = new User();
        dummyUser.setUsername("Test User");

        UserOutputDTOV2 expectedUserDto = new UserOutputDTOV2(null, "Test User", null, null, null, null, null, 0, 0, false);

        when(userOutputV2Mapper.toOutputDTOV2(dummyUser)).thenReturn(expectedUserDto);

        Post post = new Post();
        post.setId(CONSTANT_ID);
        post.setImage(CONSTANT_IMAGE);
        post.setStreak(CONSTANT_STREAK);
        post.setFiledAt(CONSTANT_FILED_AT);
        post.setCreatedAt(CONSTANT_CREATED_AT);
        post.setUser(dummyUser);

        List<Post> posts = Arrays.asList(post);
        List<PostOutputDTOV2> resultList = postOutputV2Mapper.toDto(posts);

        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        PostOutputDTOV2 result = resultList.get(0);
        assertEquals(CONSTANT_ID, result.id());
        assertEquals(CONSTANT_IMAGE, result.image());
        assertEquals(CONSTANT_STREAK, result.streak());
        assertEquals(CONSTANT_FILED_AT, result.filedAt());
        assertEquals(CONSTANT_CREATED_AT, result.createdAt());
        assertEquals(expectedUserDto, result.user());
    }

    @Test
     void testNullPost() {
        assertNull(postOutputV2Mapper.toDto((Post) null));
    }

    @Test
     void testNullPostsList() {
        assertNull(postOutputV2Mapper.toDto((List<Post>) null));
    }
}