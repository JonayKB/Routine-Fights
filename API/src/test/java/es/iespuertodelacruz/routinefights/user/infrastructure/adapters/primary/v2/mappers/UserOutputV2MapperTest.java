package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.dtos.UserOutputDTOV2;
import es.iespuertodelacruz.routinefights.user.utils.UserInitializer;

@SpringBootTest
class UserOutputV2MapperTest extends UserInitializer {
    @Autowired
    private UserOutputV2Mapper mapper;

    @Test
    void toOutputDTOV2Test() {
        UserOutputDTOV2 userOutputDTOV2 = mapper.toOutputDTOV2(user);
        assertNotNull(userOutputDTOV2, "Error mapping to output DTO");
        assertEquals(ID, userOutputDTOV2.id(), ERROR_MAPPING_PROPERTY + "id");
        assertEquals(USERNAME, userOutputDTOV2.username(), ERROR_MAPPING_PROPERTY + "username");
        assertEquals(EMAIL, userOutputDTOV2.email(), ERROR_MAPPING_PROPERTY + "email");
        assertEquals(NATIONALITY, userOutputDTOV2.nationality(), ERROR_MAPPING_PROPERTY + "nationality");
        assertEquals(PHONE_NUMBER, userOutputDTOV2.phoneNumber(), ERROR_MAPPING_PROPERTY + "phoneNumber");
        assertEquals(IMAGE, userOutputDTOV2.image(), ERROR_MAPPING_PROPERTY + "image");
        assertEquals(CREATED_AT, userOutputDTOV2.createdAt(), ERROR_MAPPING_PROPERTY + "createdAt");
        assertEquals(user.getFollowers().size(), userOutputDTOV2.followers(), ERROR_MAPPING_PROPERTY + "followers");
        assertEquals(user.getFollowing().size(), userOutputDTOV2.following(), ERROR_MAPPING_PROPERTY + "following");
    }

    @Test
    void toOutputDTOV2ListTest() {
        User user2 = new User();
        User user3 = new User();
        List<User> users = new ArrayList<>(List.of(user2, user3));
        List<UserOutputDTOV2> usersOutputV2 = mapper.toOutputDTOV2(users);
        assertNotNull(users, "Error mapping to entity");
        assertEquals(users.size(), usersOutputV2.size(), "Error mapping to entity");
    }

    @Test
    void toOutputDTOV2WithSearchingUserTest() {
        UserOutputDTOV2 userOutputDTOV2 = mapper.toOutputDTOV2(user, follower);
        assertNotNull(userOutputDTOV2, "Error mapping to output DTO");
        assertEquals(ID, userOutputDTOV2.id(), ERROR_MAPPING_PROPERTY + "id");
        assertEquals(USERNAME, userOutputDTOV2.username(), ERROR_MAPPING_PROPERTY + "username");
        assertEquals(EMAIL, userOutputDTOV2.email(), ERROR_MAPPING_PROPERTY + "email");
        assertEquals(NATIONALITY, userOutputDTOV2.nationality(), ERROR_MAPPING_PROPERTY + "nationality");
        assertEquals(PHONE_NUMBER, userOutputDTOV2.phoneNumber(), ERROR_MAPPING_PROPERTY + "phoneNumber");
        assertEquals(IMAGE, userOutputDTOV2.image(), ERROR_MAPPING_PROPERTY + "image");
        assertEquals(CREATED_AT, userOutputDTOV2.createdAt(), ERROR_MAPPING_PROPERTY + "createdAt");
        assertEquals(user.getFollowers().size(), userOutputDTOV2.followers(), ERROR_MAPPING_PROPERTY + "followers");
        assertEquals(user.getFollowing().size(), userOutputDTOV2.following(), ERROR_MAPPING_PROPERTY + "following");
        assertEquals(true, userOutputDTOV2.isFollowing(), ERROR_MAPPING_PROPERTY + "isFollowing");
    }

    @Test
    void toOutputDTOV2WithSearchingUserListTest() {
        User user2 = new User();
        User user3 = new User();
        List<User> users = new ArrayList<>(List.of(user2, user3));
        List<UserOutputDTOV2> usersOutputV2 = mapper.toOutputDTOV2(users, user);
        assertNotNull(users, "Error mapping to entity");
        assertEquals(users.size(), usersOutputV2.size(), "Error mapping to entity");
    }
}
