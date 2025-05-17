package es.iespuertodelacruz.routinefights.badge.infrastructure.adapters.primary.v3.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import es.iespuertodelacruz.routinefights.badge.domain.Badge;
import es.iespuertodelacruz.routinefights.badge.infrastructure.adapters.primary.v3.dtos.BadgeV3Output;
import es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.primary.v3.mappers.ICommunityEventOutputV3Mapper;

@Mapper(componentModel = "spring", uses = { ICommunityEventOutputV3Mapper.class })
public interface BadgeV3OutputMapper {

    public BadgeV3Output toDTO(Badge badge);
    public List<BadgeV3Output> toDTO(List<Badge> badges);

}
