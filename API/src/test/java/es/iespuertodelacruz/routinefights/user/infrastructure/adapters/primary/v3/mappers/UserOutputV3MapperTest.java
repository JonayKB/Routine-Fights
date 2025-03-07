package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v3.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v3.dtos.UserOutputDTOV3;
import es.iespuertodelacruz.routinefights.user.utils.UserInitializer;

@SpringBootTest
class UserOutputV3MapperTest extends UserInitializer {
    @Autowired
    private UserOutputV3Mapper mapper;

    @Test
    void toOutputDTOV3Test() {
        UserOutputDTOV3 userOutputDTOV3 = mapper.tOutputDTOV3(user);
        assertNotNull(userOutputDTOV3, "Error mapping to output DTO");
        assertEquals(ID, userOutputDTOV3.id(), ERROR_MAPPING_PROPERTY + "id");
        assertEquals(USERNAME, userOutputDTOV3.username(), ERROR_MAPPING_PROPERTY + "username");
        assertEquals(EMAIL, userOutputDTOV3.email(), ERROR_MAPPING_PROPERTY + "email");
        assertEquals(NATIONALITY, userOutputDTOV3.nationality(), ERROR_MAPPING_PROPERTY + "nationality");
        assertEquals(PHONE_NUMBER, userOutputDTOV3.phoneNumber(), ERROR_MAPPING_PROPERTY + "phoneNumber");
        assertEquals(IMAGE, userOutputDTOV3.image(), ERROR_MAPPING_PROPERTY + "image");
        assertEquals(ROLE, userOutputDTOV3.role(), ERROR_MAPPING_PROPERTY + "role");
        assertEquals(VERIFIED, userOutputDTOV3.verified(), ERROR_MAPPING_PROPERTY + "verified");
        assertEquals(VERIFICATION_TOKEN, userOutputDTOV3.verificationToken(), ERROR_MAPPING_PROPERTY + "verificationToken");
        assertEquals(CREATED_AT, userOutputDTOV3.createdAt(), ERROR_MAPPING_PROPERTY + "createdAt");
        assertEquals(UPDATED_AT, userOutputDTOV3.updatedAt(), ERROR_MAPPING_PROPERTY + "updatedAt");
        assertEquals(DELETED_AT, userOutputDTOV3.deletedAt(), ERROR_MAPPING_PROPERTY + "deletedAt");
        assertEquals(user.getFollowers().size(), userOutputDTOV3.followers().size(), ERROR_MAPPING_PROPERTY + "followers");
        assertEquals(user.getFollowing().size(), userOutputDTOV3.following().size(), ERROR_MAPPING_PROPERTY + "following");
    }

    @Test
    void toOutputDTOV3ListTest() {
        User user2 = new User();
        User user3 = new User();
        List<User> users = new ArrayList<>(List.of(user2, user3));
        List<UserOutputDTOV3> usersOutputV3 = mapper.tOutputDTOV3(users);
        assertNotNull(users, "Error mapping to entity");
        assertEquals(users.size(), usersOutputV3.size(), "Error mapping to entity");
    }
}
