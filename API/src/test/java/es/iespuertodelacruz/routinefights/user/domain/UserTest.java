package es.iespuertodelacruz.routinefights.user.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import es.iespuertodelacruz.routinefights.user.utils.UserInitializer;

@SpringBootTest
class UserTest extends UserInitializer {
    private static final String ERROR_CREATING_USER = "Error creating user";

    @Test
    void UserNullTest() {
        User userTest = null;
        assertNull(userTest);
    }

    @Test
    void defaultConstructorTest() {
        User userTest = new User();

        userTest.setId(ID);
        userTest.setUsername(USERNAME);
        userTest.setEmail(EMAIL);
        userTest.setPassword(PASSWORD);
        userTest.setNationality(NATIONALITY);
        userTest.setPhoneNumber(PHONE_NUMBER);
        userTest.setImage(IMAGE);
        userTest.setRole(ROLE);
        userTest.setVerified(VERIFIED);
        userTest.setVerificationToken(VERIFICATION_TOKEN);
        userTest.setCreatedAt(CREATED_AT);
        userTest.setUpdatedAt(UPDATED_AT);
        userTest.setDeletedAt(DELETED_AT);
        userTest.setFollowers(followers);
        userTest.setFollowing(following);

        assertNotNull(userTest, "Error creating user without properties");
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
        assertEquals(followers, userTest.getFollowers(), ERROR_MAPPING_PROPERTY + "followers");
        assertEquals(following, userTest.getFollowing(), ERROR_MAPPING_PROPERTY + "following");
    }

    @Test
    void constructorWithPropertiesTest() {
        User userTest = new User(USERNAME, EMAIL, PASSWORD, NATIONALITY, PHONE_NUMBER, IMAGE, ROLE, VERIFIED,
                VERIFICATION_TOKEN, CREATED_AT, UPDATED_AT, DELETED_AT);
        assertNotNull(userTest, ERROR_CREATING_USER);
        assertEquals(USERNAME, userTest.getUsername(), ERROR_CREATING_USER);
        assertEquals(EMAIL, userTest.getEmail(), ERROR_CREATING_USER);
        assertEquals(PASSWORD, userTest.getPassword(), ERROR_CREATING_USER);
        assertEquals(NATIONALITY, userTest.getNationality(), ERROR_CREATING_USER);
        assertEquals(PHONE_NUMBER, userTest.getPhoneNumber(), ERROR_CREATING_USER);
        assertEquals(IMAGE, userTest.getImage(), ERROR_CREATING_USER);
        assertEquals(ROLE, userTest.getRole(), ERROR_CREATING_USER);
        assertEquals(VERIFIED, userTest.getVerified(), ERROR_CREATING_USER);
        assertEquals(VERIFICATION_TOKEN, userTest.getVerificationToken(), ERROR_CREATING_USER);
        assertEquals(CREATED_AT, userTest.getCreatedAt(), ERROR_CREATING_USER);
        assertEquals(UPDATED_AT, userTest.getUpdatedAt(), ERROR_CREATING_USER);
        assertEquals(DELETED_AT, userTest.getDeletedAt(), ERROR_CREATING_USER);
    }

    @Test
    void constructorForV2Test() {
        User userTest = new User(ID, USERNAME, EMAIL, PASSWORD, NATIONALITY, PHONE_NUMBER, IMAGE);
        assertNotNull(userTest, ERROR_CREATING_USER);
        assertEquals(ID, userTest.getId(), ERROR_CREATING_USER);
        assertEquals(USERNAME, userTest.getUsername(), ERROR_CREATING_USER);
        assertEquals(EMAIL, userTest.getEmail(), ERROR_CREATING_USER);
        assertEquals(PASSWORD, userTest.getPassword(), ERROR_CREATING_USER);
        assertEquals(NATIONALITY, userTest.getNationality(), ERROR_CREATING_USER);
        assertEquals(PHONE_NUMBER, userTest.getPhoneNumber(), ERROR_CREATING_USER);
        assertEquals(IMAGE, userTest.getImage(), ERROR_CREATING_USER);
    }

    @Test
    void constructorWithAllFieldsTest() {
        User userTest = new User(ID, USERNAME, EMAIL, PASSWORD, NATIONALITY, PHONE_NUMBER, IMAGE, ROLE, VERIFIED,
                VERIFICATION_TOKEN, CREATED_AT, UPDATED_AT, DELETED_AT, followers, following, null);
        assertNotNull(userTest, ERROR_CREATING_USER);
        assertEquals(USERNAME, userTest.getUsername(), ERROR_CREATING_USER);
        assertEquals(EMAIL, userTest.getEmail(), ERROR_CREATING_USER);
        assertEquals(PASSWORD, userTest.getPassword(), ERROR_CREATING_USER);
        assertEquals(NATIONALITY, userTest.getNationality(), ERROR_CREATING_USER);
        assertEquals(PHONE_NUMBER, userTest.getPhoneNumber(), ERROR_CREATING_USER);
        assertEquals(IMAGE, userTest.getImage(), ERROR_CREATING_USER);
        assertEquals(ROLE, userTest.getRole(), ERROR_CREATING_USER);
        assertEquals(VERIFIED, userTest.getVerified(), ERROR_CREATING_USER);
        assertEquals(VERIFICATION_TOKEN, userTest.getVerificationToken(), ERROR_CREATING_USER);
        assertEquals(CREATED_AT, userTest.getCreatedAt(), ERROR_CREATING_USER);
        assertEquals(UPDATED_AT, userTest.getUpdatedAt(), ERROR_CREATING_USER);
        assertEquals(DELETED_AT, userTest.getDeletedAt(), ERROR_CREATING_USER);
        assertEquals(followers, userTest.getFollowers(), ERROR_CREATING_USER);
        assertEquals(following, userTest.getFollowing(), ERROR_CREATING_USER);
    }

    @Test
    void equalsTest() {
        User userTest = new User();
        userTest.setId(ID);
        assertEquals(user, userTest, ERROR_IN_EQUALS_METHOD);
    }

    @Test
    void equalsSameTest() {
        assertEquals(user, user, ERROR_IN_EQUALS_METHOD);
    }

    @Test
    void equalsDifferentInstanceTest() {
        assertNotEquals(user, userEntity, ERROR_IN_EQUALS_METHOD);
    }

    @Test
    void equalsFalseTest() {
        User userTest = new User();
        userTest.setId("False ID");
        assertNotEquals(user, userTest, ERROR_IN_EQUALS_METHOD);
    }

    @Test
    void toStringTest() {
        User userTest = new User(ID, USERNAME, EMAIL, PASSWORD, NATIONALITY, EMAIL, IMAGE, ROLE, VERIFIED,
                VERIFICATION_TOKEN, CREATED_AT, UPDATED_AT, DELETED_AT, followers, following, null);
        assertEquals(user.toString(), userTest.toString(), "Error in toString method");
    }

    @Test
    void hashCodeTest() {
        User userTest = new User(ID, USERNAME, EMAIL, PASSWORD, NATIONALITY, EMAIL, IMAGE, ROLE, VERIFIED,
                VERIFICATION_TOKEN, CREATED_AT, UPDATED_AT, DELETED_AT, followers, following, null);
        assertEquals(user.hashCode(), userTest.hashCode(), "Error in hashCode method");
    }
}
