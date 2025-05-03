package es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.secondary.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import es.iespuertodelacruz.routinefights.comment.domain.Comment;
import es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.secondary.entities.CommentEntity;
import es.iespuertodelacruz.routinefights.post.infrastructure.adapters.secondary.mappers.PostEntityMapper;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.mappers.IUserEntityMapper;

@Mapper(componentModel = "spring",
        uses = {IUserEntityMapper.class, PostEntityMapper.class})
public interface CommentEntityMapper {

    CommentEntity toEntity(Comment comment);

    List<CommentEntity> toEntity(List<Comment> comment);

    Comment toDomain(CommentEntity commentEntity);

    List<Comment> toDomain(List<CommentEntity> commentEntity);

    
}
