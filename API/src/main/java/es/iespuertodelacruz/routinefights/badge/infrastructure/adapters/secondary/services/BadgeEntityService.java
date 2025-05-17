package es.iespuertodelacruz.routinefights.badge.infrastructure.adapters.secondary.services;

import java.util.List;

import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.badge.domain.Badge;
import es.iespuertodelacruz.routinefights.badge.domain.ports.secondary.IBadgeRepository;
import es.iespuertodelacruz.routinefights.badge.infrastructure.adapters.secondary.mappers.BadgeEntityMapper;
import es.iespuertodelacruz.routinefights.badge.infrastructure.adapters.secondary.repositories.IBadgeEntityRepository;

@Service
public class BadgeEntityService implements IBadgeRepository {
    private IBadgeEntityRepository badgeEntityRepository;
    private BadgeEntityMapper badgeEntityMapper;

    public BadgeEntityService(IBadgeEntityRepository badgeEntityRepository,
            BadgeEntityMapper badgeEntityMapper) {
        this.badgeEntityRepository = badgeEntityRepository;
        this.badgeEntityMapper = badgeEntityMapper;
    }

    @Override
    public Badge save(Badge badge) {

        return badgeEntityMapper.toDomain(badgeEntityRepository.create(badge.getImage(), badge.getLevel(),badge.getCommunityEvent().getId()));
    }

    @Override
    public Badge findById(String id) {
        return badgeEntityMapper.toDomain(badgeEntityRepository.findById(id).orElse(null));
    }

    @Override
    public List<Badge> findByCommunityEvent(String communityEventId) {
        return badgeEntityMapper.toDomain(badgeEntityRepository.findByCommunityEventId(communityEventId));

    }

    @Override
    public List<Badge> findByUserEmail(String name) {
        return badgeEntityMapper.toDomain(badgeEntityRepository.findByUserEmail(name));
    }

    @Override
    public Boolean addBadgeToUser(String userEmail, String  badgeId) {
        return badgeEntityRepository.addBadgeToUser(userEmail, badgeId);
    }
    @Override
    public List<Boolean> addBadgeToUser(List<String> userEmail, String  badgeId) {
        return badgeEntityRepository.addBadgeToUser(userEmail, badgeId);
    }

}
