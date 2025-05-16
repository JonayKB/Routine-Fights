package es.iespuertodelacruz.routinefights.communityEvent.domain.services;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import es.iespuertodelacruz.routinefights.activity.domain.ports.secondary.IActivityRepository;
import es.iespuertodelacruz.routinefights.communityEvent.domain.CommunityEvent;
import es.iespuertodelacruz.routinefights.communityEvent.domain.ports.secondary.ICommunityEventRepository;
import es.iespuertodelacruz.routinefights.user.domain.User;

class CommunityEventServiceTest {
    private static final String ID = "evt1";
    private CommunityEventService service;
    @Mock
    private ICommunityEventRepository repository;
    @Mock
    private IActivityRepository activityRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new CommunityEventService(repository, activityRepository);
    }

    @Test
    void createCommunityEvent() {
        CommunityEvent communityEvent = new CommunityEvent();
        when(repository.save(communityEvent)).thenReturn(communityEvent);
        CommunityEvent createdEvent = service.createCommunityEvent(
            "Event Name", 
            10, 
            LocalDateTime.now(), 
            LocalDateTime.now().plusDays(1), 
            "image.png", 
            List.of("activity1", "activity2")
        );
        assertNotNull(createdEvent);
    }

    @Test
    void getActiveCommunityEvent() {
        when(repository.getActiveCommunityEvents()).thenReturn(List.of(new CommunityEvent()));
        List<CommunityEvent> activeEvents = service.getActiveCommunityEvent();
        assertNotNull(activeEvents);
    }

    @Test
    void getNearestCommunityEvent() {
        when(repository.getNearestCommunityEvent()).thenReturn(new CommunityEvent());
        CommunityEvent nearestEvent = service.getNearestCommunityEvent();
        assertNotNull(nearestEvent);
    }

    @Test
    void getCommunityEventPointsById() {
        when(repository.getActualCommunityEventPoints(ID)).thenReturn(100);
        Integer points = service.getCommunityEventPointsById(ID);
        assertNotNull(points);
    }

    @Test
    void getUsersParticipatingInCommunityEvent() {
        when(repository.getUsersParticipatingInCommunityEvent(ID)).thenReturn(List.of(new User()));
        List<User> users = service.getUsersParticipatingInCommunityEvent(ID);
        assertNotNull(users);
    }

    @Test
    void findAllImages() {
        when(repository.getAllImages()).thenReturn(Set.of("image1.png", "image2.png"));
        Set<String> images = service.findAllImages();
        assertNotNull(images);
    }

    @Test
    void getUsersParticipatingInCommunityEventEndsToday() {
        when(repository.getUsersParticipatingInCommunityEventEndsToday()).thenReturn(List.of(new User()));
        List<User> users = service.getUsersParticipatingInCommunityEventEndsToday();
        assertNotNull(users);
    }

    @Test
    void getCommunityEventEndsToday() {
        when(repository.getCommunityEventEndsToday()).thenReturn(new CommunityEvent());
        CommunityEvent event = service.getCommunityEventEndsToday();
        assertNotNull(event);
    }

}
