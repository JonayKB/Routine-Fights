package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import es.iespuertodelacruz.routinefights.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions.UserDeleteException;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions.UserNotFoundException;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions.UserSaveException;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.exceptions.UserUpdateException;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.mappers.IUserEntityMapper;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.repositories.IUserEntityRepository;
import es.iespuertodelacruz.routinefights.user.utils.UserInitializer;

@SpringBootTest
class UserEntityServiceTest extends UserInitializer {
    private static final String TEST_EXCEPTION = "Test Exception";

    private UserEntityService userEntityService;

    @Mock
    private IUserEntityRepository userEntityRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private IUserEntityMapper userEntityMapper;

    @BeforeEach
    void init() {
        userEntityService = new UserEntityService();
        userEntityService.setUserRepository(userEntityRepository);
        userEntityService.setPasswordEncoder(passwordEncoder);
        userEntityService.setUserEntityMapper(userEntityMapper);
    }

    @Test
    void getPasswordEncoderTest() {
        assertNotNull(userEntityService.getPasswordEncoder());
    }

    @Test
    void getUserEntityMapperTest() {
        assertNotNull(userEntityService.getUserEntityMapper());
    }

    @Test
    void getUserRepositoryTest() {
        assertNotNull(userEntityService.getUserRepository());
    }

    @Test
    void deleteTest() {
        assertTrue(userEntityService.delete("id"));
    }

    @Test
    void deleteExceptionTest() {
        when(userEntityRepository.existsById(anyString())).thenThrow(new UserDeleteException(TEST_EXCEPTION));

        UserDeleteException exception = assertThrows(UserDeleteException.class, () -> {
            userEntityService.delete("id");
        });
        assertEquals("Error deleting user", exception.getMessage());
    }

    @Test
    void existsByEmailTest() {
        when(userEntityRepository.existsByEmail(anyString())).thenReturn(true);
        assertTrue(userEntityService.existsByEmail("email"));
    }

    @Test
    void findAllTest() {
        when(userEntityRepository.findAll()).thenReturn(new ArrayList<UserEntity>());
        when(userEntityMapper.toDomain(anyList())).thenReturn(new ArrayList<User>());
        assertNotNull(userEntityService.findAll());
    }

