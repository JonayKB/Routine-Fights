package es.iespuertodelacruz.routinefights.community_event.infrastructure.adapters.primary.v3.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import es.iespuertodelacruz.routinefights.community_event.domain.CommunityEvent;
import es.iespuertodelacruz.routinefights.community_event.infrastructure.adapters.primary.v3.dtos.CommunityEventOutputV3;

@Mapper(componentModel = "spring")
public interface ICommunityEventOutputV3Mapper {
    CommunityEventOutputV3 toDto(CommunityEvent communityEvent);

    List<CommunityEventOutputV3> toDto(List<CommunityEvent> communityEvents);

    
}
