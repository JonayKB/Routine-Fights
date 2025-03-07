package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.mappers;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;

/**
 * IUserEntityMapper interface for UserEntityMapper
 */
@Mapper(componentModel = "spring")
public interface IUserEntityMapper {

    /**
     * Method to map User to UserEntity
     * 
     * @param user User
     * @return UserEntity
     */
    UserEntity toEntity(User user);

    /**
     * Method to map UserEntity to User
     * 
     * @param userEntity UserEntity
     * @return User
     */
    @Mapping(target = "followers", source = "followers", qualifiedByName = "setFollows")
    @Mapping(target = "following", source = "following", qualifiedByName = "setFollows")
    public User toDomain(UserEntity userEntity);

    /**
     * Method to map List<User> to List<UserEntity>
     * 
     * @param users List<User>
     * @return List<UserEntity>
     */
    public List<UserEntity> toEntity(List<User> users);

    /**
     * Method to map List<UserEntity> to List<User>
     * 
     * @param userEntities List<UserEntity>
     * @return List<User>
     */
    public List<User> toDomain(List<UserEntity> userEntities);

    @Named("setFollows")
    public static List<User> setFollows(List<UserEntity> users) {
        List<User> userList = new ArrayList<>();
        for (UserEntity userEntity : users) {
            User user = new User();
            user.setCreatedAt(userEntity.getCreatedAt());
            user.setDeletedAt(userEntity.getDeletedAt());
            user.setEmail(userEntity.getEmail());
            user.setImage(userEntity.getImage());
            user.setNationality(userEntity.getNationality());
            user.setPassword(userEntity.getPassword());
            user.setPhoneNumber(userEntity.getPhoneNumber());
            user.setRole(userEntity.getRole());
            user.setUpdatedAt(userEntity.getUpdatedAt());
            user.setUsername(userEntity.getUsername());
            user.setVerificationToken(userEntity.getVerificationToken());
            user.setVerified(userEntity.getVerified());
            user.setId(userEntity.getId());
            userList.add(user);
        }
        return userList;
    }
}
