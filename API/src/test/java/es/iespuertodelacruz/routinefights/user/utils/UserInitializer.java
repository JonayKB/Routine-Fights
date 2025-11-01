package es.iespuertodelacruz.routinefights.user.utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;

import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;

public abstract class UserInitializer {
    protected static final String ERROR_MAPPING_PROPERTY = "Error mapping property: ";
    protected static final String ERROR_IN_EQUALS_METHOD = "Error in equals method";

    protected static final String ID = "id";
    protected static final String USERNAME = "username";
    protected static final String EMAIL = "email@email.com";
    protected static final String PASSWORD = "password";
    protected static final String NATIONALITY = "nationality";
    protected static final String PHONE_NUMBER = "+34648742361";
    protected static final String IMAGE = "image";
    protected static final String ROLE = "role";
    protected static final boolean VERIFIED = false;
    protected static final String VERIFICATION_TOKEN = "verification_token";
    protected static final LocalDateTime CREATED_AT = LocalDateTime.now();
    protected static final LocalDateTime UPDATED_AT = LocalDateTime.now();
    protected static final LocalDateTime DELETED_AT = LocalDateTime.now();
    protected static final List<User> followers = new ArrayList<>();
    protected static final List<User> following = new ArrayList<>();
    protected static final List<UserEntity> followersEntity = new ArrayList<>();
    protected static final List<UserEntity> followingEntity = new ArrayList<>();

    protected User user;
    protected User follower;
    protected UserEntity userEntity;

    @BeforeEach
    void setUp() {
        user = new User(ID, USERNAME, EMAIL, PASSWORD, NATIONALITY, PHONE_NUMBER, IMAGE, ROLE, VERIFIED,
                VERIFICATION_TOKEN,
                CREATED_AT, UPDATED_AT, DELETED_AT, followers, following, null);
        follower = new User("followerId", "followerUsername", "followerEmail", "followerPassword",
                "followerNationality",
                "followerPhoneNumber", "followerImage", "followerRole", true, "followerVerificationToken",
                LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), new ArrayList<>(), new ArrayList<>(), null);
        followers.add(follower);

        userEntity = new UserEntity(ID, USERNAME, EMAIL, PASSWORD, NATIONALITY, PHONE_NUMBER, IMAGE, ROLE, VERIFIED,
                VERIFICATION_TOKEN, CREATED_AT, UPDATED_AT, DELETED_AT, followersEntity, followingEntity, null);
        followersEntity.add(new UserEntity());
    }
}
