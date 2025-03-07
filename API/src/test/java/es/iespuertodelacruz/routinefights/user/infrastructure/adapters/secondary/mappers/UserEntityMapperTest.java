package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;
import es.iespuertodelacruz.routinefights.user.utils.UserInitializer;

@SpringBootTest
class UserEntityMapperTest extends UserInitializer {
    @Autowired
    private IUserEntityMapper mapper;

    @Test
    void toDomainTest() {
        userEntity.setFollowing(null);

        User userTest = mapper.toDomain(userEntity);
        assertNotNull(userTest, "Error mapping to domain");
        assertEquals(ID, userTest.getId(), ERROR_MAPPING_PROPERTY + "id");
        assertEquals(USERNAME, userTest.getUsername(), ERROR_MAPPING_PROPERTY + "username");
        assertEquals(EMAIL, userTest.getEmail(), ERROR_MAPPING_PROPERTY + "email");
        assertEquals(PASSWORD, userTest.getPassword(), ERROR_MAPPING_PROPERTY + "password");
        assertEquals(NATIONALITY, userTest.getNationality(), ERROR_MAPPING_PROPERTY + "nationality");
        assertEquals(PHONE_NUMBER, userTest.getPhoneNumber(), ERROR_MAPPING_PROPERTY + "phone_number");
        assertEquals(IMAGE, userTest.getImage(), ERROR_MAPPING_PROPERTY + "image");
        assertEquals(ROLE, userTest.getRole(), ERROR_MAPPING_PROPERTY + "role");
        assertEquals(VERIFIED, userTest.getVerified(), ERROR_MAPPING_PROPERTY + "verified");
        assertEquals(VERIFICATION_TOKEN, userTest.getVerificationToken(),
                ERROR_MAPPING_PROPERTY + "verification_token");
        assertEquals(CREATED_AT, userTest.getCreatedAt(), ERROR_MAPPING_PROPERTY + "created_at");
        assertEquals(UPDATED_AT, userTest.getUpdatedAt(), ERROR_MAPPING_PROPERTY + "updated_at");
        assertEquals(DELETED_AT, userTest.getDeletedAt(), ERROR_MAPPING_PROPERTY + "deleted_at");
        assertEquals(user.getFollowers(), userTest.getFollowers(), ERROR_MAPPING_PROPERTY + "followers");
        assertEquals(user.getFollowing(), userTest.getFollowing(), ERROR_MAPPING_PROPERTY + "following");
    }

    @Test
    void toDomainListTest() {
        UserEntity userEntity2 = new UserEntity();
        UserEntity userEntity3 = new UserEntity();
        List<UserEntity> userEntities = new ArrayList<>(List.of(userEntity, userEntity2, userEntity3));
        List<User> users = mapper.toDomain(userEntities);
        assertNotNull(users, "Error mapping to entity");
        assertEquals(userEntities.size(), users.size(), "Error mapping to entity");
    }

    @Test
    void toEntityTest() {
        UserEntity userEntityTest = mapper.toEntity(user);
        assertNotNull(userEntityTest, "Error mapping to entity");
        assertEquals(ID, userEntityTest.getId(), ERROR_MAPPING_PROPERTY + "id");
        assertEquals(USERNAME, userEntityTest.getUsername(), ERROR_MAPPING_PROPERTY + "username");
        assertEquals(EMAIL, userEntityTest.getEmail(), ERROR_MAPPING_PROPERTY + "email");
        assertEquals(PASSWORD, userEntityTest.getPassword(), ERROR_MAPPING_PROPERTY + "password");
        assertEquals(NATIONALITY, userEntityTest.getNationality(), ERROR_MAPPING_PROPERTY + "nationality");
        assertEquals(PHONE_NUMBER, userEntityTest.getPhoneNumber(), ERROR_MAPPING_PROPERTY + "phone_number");
        assertEquals(IMAGE, userEntityTest.getImage(), ERROR_MAPPING_PROPERTY + "image");
        assertEquals(ROLE, userEntityTest.getRole(), ERROR_MAPPING_PROPERTY + "role");
        assertEquals(VERIFIED, userEntityTest.getVerified(), ERROR_MAPPING_PROPERTY + "verified");
        assertEquals(VERIFICATION_TOKEN, userEntityTest.getVerificationToken(),
                ERROR_MAPPING_PROPERTY + "verification_token");
        assertEquals(CREATED_AT, userEntityTest.getCreatedAt(), ERROR_MAPPING_PROPERTY + "created_at");
        assertEquals(UPDATED_AT, userEntityTest.getUpdatedAt(), ERROR_MAPPING_PROPERTY + "updated_at");
        assertEquals(DELETED_AT, userEntityTest.getDeletedAt(), ERROR_MAPPING_PROPERTY + "deleted_at");
        assertEquals(userEntity.getFollowers(), userEntityTest.getFollowers(), ERROR_MAPPING_PROPERTY + "followers");
        assertEquals(userEntity.getFollowing(), userEntityTest.getFollowing(), ERROR_MAPPING_PROPERTY + "following");
    }

    @Test
    void toEntityListTest() {
        User user2 = new User();
        User user3 = new User();
        List<User> users = new ArrayList<>(List.of(user, user2, user3));
        List<UserEntity> userEntities = mapper.toEntity(users);
        assertNotNull(userEntities, "Error mapping to entity");
        assertEquals(users.size(), userEntities.size(), "Error mapping to entity");
    }
}
