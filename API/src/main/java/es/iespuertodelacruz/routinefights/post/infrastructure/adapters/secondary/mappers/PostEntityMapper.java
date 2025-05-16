package es.iespuertodelacruz.routinefights.post.infrastructure.adapters.secondary.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.mappers.ActivityEntityMapper;
import es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.secondary.mappers.CommentEntityMapper;
import es.iespuertodelacruz.routinefights.post.domain.Post;
import es.iespuertodelacruz.routinefights.post.infrastructure.adapters.secondary.entities.PostEntity;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.mappers.IUserEntityMapper;

@Mapper(componentModel = "spring",
        uses = { IUserEntityMapper.class, CommentEntityMapper.class, ActivityEntityMapper.class })
public interface PostEntityMapper {
    List<PostEntity> toEntity(List<Post> posts);

    List<Post> toDomain(List<PostEntity> postEntities);

    PostEntity toEntity(Post post);

    Post toDomain(PostEntity postEntity);

}
