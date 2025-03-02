package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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

import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.mappers.IUserEntityMapper;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.repositories.IUserEntityRepository;
import es.iespuertodelacruz.routinefights.user.utils.UserInitializer;

@SpringBootTest
class UserEntityServiceTest extends UserInitializer {
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
    void existsByEmailTest() {
        when(userEntityRepository.existsByEmail(anyString())).thenReturn(true);
        assertTrue(userEntityService.existsByEmail("email"));
    }

    @Test
    void findAllTest() {
        when(userEntityRepository.findAll()).thenReturn(new ArrayList<UserEntity>());
        assertNotNull(userEntityService.findAll());
    }

    @Test
    void findAllImagesTest() {
        when(userEntityRepository.findAllImages()).thenReturn(new ArrayList<String>());
        assertNotNull(userEntityService.findAllImages());
    }

    @Test
    void findByEmailTest() {
        when(userEntityRepository.findByEmail(anyString())).thenReturn(new UserEntity());
        when(userEntityMapper.toDomain(any(UserEntity.class))).thenReturn(user);
        assertNotNull(userEntityService.findByEmail("email"));
    }

    @Test
    void findByIdTest() {
        when(userEntityRepository.findById(anyString())).thenReturn(Optional.of(new UserEntity()));
        when(userEntityMapper.toDomain(any(UserEntity.class))).thenReturn(user);
        assertNotNull(userEntityService.findById("id"));
    }

    @Test
    void findFollowedUsersByEmailTest() {
        when(userEntityRepository.findFollowedUsersByEmail(anyString())).thenReturn(new ArrayList<UserEntity>());
        assertNotNull(userEntityService.findFollowedUsersByEmail("email"));
    }

    @Test
    void findFollowersByEmailTest() {
        when(userEntityRepository.findFollowersByEmail(anyString())).thenReturn(new ArrayList<UserEntity>());
        assertNotNull(userEntityService.findFollowersByEmail("email"));
    }

    @Test
    void followUserTest() {
        when(userEntityRepository.followUser(anyString(), anyString())).thenReturn(true);
        Boolean followed = userEntityService.followUser("frEmail", "fdEmail");
        assertTrue(followed);
    }

    @Test
    void postTest() {
        when(userEntityRepository.save(any(UserEntity.class))).thenReturn(new UserEntity());
        when(userEntityMapper.toEntity(any(User.class))).thenReturn(userEntity);
        when(userEntityMapper.toDomain(any(UserEntity.class))).thenReturn(user);
        assertNotNull(userEntityService.post(user));
    }

    @Test
    void putTest() {
        when(userEntityRepository.save(any(UserEntity.class))).thenReturn(new UserEntity());
        when(userEntityRepository.findById(anyString())).thenReturn(Optional.of(userEntity));
        when(userEntityMapper.toDomain(any(UserEntity.class))).thenReturn(user);
        assertNotNull(userEntityService.put(user));
    }

    @Test
    void unfollowUserTest() {
        when(userEntityRepository.unfollowUser(anyString(), anyString())).thenReturn(true);
        Boolean unfollowed = userEntityService.unfollowUser("frEmail", "fdEmail");
        assertTrue(unfollowed);
    }
}
