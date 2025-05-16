package es.iespuertodelacruz.routinefights.badge.infrastructure.adapters.secondary.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import es.iespuertodelacruz.routinefights.badge.domain.Badge;
import es.iespuertodelacruz.routinefights.badge.infrastructure.adapters.secondary.entities.BadgeEntity;
import es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.secondary.mappers.CommunityEventEntityMapper;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.mappers.IUserEntityMapper;

@Mapper(componentModel = "spring", uses = { CommunityEventEntityMapper.class,IUserEntityMapper.class })
public interface BadgeEntityMapper {

    public BadgeEntity toEntity(Badge badge);
    public Badge toDomain(BadgeEntity badgeEntity);

    public List<BadgeEntity> toEntity(List<Badge> badges);
    public List<Badge> toDomain(List<BadgeEntity> badgeEntities);
    
}
