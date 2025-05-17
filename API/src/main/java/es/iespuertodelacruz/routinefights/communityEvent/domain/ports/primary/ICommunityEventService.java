package es.iespuertodelacruz.routinefights.communityEvent.domain.ports.primary;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import es.iespuertodelacruz.routinefights.communityEvent.domain.CommunityEvent;
import es.iespuertodelacruz.routinefights.user.domain.User;

public interface ICommunityEventService {

    CommunityEvent createCommunityEvent(String name, Integer totalRequired, LocalDateTime startDate,
            LocalDateTime finishDate, String image, List<String> activityIDs);

    CommunityEvent getCommunityEventById(String id);

    List<CommunityEvent> getActiveCommunityEvent();

    CommunityEvent getNearestCommunityEvent();

    Integer getCommunityEventPointsById(String id);

    List<String> getUsersParticipatingInCommunityEvent(String id);

    List<String> getUsersParticipatingInCommunityEventEndsToday();

    Set<String> findAllImages();

    CommunityEvent getCommunityEventEndsToday();

}
