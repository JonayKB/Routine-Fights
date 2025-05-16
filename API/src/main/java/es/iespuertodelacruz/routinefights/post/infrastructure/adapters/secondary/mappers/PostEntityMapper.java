package es.iespuertodelacruz.routinefights.post.infrastructure.adapters.secondary.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.mappers.ActivityEntityMapper;
import es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.secondary.mappers.CommentEntityMapper;
import es.iespuertodelacruz.routinefights.comment.domain.Comment;
import es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.secondary.entities.CommentEntity;
import es.iespuertodelacruz.routinefights.post.domain.Post;
import es.iespuertodelacruz.routinefights.post.infrastructure.adapters.secondary.entities.PostEntity;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.mappers.IUserEntityMapper;

@Mapper(componentModel = "spring",
        uses = { IUserEntityMapper.class, CommentEntityMapper.class, ActivityEntityMapper.class })
public interface PostEntityMapper {
    List<PostEntity> toEntity(List<Post> posts);

    List<Post> toDomain(List<PostEntity> postEntities);

    PostEntity toEntity(Post post);

    @Mapping(target = "comments", source = "postEntity.comments", qualifiedByName = "mapCommentsToComments")
    Post toDomain(PostEntity postEntity);

    @Named("mapCommentsToComments")
    default List<Comment> mapCommentsToComments(List<CommentEntity> commentsEntities) {
        if (commentsEntities == null) {
            return List.of();
        }
        return commentsEntities.stream()
                .map(commentEntity -> {
                    Comment comment = new Comment();
                    comment.setId(commentEntity.getId());
                    comment.setMessage(commentEntity.getMessage());
                    comment.setCreatedAt(commentEntity.getCreatedAt());
                    comment.setDeletedAt(commentEntity.getDeletedAt());

                    return comment;
                })
                .toList();
    }

}
