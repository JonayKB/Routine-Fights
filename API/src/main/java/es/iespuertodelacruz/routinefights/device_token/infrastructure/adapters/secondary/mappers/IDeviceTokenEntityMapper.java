package es.iespuertodelacruz.routinefights.device_token.infrastructure.adapters.secondary.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import es.iespuertodelacruz.routinefights.device_token.domain.DeviceToken;
import es.iespuertodelacruz.routinefights.device_token.infrastructure.adapters.secondary.entities.DeviceTokenEntity;
import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;

@Mapper(componentModel = "spring")
public interface IDeviceTokenEntityMapper {
    DeviceToken toDomain(DeviceTokenEntity deviceTokenEntity);

    @Mapping(target = "user", source = "user", qualifiedByName = "userDomainToEntity")
    DeviceTokenEntity toEntity(DeviceToken deviceToken);

    @Named("userDomainToEntity")
    default UserEntity userDomainToEntity(User user) {
        if (user == null) {
            return null;
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setUsername(user.getUsername());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());
        userEntity.setNationality(user.getNationality());
        userEntity.setPhoneNumber(user.getPhoneNumber());
        userEntity.setImage(user.getImage());
        userEntity.setRole(user.getRole());
        userEntity.setVerified(user.getVerified());
        userEntity.setVerificationToken(user.getVerificationToken());
        

        return userEntity;
    }
}