package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
    @Mapping(target = "followers", ignore = true)
    @Mapping(target = "following", ignore = true)
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
}
