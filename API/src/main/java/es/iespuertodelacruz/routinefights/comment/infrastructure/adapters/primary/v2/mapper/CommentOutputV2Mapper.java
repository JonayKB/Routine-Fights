package es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.primary.v2.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import es.iespuertodelacruz.routinefights.comment.domain.Comment;
import es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.primary.v2.dtos.CommentOutputV2;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.mappers.UserOutputV2Mapper;

@Mapper(componentModel = "spring",uses = UserOutputV2Mapper.class)
public interface CommentOutputV2Mapper {

    CommentOutputV2 toDTO(Comment comment);

    List<CommentOutputV2> toDTO(List<Comment> comment);
    
}
