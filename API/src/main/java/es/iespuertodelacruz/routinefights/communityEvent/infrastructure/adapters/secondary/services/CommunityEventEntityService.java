package es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.secondary.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.communityEvent.domain.CommunityEvent;
import es.iespuertodelacruz.routinefights.communityEvent.domain.ports.secondary.ICommunityEventRepository;
import es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.secondary.mappers.CommunityEventEntityMapper;
import es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.secondary.repositories.ICommunityEventEntityRepository;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.mappers.IUserEntityMapper;

@Service
public class CommunityEventEntityService implements ICommunityEventRepository {
    private ICommunityEventEntityRepository communityEventEntityRepository;
    private CommunityEventEntityMapper communityEventEntityMapper;

    public CommunityEventEntityService(ICommunityEventEntityRepository communityEventEntityRepository,
            CommunityEventEntityMapper communityEventEntityMapper) {
        this.communityEventEntityRepository = communityEventEntityRepository;
        this.communityEventEntityMapper = communityEventEntityMapper;
    }

    @Override
    public CommunityEvent save(CommunityEvent entity) {
        return communityEventEntityMapper
                .toDomain(communityEventEntityRepository.create(entity.getName(), entity.getCreatedAt(),
                        entity.getStartDate(), entity.getFinishDate(), entity.getTotalRequired(), entity.getImage(),
                        entity.getActivities().stream().map(a -> a.getId()).toList()));
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
    public List<String> getUsersParticipatingInCommunityEvent(String id) {
        return communityEventEntityRepository.getUsersParticipatingInCommunityEvent(id);

    }

    @Override
    public CommunityEvent findByIdOnlyBase(String id) {
        return communityEventEntityMapper
                .toDomain(communityEventEntityRepository.findByIdOnlyBase(id));
    }

    @Override
    public Set<String> getAllImages() {
        return communityEventEntityRepository.getAllImages();
    }

    @Override
    public List<String> getUsersParticipatingInCommunityEventEndsToday() {
        return 
                communityEventEntityRepository.getUsersParticipatingInCommunityEventEndsToday(LocalDateTime.now());
    }

    @Override
    public CommunityEvent getCommunityEventEndsToday() {
        return communityEventEntityMapper
                .toDomain(communityEventEntityRepository.getCommunityEventEndsToday(LocalDateTime.now()));
    }

}
