package es.iespuertodelacruz.routinefights.post.infrastructure.adapters.primary.v2.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import es.iespuertodelacruz.routinefights.post.domain.Post;
import es.iespuertodelacruz.routinefights.post.infrastructure.adapters.primary.v2.dtos.PostOutputDTOV2;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v2.mappers.UserOutputV2Mapper;

@Mapper(componentModel = "spring", uses = {UserOutputV2Mapper.class})
public interface PostOutputV2Mapper {

    PostOutputDTOV2 toDto(Post post);

    List<PostOutputDTOV2> toDto(List<Post> posts);

}
