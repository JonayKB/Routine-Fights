package es.iespuertodelacruz.routinefights.communityEvent.domain.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.activity.domain.ports.secondary.IActivityRepository;
import es.iespuertodelacruz.routinefights.communityEvent.domain.CommunityEvent;
import es.iespuertodelacruz.routinefights.communityEvent.domain.ports.primary.ICommunityEventService;
import es.iespuertodelacruz.routinefights.communityEvent.domain.ports.secondary.ICommunityEventRepository;
@Service
public class CommunityEventService implements ICommunityEventService {
    private ICommunityEventRepository communityEventRepository;
    private IActivityRepository activityRepository;

    public CommunityEventService(ICommunityEventRepository communityEventRepository,
            IActivityRepository activityRepository) {
        this.communityEventRepository = communityEventRepository;
        this.activityRepository = activityRepository;
    }

    @Override
    public CommunityEvent createCommunityEvent(String name, Integer totalRequired, LocalDateTime startDate,
            LocalDateTime finishDate, List<String> activityIDs) {
        CommunityEvent communityEvent = new CommunityEvent();
        communityEvent.setName(name);
        communityEvent.setTotalRequired(totalRequired);
        communityEvent.setStartDate(startDate);
        communityEvent.setFinishDate(finishDate);
        communityEvent.setCreatedAt(LocalDateTime.now());
        communityEvent.setActivities(activityIDs.stream()
                .map(activityID -> activityRepository.findById(activityID))
                .toList());
        return communityEventRepository.save(communityEvent);
    }

}
