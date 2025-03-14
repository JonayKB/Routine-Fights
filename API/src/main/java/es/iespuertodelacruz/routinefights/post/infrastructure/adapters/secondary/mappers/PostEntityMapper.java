package es.iespuertodelacruz.routinefights.post.infrastructure.adapters.secondary.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import es.iespuertodelacruz.routinefights.post.domain.Post;
import es.iespuertodelacruz.routinefights.post.infrastructure.adapters.secondary.entities.PostEntity;

@Mapper(componentModel = "spring")
public interface PostEntityMapper {
    List<PostEntity> toEntity(List<Post> posts);

    List<Post> toDomain(List<PostEntity> postEntities);

    PostEntity toEntity(Post post);

    Post toDomain(PostEntity postEntity);

}
