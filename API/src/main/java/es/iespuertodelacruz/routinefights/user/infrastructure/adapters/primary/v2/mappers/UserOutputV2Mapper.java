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
    public UserOutputDTOV2 tOutputDTOV2(User user);

    public List<UserOutputDTOV2> tOutputDTOV2(List<User> users);
}
