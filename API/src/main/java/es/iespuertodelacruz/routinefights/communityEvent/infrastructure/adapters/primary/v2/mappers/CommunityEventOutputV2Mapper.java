package es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.primary.v2.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import es.iespuertodelacruz.routinefights.communityEvent.domain.CommunityEvent;
import es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.primary.v2.dtos.CommunityEventOutputV2;

@Mapper(componentModel = "spring")
public interface CommunityEventOutputV2Mapper {

    CommunityEventOutputV2 toDto(CommunityEvent communityEvent);

    List<CommunityEventOutputV2> toDto(List<CommunityEvent> communityEvents);
    
}