    @Test
    void findAllExceptionTest() {
        when(userEntityRepository.findAll()).thenThrow(new UserNotFoundException(TEST_EXCEPTION));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userEntityService.findAll();
        });
        assertEquals("Users not found", exception.getMessage());
    }

    @Test
    void findAllImagesTest() {
        when(userEntityRepository.findAllImages()).thenReturn(new ArrayList<String>());
        assertNotNull(userEntityService.findAllImages());
    }

    @Test
    void findAllImagesExceptionTest() {
        when(userEntityRepository.findAllImages()).thenThrow(new UserNotFoundException(TEST_EXCEPTION));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userEntityService.findAllImages();
        });
        assertEquals("Error finding images", exception.getMessage());
    }

    @Test
    void findByEmailTest() {
        when(userEntityRepository.findByEmail(anyString())).thenReturn(new UserEntity());
        when(userEntityMapper.toDomain(any(UserEntity.class))).thenReturn(user);
        assertNotNull(userEntityService.findByEmail("email"));
    }

    @Test
    void findByEmailExceptionTest() {
        when(userEntityRepository.findByEmail(anyString())).thenThrow(UserNotFoundException.class);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userEntityService.findByEmail("email");
        });
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void findByIdTest() {
        when(userEntityRepository.findById(anyString())).thenReturn(Optional.of(new UserEntity()));
        when(userEntityMapper.toDomain(any(UserEntity.class))).thenReturn(user);
        assertNotNull(userEntityService.findById("id"));
    }

    @Test
    void findByIdExceptionTest() {
        when(userEntityRepository.findById(anyString())).thenThrow(new UserNotFoundException(TEST_EXCEPTION));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userEntityService.findById("id");
        });
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void findFollowedUsersByEmailTest() {
        when(userEntityRepository.findFollowedUsersByEmail(anyString())).thenReturn(new ArrayList<UserEntity>());
        assertNotNull(userEntityService.findFollowedUsersByEmail("email"));
    }

    @Test
    void findFollowedUsersByEmailExceptionTest() {
        when(userEntityRepository.findFollowedUsersByEmail(anyString()))
                .thenThrow(new UserNotFoundException(TEST_EXCEPTION));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userEntityService.findFollowedUsersByEmail("email");
        });
        assertEquals("Followed users not found", exception.getMessage());
    }

    @Test
    void findFollowersByEmailTest() {
        when(userEntityRepository.findFollowersByEmail(anyString())).thenReturn(new ArrayList<UserEntity>());
        assertNotNull(userEntityService.findFollowersByEmail("email"));
    }

    @Test
    void findFollowersByEmailExceptionTest() {
        when(userEntityRepository.findFollowersByEmail(anyString()))
                .thenThrow(new UserNotFoundException(TEST_EXCEPTION));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userEntityService.findFollowersByEmail("email");
        });
        assertEquals("Followers not found", exception.getMessage());
    }

    @Test
    void followUserTest() {
        when(userEntityRepository.existsByEmail(anyString())).thenReturn(true);
        when(userEntityRepository.followUser(anyString(), anyString())).thenReturn(true);
        Boolean followed = userEntityService.followUser("frEmail", "fdEmail");
        assertTrue(followed);
    }

    @Test
    void followUserNotFoundTest() {
        when(userEntityRepository.existsByEmail(anyString())).thenReturn(false);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userEntityService.followUser("frEmail", "fdEmail");
        });
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void followUserExceptionTest() {
        when(userEntityRepository.existsByEmail(anyString())).thenReturn(true);
        when(userEntityRepository.followUser(anyString(), anyString()))
                .thenThrow(new UserNotFoundException(TEST_EXCEPTION));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userEntityService.followUser("frEmail", "fdEmail");
        });
        assertEquals("Error following user", exception.getMessage());
    }

    @Test
    void postTest() {
        when(userEntityRepository.save(any(UserEntity.class))).thenReturn(new UserEntity());
        when(userEntityMapper.toEntity(any(User.class))).thenReturn(userEntity);
        when(userEntityMapper.toDomain(any(UserEntity.class))).thenReturn(user);
        assertNotNull(userEntityService.post(user));
    }

    @Test
    void postNullUserTest() {
        UserSaveException exception = assertThrows(UserSaveException.class, () -> {
            userEntityService.post(null);
        });
        assertEquals("Data required", exception.getMessage());
    }

    @Test
    void postExistingUserTest() {
        when(userEntityRepository.existsByEmail(anyString())).thenReturn(true);
        UserSaveException exception = assertThrows(UserSaveException.class, () -> {
            userEntityService.post(user);
        });
        assertEquals("Email already exists", exception.getMessage());
    }

    @Test
    void postExceptionTest() {
        when(userEntityRepository.save(any(UserEntity.class))).thenThrow(new UserSaveException(TEST_EXCEPTION));
        when(userEntityMapper.toEntity(any(User.class))).thenReturn(userEntity);

        UserSaveException exception = assertThrows(UserSaveException.class, () -> {
            userEntityService.post(user);
        });
        assertEquals("Error saving user", exception.getMessage());
    }

    @Test
    void putTest() {
        when(userEntityRepository.save(any(UserEntity.class))).thenReturn(new UserEntity());
        when(userEntityRepository.findById(anyString())).thenReturn(Optional.of(userEntity));
        when(userEntityMapper.toDomain(any(UserEntity.class))).thenReturn(user);
        assertNotNull(userEntityService.put(user));
    }

    @Test
    void putNullUserTest() {
        UserUpdateException exception = assertThrows(UserUpdateException.class, () -> {
            userEntityService.put(null);
        });
        assertEquals("Data required", exception.getMessage());
    }

    @Test
    void putUserNotFoundTest() {
        when(userEntityRepository.findById(anyString())).thenReturn(Optional.empty());
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userEntityService.put(user);
        });
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void putPasswordEncoderTest() {
        when(userEntityRepository.findById(anyString())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.encode(anyString())).thenReturn("password2");
        when(userEntityRepository.save(any(UserEntity.class))).thenReturn(new UserEntity());
        when(userEntityMapper.toDomain(any(UserEntity.class))).thenReturn(user);

        user.setPassword("password2");
        assertEquals(user, userEntityService.put(user));
    }

    @Test
    void putExceptionTest() {
        when(userEntityRepository.findById(anyString())).thenReturn(Optional.of(userEntity));
        when(userEntityRepository.save(any(UserEntity.class))).thenThrow(new UserUpdateException(TEST_EXCEPTION));

        UserUpdateException exception = assertThrows(UserUpdateException.class, () -> {
            userEntityService.put(user);
        });
        assertEquals("Error updating user", exception.getMessage());
    }

    @Test
    void unfollowUserTest() {
        when(userEntityRepository.existsByEmail(anyString())).thenReturn(true);
        when(userEntityRepository.unfollowUser(anyString(), anyString())).thenReturn(true);
        Boolean unfollowed = userEntityService.unfollowUser("frEmail", "fdEmail");
        assertTrue(unfollowed);
    }

    @Test
    void unfollowUserNotFoundTest() {
        when(userEntityRepository.existsByEmail(anyString())).thenReturn(false);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userEntityService.unfollowUser("frEmail", "fdEmail");
        });
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void unfollowUserExceptionTest() {
        when(userEntityRepository.existsByEmail(anyString())).thenReturn(true);
        when(userEntityRepository.unfollowUser(anyString(), anyString()))
                .thenThrow(new UserNotFoundException(TEST_EXCEPTION));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userEntityService.unfollowUser("frEmail", "fdEmail");
        });
        assertEquals("Error unfollowing user", exception.getMessage());
    }

    @Test
    void findByUsernameTest() {
        when(userEntityRepository.findByUsername(anyString())).thenReturn(new ArrayList<UserEntity>());
        when(userEntityMapper.toDomain(anyList())).thenReturn(new ArrayList<User>());
        assertNotNull(userEntityService.findByUsername("username"));
    }

    @Test
    void restoreTest() {
        when(userEntityRepository.findById(anyString())).thenReturn(Optional.of(new UserEntity()));
        when(userEntityRepository.save(any(UserEntity.class))).thenReturn(new UserEntity());
        assertTrue(userEntityService.restore("id"));
    }

    @Test
    void restoreUserNotFoundExceptionTest() {
        when(userEntityRepository.findById(anyString())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userEntityService.restore("id");
        });
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void restoreUserUpdateExceptionTest() {
        when(userEntityRepository.findById(anyString())).thenReturn(Optional.of(new UserEntity()));
        when(userEntityRepository.save(any(UserEntity.class))).thenThrow(new UserUpdateException(TEST_EXCEPTION));

        UserUpdateException exception = assertThrows(UserUpdateException.class, () -> {
            userEntityService.restore("id");
        });
        assertEquals("Error updating user", exception.getMessage());
    }

    @Test
    void softdeleteTest() {
        when(userEntityRepository.findById(anyString())).thenReturn(Optional.of(new UserEntity()));
        when(userEntityRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        assertTrue(userEntityService.softDelete("id"));
    }

    @Test
    void softDeleteUserNotFoundExceptionTest() {
        when(userEntityRepository.findById(anyString())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userEntityService.softDelete("id");
        });
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void softDeleteUserDeleteExceptionTest() {
        when(userEntityRepository.findById(anyString())).thenReturn(Optional.of(new UserEntity()));
        when(userEntityRepository.save(any(UserEntity.class))).thenThrow(new UserDeleteException(TEST_EXCEPTION));

        UserDeleteException exception = assertThrows(UserDeleteException.class, () -> {
            userEntityService.softDelete("id");
        });
        assertEquals("Error deleting user", exception.getMessage());
    }

    @Test
    void updateTest() {
        when(userEntityRepository.findById(anyString())).thenReturn(Optional.of(userEntity));
        when(userEntityRepository.save(any(UserEntity.class))).thenReturn(new UserEntity());
        when(userEntityMapper.toDomain(any(UserEntity.class))).thenReturn(user);
        assertNotNull(userEntityService.update(user));
    }
}
