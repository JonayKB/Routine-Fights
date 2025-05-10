package es.iespuertodelacruz.routinefights.communityEvent.domain.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.activity.domain.ports.secondary.IActivityRepository;
import es.iespuertodelacruz.routinefights.communityEvent.domain.CommunityEvent;
import es.iespuertodelacruz.routinefights.communityEvent.domain.ports.primary.ICommunityEventService;
import es.iespuertodelacruz.routinefights.communityEvent.domain.ports.secondary.ICommunityEventRepository;
import es.iespuertodelacruz.routinefights.user.domain.User;
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
            LocalDateTime finishDate,String image,List<String> activityIDs) {
        CommunityEvent communityEvent = new CommunityEvent();
        communityEvent.setName(name);
        communityEvent.setTotalRequired(totalRequired);
        communityEvent.setStartDate(startDate);
        communityEvent.setFinishDate(finishDate);
        communityEvent.setCreatedAt(LocalDateTime.now());
        communityEvent.setImage(image);
        communityEvent.setActivities(activityIDs.stream()
                .map(activityID -> activityRepository.findById(activityID))
                .toList());
        return communityEventRepository.save(communityEvent);
    }

    @Override
    public CommunityEvent getCommunityEventById(String id) {
        return communityEventRepository.findByIdOnlyBase(id);
    }

    @Override
    public List<CommunityEvent> getActiveCommunityEvent() {
        return communityEventRepository.getActiveCommunityEvents();
    }

    @Override
    public CommunityEvent getNearestCommunityEvent() {
        return communityEventRepository.getNearestCommunityEvent();
    }

    @Override
    public Integer getCommunityEventPointsById(String id) {
        return communityEventRepository.getActualCommunityEventPoints(id);
    }

    @Override
    public List<User> getUsersParticipatingInCommunityEvent(String id) {
        return communityEventRepository.getUsersParticipatingInCommunityEvent(id);
    }

    @Override
    public Set<String> findAllImages() {
        return communityEventRepository.getAllImages();
    }

}
