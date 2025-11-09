package es.iespuertodelacruz.routinefights.community_event.domain.ports.secondary;

import java.util.List;
import java.util.Set;

import es.iespuertodelacruz.routinefights.community_event.domain.CommunityEvent;

public interface ICommunityEventRepository {

    CommunityEvent save(CommunityEvent entity);
    CommunityEvent findById(String id);
    List<CommunityEvent> getActiveCommunityEvents();
    CommunityEvent getNearestCommunityEvent();
    Integer getActualCommunityEventPoints(String id);
    List<String> getUsersParticipatingInCommunityEvent(String id);
    CommunityEvent findByIdOnlyBase(String id);
    Set<String> getAllImages();
    List<String> getUsersParticipatingInCommunityEventEndsToday();
    CommunityEvent getCommunityEventEndsToday();
    List<CommunityEvent> findAll();

    
}
