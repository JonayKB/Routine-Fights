package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;

/**
 * IUserEntityMapper interface for UserEntityMapper
 */
@Mapper
public interface IUserEntityMapper {
    IUserEntityMapper INSTANCE = Mappers.getMapper(IUserEntityMapper.class);

    /**
     * Method to map User to UserEntity
     * 
     * @param user User
     * @return UserEntity
     */
    public UserEntity userToUserEntity(User user);

    /**
     * Method to map UserEntity to User
     * 
     * @param userEntity UserEntity
     * @return User
     */
    public User userEntityToUser(UserEntity userEntity);

    /**
     * Method to map List<User> to List<UserEntity>
     * 
     * @param users List<User>
     * @return List<UserEntity>
     */
    public List<UserEntity> usersToUserEntities(List<User> users);

    /**
     * Method to map List<UserEntity> to List<User>
     * 
     * @param userEntities List<UserEntity>
     * @return List<User>
     */
    public List<User> userEntitiesToUsers(List<UserEntity> userEntities);
}
