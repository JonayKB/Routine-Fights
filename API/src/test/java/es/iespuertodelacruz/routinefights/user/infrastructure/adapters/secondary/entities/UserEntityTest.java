package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import es.iespuertodelacruz.routinefights.user.utils.UserInitializer;

@SpringBootTest
class UserEntityTest extends UserInitializer {
    private static final String ERROR_CREATING_USER = "Error creating user";

    @Test
    void UserNullTest() {
        UserEntity userTest = null;
        assertNull(userTest);
    }

    @Test
    void defaultConstructorTest() {
        UserEntity userEntityTest = new UserEntity();

        userEntityTest.setId(ID);
        userEntityTest.setUsername(USERNAME);
        userEntityTest.setEmail(EMAIL);
        userEntityTest.setPassword(PASSWORD);
        userEntityTest.setNationality(NATIONALITY);
        userEntityTest.setPhoneNumber(PHONE_NUMBER);
        userEntityTest.setImage(IMAGE);
        userEntityTest.setRole(ROLE);
        userEntityTest.setVerified(VERIFIED);
        userEntityTest.setVerificationToken(VERIFICATION_TOKEN);
        userEntityTest.setCreatedAt(CREATED_AT);
        userEntityTest.setUpdatedAt(UPDATED_AT);
        userEntityTest.setDeletedAt(DELETED_AT);
        userEntityTest.setFollowers(followersEntity);
        userEntityTest.setFollowing(followingEntity);

        assertNotNull(userEntityTest, "Error creating user without properties");
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
        assertEquals(followersEntity, userEntityTest.getFollowers(), ERROR_MAPPING_PROPERTY + "followers");
        assertEquals(followingEntity, userEntityTest.getFollowing(), ERROR_MAPPING_PROPERTY + "following");
    }

    @Test
    void constructorWithAllFieldsTest() {
        UserEntity userEntityTest = new UserEntity(ID, USERNAME, EMAIL, PASSWORD, NATIONALITY, PHONE_NUMBER, IMAGE, ROLE,
                VERIFIED, VERIFICATION_TOKEN, CREATED_AT, UPDATED_AT, DELETED_AT, followersEntity, followingEntity);
        assertNotNull(userEntityTest, ERROR_CREATING_USER);
        assertEquals(USERNAME, userEntityTest.getUsername(), ERROR_CREATING_USER);
        assertEquals(EMAIL, userEntityTest.getEmail(), ERROR_CREATING_USER);
        assertEquals(PASSWORD, userEntityTest.getPassword(), ERROR_CREATING_USER);
        assertEquals(NATIONALITY, userEntityTest.getNationality(), ERROR_CREATING_USER);
        assertEquals(PHONE_NUMBER, userEntityTest.getPhoneNumber(), ERROR_CREATING_USER);
        assertEquals(IMAGE, userEntityTest.getImage(), ERROR_CREATING_USER);
        assertEquals(ROLE, userEntityTest.getRole(), ERROR_CREATING_USER);
        assertEquals(VERIFIED, userEntityTest.getVerified(), ERROR_CREATING_USER);
        assertEquals(VERIFICATION_TOKEN, userEntityTest.getVerificationToken(), ERROR_CREATING_USER);
        assertEquals(CREATED_AT, userEntityTest.getCreatedAt(), ERROR_CREATING_USER);
        assertEquals(UPDATED_AT, userEntityTest.getUpdatedAt(), ERROR_CREATING_USER);
        assertEquals(DELETED_AT, userEntityTest.getDeletedAt(), ERROR_CREATING_USER);
        assertEquals(followersEntity, userEntityTest.getFollowers(), ERROR_CREATING_USER);
        assertEquals(followingEntity, userEntityTest.getFollowing(), ERROR_CREATING_USER);
    }

    @Test
    void equalsTest() {
        UserEntity userEntityTest = new UserEntity();
        userEntityTest.setId(ID);
        assertEquals(userEntity, userEntityTest, "Error in equals method");
    }

    @Test
    void equalsFalseTest() {
        UserEntity userEntityTest = new UserEntity();
        userEntityTest.setId("False ID");
        assertNotEquals(userEntity, userEntityTest, "Error in equals method");
    }

    @Test
    void toStringTest() {
        UserEntity userEntityTest = new UserEntity(ID, USERNAME, EMAIL, PASSWORD, NATIONALITY, EMAIL, IMAGE, ROLE, VERIFIED,
                VERIFICATION_TOKEN, CREATED_AT, UPDATED_AT, DELETED_AT, followersEntity, followingEntity);
        assertEquals(userEntity.toString(), userEntityTest.toString(), "Error in toString method");
    }

    @Test
    void hashCodeTest() {
        UserEntity userEntityTest = new UserEntity(ID, USERNAME, EMAIL, PASSWORD, NATIONALITY, EMAIL, IMAGE, ROLE, VERIFIED,
                VERIFICATION_TOKEN, CREATED_AT, UPDATED_AT, DELETED_AT, followersEntity, followingEntity);
        assertEquals(userEntity.hashCode(), userEntityTest.hashCode(), "Error in hashCode method");
    }
}
