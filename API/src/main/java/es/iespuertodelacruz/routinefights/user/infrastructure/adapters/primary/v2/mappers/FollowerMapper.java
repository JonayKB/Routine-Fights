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

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "nationality", source = "user.nationality")
    @Mapping(target = "image", source = "user.image")
    @Mapping(target = "createdAt", source = "user.createdAt")
    @Mapping(target = "followers", expression = "java(user.getFollowers() == null ? 0 : user.getFollowers().size())")
    @Mapping(target = "following", expression = "java(user.getFollowing() == null ? 0 : user.getFollowing().size())")
    @Mapping(target = "isFollowing", expression = "java(searchingUser != null && searchingUser.getFollowing() != null && searchingUser.getFollowing().contains(user))")
    @Mapping(target = "email", source = "user.email")
    public Follower toFollower(User user, User searchingUser);

    public List<Follower> toFollower(List<User> users);

    
    default List<Follower> toFollower(List<User> users, User searchingUser) {
        return users.stream()
                    .map(user -> toFollower(user, searchingUser))
                    .toList();
    }
}
