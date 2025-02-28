package es.iespuertodelacruz.routinefights.shared.mappers;

import org.mapstruct.Mapper;

import es.iespuertodelacruz.routinefights.shared.dto.UserDTOAuth;
import es.iespuertodelacruz.routinefights.user.domain.User;

@Mapper(componentModel = "spring")
public interface UserDTOAuthMapper {
    UserDTOAuth toDTO(User user);
}
