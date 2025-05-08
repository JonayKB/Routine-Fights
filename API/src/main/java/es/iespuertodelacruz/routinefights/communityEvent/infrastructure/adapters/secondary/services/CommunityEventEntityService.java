package es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.secondary.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.communityEvent.domain.CommunityEvent;
import es.iespuertodelacruz.routinefights.communityEvent.domain.ports.secondary.ICommunityEventRepository;
import es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.secondary.mappers.CommunityEventEntityMapper;
import es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.secondary.repositories.ICommunityEventEntityRepository;
import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.mappers.IUserEntityMapper;

@Service
public class CommunityEventEntityService implements ICommunityEventRepository {
    private ICommunityEventEntityRepository communityEventEntityRepository;
    private CommunityEventEntityMapper communityEventEntityMapper;
    private IUserEntityMapper userEntityMapper;

    public CommunityEventEntityService(ICommunityEventEntityRepository communityEventEntityRepository,
            CommunityEventEntityMapper communityEventEntityMapper, IUserEntityMapper userEntityMapper) {
        this.communityEventEntityRepository = communityEventEntityRepository;
        this.communityEventEntityMapper = communityEventEntityMapper;
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public CommunityEvent save(CommunityEvent entity) {
        return communityEventEntityMapper
                .toDomain(communityEventEntityRepository.save(communityEventEntityMapper.toEntity(entity)));
    }

    @Override
    public CommunityEvent findById(String id) {
        return communityEventEntityMapper.toDomain(communityEventEntityRepository.findById(id).orElse(null));
    }

    @Override
    public List<CommunityEvent> getActiveCommunityEvents() {
        return communityEventEntityMapper
                .toDomain(communityEventEntityRepository.getActiveCommunityEvents(LocalDateTime.now()));
    }

    @Override
    public CommunityEvent getNearestCommunityEvent() {
        return communityEventEntityMapper
                .toDomain(communityEventEntityRepository.getNearestCommunityEvent(LocalDateTime.now()));
    }

    @Override
    public Integer getActualCommunityEventPoints(String id) {
        return communityEventEntityRepository.getActualCommunityEventPoints(id);
    }

    @Override
    public List<User> getUsersParticipatingInCommunityEvent(String id) {
        return userEntityMapper.toDomain(communityEventEntityRepository.getUsersParticipatingInCommunityEvent(id));

    }

}
