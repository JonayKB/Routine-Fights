package es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.secondary.services;


import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.communityEvent.domain.CommunityEvent;
import es.iespuertodelacruz.routinefights.communityEvent.domain.ports.secondary.ICommunityEventRepository;
import es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.secondary.mappers.CommunityEventEntityMapper;
import es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.secondary.repositories.ICommunityEventEntityRepository;
@Service
public class CommunityEventEntityService implements ICommunityEventRepository{
    private ICommunityEventEntityRepository communityEventEntityRepository;
    private CommunityEventEntityMapper communityEventEntityMapper;
    
    public CommunityEventEntityService(ICommunityEventEntityRepository communityEventEntityRepository,
            CommunityEventEntityMapper communityEventEntityMapper) {
        this.communityEventEntityRepository = communityEventEntityRepository;
        this.communityEventEntityMapper = communityEventEntityMapper;
    }

    @Override
    public CommunityEvent save(CommunityEvent entity) {
        return communityEventEntityMapper.toDomain(communityEventEntityRepository.save(communityEventEntityMapper.toEntity(entity)));
    }
    


}
