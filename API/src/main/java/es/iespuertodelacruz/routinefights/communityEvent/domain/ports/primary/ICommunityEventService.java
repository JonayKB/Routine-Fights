package es.iespuertodelacruz.routinefights.communityEvent.domain.ports.primary;

import java.time.LocalDateTime;
import java.util.List;

import es.iespuertodelacruz.routinefights.communityEvent.domain.CommunityEvent;

public interface ICommunityEventService {
    CommunityEvent createCommunityEvent(String name, Integer totalRequired, LocalDateTime startDate,
    LocalDateTime finishDate, List<String> activityIDs);
    
}
