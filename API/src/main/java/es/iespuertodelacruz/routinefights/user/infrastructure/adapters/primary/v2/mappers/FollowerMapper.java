package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.dtos.Follower;

/**
 * FollowerMapper
 */
@Mapper(componentModel = "spring")
public interface FollowerMapper {
    @Mapping(target = "followers", expression = "java(user.getFollowers() == null ? 0 : user.getFollowers().size())")
    @Mapping(target = "following", expression = "java(user.getFollowing() == null ? 0 : user.getFollowing().size())")
    public Follower toFollower(User user);

    public List<Follower> toFollower(List<User> users);
}
