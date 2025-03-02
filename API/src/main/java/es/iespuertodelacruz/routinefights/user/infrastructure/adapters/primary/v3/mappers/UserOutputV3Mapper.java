package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v3.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.primary.v3.dtos.UserOutputDTOV3;

/**
 * UserOutputV3Mapper
 */
@Mapper(componentModel = "spring")
public interface UserOutputV3Mapper {
    public UserOutputDTOV3 tOutputDTOV3(User user);

    public List<UserOutputDTOV3> tOutputDTOV3(List<User> users);
}
