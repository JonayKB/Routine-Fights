package es.iespuertodelacruz.routinefights.badge.infrastructure.adapters.primary.v2.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import es.iespuertodelacruz.routinefights.badge.domain.Badge;
import es.iespuertodelacruz.routinefights.badge.infrastructure.adapters.primary.v2.dtos.BadgeV2Output;
import es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.primary.v2.mappers.CommunityEventOutputV2Mapper;

@Mapper(componentModel = "spring", uses = { CommunityEventOutputV2Mapper.class })
public interface BadgeV2OutputMapper {

    public BadgeV2Output toDTO(Badge badge);

    public List<BadgeV2Output> toDTO(List<Badge> badge);

}
