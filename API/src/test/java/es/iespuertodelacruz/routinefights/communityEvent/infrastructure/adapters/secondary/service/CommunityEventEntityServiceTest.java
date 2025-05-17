package es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.secondary.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import es.iespuertodelacruz.routinefights.communityEvent.domain.CommunityEvent;
import es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.secondary.entities.CommunityEventEntity;
import es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.secondary.mappers.CommunityEventEntityMapper;
import es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.secondary.repositories.ICommunityEventEntityRepository;
import es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.secondary.services.CommunityEventEntityService;

class CommunityEventEntityServiceTest {
    private CommunityEventEntityService communityEventEntityService;

    @Mock
    private ICommunityEventEntityRepository communityEventEntityRepository;
    @Mock
    private CommunityEventEntityMapper communityEventEntityMapper;

    CommunityEventEntity communityEventEntity;
    CommunityEvent communityEvent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        communityEventEntityService = new CommunityEventEntityService(communityEventEntityRepository,
                communityEventEntityMapper);

        communityEventEntity = new CommunityEventEntity();
        communityEventEntity.setId("evt1");

        communityEvent = new CommunityEvent();
        communityEvent.setId("evt1");
    }

    @Test
    void saveTest() {
        when(communityEventEntityMapper.toEntity(any(CommunityEvent.class))).thenReturn(new CommunityEventEntity());
        when(communityEventEntityRepository.create(any(), any(), any(), any(), any(), any(), anyList()))
                .thenReturn(new CommunityEventEntity());
        when(communityEventEntityMapper.toDomain(any(CommunityEventEntity.class))).thenReturn(new CommunityEvent());

        CommunityEvent savedCommunityEvent = communityEventEntityService.save(communityEvent);
        assertNotNull(savedCommunityEvent);
    }

    @Test
    void findByIdTest() {
        when(communityEventEntityMapper.toDomain(any(CommunityEventEntity.class))).thenReturn(communityEvent);
        when(communityEventEntityRepository.findById(any()))
                .thenReturn(java.util.Optional.of(new CommunityEventEntity()));

        CommunityEvent foundCommunityEvent = communityEventEntityService.findById("evt1");
        assertNotNull(foundCommunityEvent);
    }

    @Test
    void getActiveCommunityEventsTest() {
        when(communityEventEntityMapper.toDomain(anyList())).thenReturn(List.of(communityEvent));
        when(communityEventEntityRepository.getActiveCommunityEvents(any(LocalDateTime.class)))
                .thenReturn(List.of(new CommunityEventEntity()));

        List<CommunityEvent> activeCommunityEvents = communityEventEntityService.getActiveCommunityEvents();
        assertNotNull(activeCommunityEvents);
    }

    @Test
    void getNearestCommunityEventTest() {
        when(communityEventEntityMapper.toDomain(any(CommunityEventEntity.class))).thenReturn(communityEvent);
        when(communityEventEntityRepository.getNearestCommunityEvent(any(LocalDateTime.class)))
                .thenReturn(new CommunityEventEntity());

        CommunityEvent nearestCommunityEvent = communityEventEntityService.getNearestCommunityEvent();
        assertNotNull(nearestCommunityEvent);
    }

    @Test
    void getActualCommunityEventPointsTest() {
        when(communityEventEntityRepository.getActualCommunityEventPoints(any())).thenReturn(100);
        Integer points = communityEventEntityService.getActualCommunityEventPoints("evt1");
        assertNotNull(points);
    }

    @Test
    void getUsersParticipatingInCommunityEventTest() {
        when(communityEventEntityRepository.getUsersParticipatingInCommunityEvent(any()))
                .thenReturn(List.of(""));

        List<String> users = communityEventEntityService.getUsersParticipatingInCommunityEvent("evt1");
        assertNotNull(users);
    }

    @Test
    void findByIdOnlyBaseTest() {
        when(communityEventEntityMapper.toDomain(any(CommunityEventEntity.class))).thenReturn(communityEvent);
        when(communityEventEntityRepository.findByIdOnlyBase(any())).thenReturn(new CommunityEventEntity());

        CommunityEvent foundCommunityEvent = communityEventEntityService.findByIdOnlyBase("evt1");
        assertNotNull(foundCommunityEvent);
    }

    @Test
    void getAllImagesTest() {
        when(communityEventEntityRepository.getAllImages()).thenReturn(Set.of("image1.png", "image2.png"));
        Set<String> images = communityEventEntityService.getAllImages();
        assertNotNull(images);
    }

    @Test
    void getUsersParticipatingInCommunityEventEndsTodayTest() {
        when(communityEventEntityRepository.getUsersParticipatingInCommunityEventEndsToday(any(LocalDateTime.class)))
                .thenReturn(List.of(new String()));

        List<String> users = communityEventEntityService.getUsersParticipatingInCommunityEventEndsToday();
        assertNotNull(users);
    }

    @Test
    void getCommunityEventEndsTodayTest() {
        when(communityEventEntityMapper.toDomain(any(CommunityEventEntity.class))).thenReturn(communityEvent);
        when(communityEventEntityRepository.getCommunityEventEndsToday(any(LocalDateTime.class)))
                .thenReturn(new CommunityEventEntity());

        CommunityEvent foundCommunityEvent = communityEventEntityService.getCommunityEventEndsToday();
        assertNotNull(foundCommunityEvent);
    }

}
