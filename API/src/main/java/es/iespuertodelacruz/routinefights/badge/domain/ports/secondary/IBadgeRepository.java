package es.iespuertodelacruz.routinefights.badge.domain.ports.secondary;

import java.util.List;

import es.iespuertodelacruz.routinefights.badge.domain.Badge;

public interface IBadgeRepository {

    Badge save(Badge badge);

    Badge findById(String id);

    List<Badge> findByCommunityEvent(String communityEventId);

    List<Badge> findByUserEmail(String name);

    Boolean addBadgeToUser(String userEmail, String badgeId);

    List<Boolean> addBadgeToUser(List<String> userEmail, String badgeId);
    
}
