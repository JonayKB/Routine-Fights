package es.iespuertodelacruz.routinefights.badge.domain.services;

import java.util.List;

import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.badge.domain.Badge;
import es.iespuertodelacruz.routinefights.badge.domain.ports.primary.IBadgeService;
import es.iespuertodelacruz.routinefights.badge.domain.ports.secondary.IBadgeRepository;
import es.iespuertodelacruz.routinefights.community_event.domain.CommunityEvent;
import es.iespuertodelacruz.routinefights.community_event.domain.ports.secondary.ICommunityEventRepository;

@Service
public class BadgeService implements IBadgeService {
    private IBadgeRepository badgeRepository;
    private ICommunityEventRepository communityEventRepository;

    public BadgeService(IBadgeRepository badgeRepository,
            ICommunityEventRepository communityEventRepository) {
        this.badgeRepository = badgeRepository;
        this.communityEventRepository = communityEventRepository;
    }

    @Override
    public Badge createBadge(String image, Integer level, String communityEventId) {
        Badge badge = new Badge();
        badge.setImage(image);
        badge.setLevel(level);
        CommunityEvent communityEvent = communityEventRepository.findById(communityEventId);
        if (communityEvent == null) {
            throw new IllegalArgumentException("Community event not found");
        }
        badge.setCommunityEvent(communityEvent);
        return badgeRepository.save(badge);
    }

    @Override
    public Badge findById(String id) {
        return badgeRepository.findById(id);
    }

    @Override
    public List<Badge> findByCommunityEvent(String communityEventId) {
        return badgeRepository.findByCommunityEvent(communityEventId);
    }

    @Override
    public List<Badge> findByUserEmail(String name) {
        return badgeRepository.findByUserEmail(name);
    }

    @Override
    public Boolean addBadgeToUser(String userId, String badgeId) {

        return badgeRepository.addBadgeToUser(userId, badgeId);
    }

    @Override
    public List<Boolean> addBadgeToUser(List<String> userId, String badgeId) {

        return badgeRepository.addBadgeToUser(userId, badgeId);
    }

}
