package es.iespuertodelacruz.routinefights.communityEvent.domain.ports.secondary;

import java.util.List;

import es.iespuertodelacruz.routinefights.communityEvent.domain.CommunityEvent;
import es.iespuertodelacruz.routinefights.user.domain.User;

public interface ICommunityEventRepository {

    CommunityEvent save(CommunityEvent entity);
    CommunityEvent findById(String id);
    List<CommunityEvent> getActiveCommunityEvents();
    CommunityEvent getNearestCommunityEvent();
    Integer getActualCommunityEventPoints(String id);
    List<User> getUsersParticipatingInCommunityEvent(String id);

    
}
