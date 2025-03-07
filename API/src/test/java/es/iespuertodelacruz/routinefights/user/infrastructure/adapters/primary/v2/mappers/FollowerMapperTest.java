package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.dtos.Follower;
import es.iespuertodelacruz.routinefights.user.utils.UserInitializer;

@SpringBootTest
class FollowerMapperTest extends UserInitializer {
    @Autowired
    private FollowerMapper mapper;

    @Test
    void toFollowerTest() {
        Follower follower = mapper.toFollower(user);
        assertNotNull(follower, "Error mapping to follower");
        assertEquals(ID, follower.id(), ERROR_MAPPING_PROPERTY + "id");
        assertEquals(USERNAME, follower.username(), ERROR_MAPPING_PROPERTY + "username");
        assertEquals(NATIONALITY, follower.nationality(), ERROR_MAPPING_PROPERTY + "nationality");
        assertEquals(IMAGE, follower.image(), ERROR_MAPPING_PROPERTY + "image");
        assertEquals(CREATED_AT, follower.createdAt(), ERROR_MAPPING_PROPERTY + "createdAt");
        assertEquals(user.getFollowers().size(), follower.followers(), ERROR_MAPPING_PROPERTY + "followers");
        assertEquals(user.getFollowing().size(), follower.following(), ERROR_MAPPING_PROPERTY + "following");
    }

    @Test
    void toFollowerListTest() {
        User user2 = new User();
        User user3 = new User();
        List<User> users = List.of(user2, user3);
        List<Follower> followers = mapper.toFollower(users);
        assertNotNull(followers, "Error mapping to followers");
        assertEquals(users.size(), followers.size(), "Error mapping to followers");
    }
}
