package es.iespuertodelacruz.routinefights.badge.domain.ports.primary;

import java.util.List;

import es.iespuertodelacruz.routinefights.badge.domain.Badge;

public interface IBadgeService {

    public Badge createBadge(String image, Integer level, String communityEventId);

    public Badge findById(String id);

    public List<Badge> findByCommunityEvent(String communityEventId);

    public List<Badge>findByUserEmail(String name);

    public Boolean addBadgeToUser(String userId, String badgeId);

    public List<Boolean> addBadgeToUser(List<String> userId, String badgeId);

    
}
