package es.iespuertodelacruz.routinefights.post.infrastructure.adapters.primary.v2.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import es.iespuertodelacruz.routinefights.comment.domain.Comment;
import es.iespuertodelacruz.routinefights.post.domain.Post;
import es.iespuertodelacruz.routinefights.post.infrastructure.adapters.primary.v2.dtos.PostOutputDTOV2;
import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.mappers.UserOutputV2Mapper;

@Mapper(componentModel = "spring", uses = { UserOutputV2Mapper.class })
public interface PostOutputV2Mapper {

    @Named("mapLikedByToLikes")
    default Integer mapLikedByToLikes(List<User> likedBy) {
        return likedBy == null ? 0 : likedBy.size();
    }

    @Named("mapCommentsToComments")
    default Integer mapCommentsToComments(List<Comment> comments) {
        return comments == null ? 0 : comments.size();
    }

    @Mapping(target = "comments", source = "post.comments", qualifiedByName = "mapCommentsToComments")
    PostOutputDTOV2 toDto(Post post);

    @Mapping(target = "user", source = "post.user")
    @Mapping(target = "likes", source = "post.likedBy", qualifiedByName = "mapLikedByToLikes")
    @Mapping(target = "id", source = "post.id")
    @Mapping(target = "comments", source = "post.comments", qualifiedByName = "mapCommentsToComments")
    @Mapping(target = "isLiked", expression = "java(post.getLikedBy() != null && post.getLikedBy().contains(user))")
    @Mapping(target = "image", source = "post.image")
    @Mapping(target = "createdAt", source = "post.createdAt")
    PostOutputDTOV2 toDto(Post post, User user);

    List<PostOutputDTOV2> toDto(List<Post> posts);

    default List<PostOutputDTOV2> toDto(List<Post> posts, User user) {
        return posts.stream()
                .map(post -> toDto(post, user))
                .toList();
    }

}
