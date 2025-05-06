package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.dtos.UserOutputDTOV2;

/**
 * UserOutputV2Mapper
 */
@Mapper(componentModel = "spring")
public interface UserOutputV2Mapper {
    @Mapping(target = "followers", expression = "java(user.getFollowers() == null ? 0 : user.getFollowers().size())")
    @Mapping(target = "following", expression = "java(user.getFollowing() == null ? 0 : user.getFollowing().size())")
    public UserOutputDTOV2 toOutputDTOV2(User user);

    @Mapping(target = "followers", expression = "java(user.getFollowers() == null ? 0 : user.getFollowers().size())")
    @Mapping(target = "following", expression = "java(user.getFollowing() == null ? 0 : user.getFollowing().size())")
    @Mapping(target = "isFollowing", expression = "java(searchingUser != null && user.getFollowers() != null && user.getFollowers().contains(searchingUser))")
    @Mapping(target = "image", source = "user.image")
    @Mapping(target = "createdAt", source = "user.createdAt")
    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "phoneNumber", source = "user.phoneNumber")
    @Mapping(target = "nationality", source = "user.nationality")
    public UserOutputDTOV2 toOutputDTOV2(User user, User searchingUser);

    public List<UserOutputDTOV2> toOutputDTOV2(List<User> users);
}
