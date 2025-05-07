package es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.secondary.mappers;

import org.mapstruct.Mapper;

import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.mappers.ActivityEntityMapper;
import es.iespuertodelacruz.routinefights.communityEvent.domain.CommunityEvent;
import es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.secondary.entities.CommunityEventEntity;

@Mapper(componentModel = "spring", uses = { ActivityEntityMapper.class/*
                                                                       * , BadgeEntityMapper.class,
                                                                       * MeetingEntityMapper.class
                                                                       */ })
public interface CommunityEventEntityMapper {

    public CommunityEvent toDomain(CommunityEventEntity entity);

    public CommunityEventEntity toEntity(CommunityEvent domain);

}
