package es.iespuertodelacruz.routinefights.notification.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import es.iespuertodelacruz.routinefights.chat.domain.ports.secondary.IChatRepository;
import es.iespuertodelacruz.routinefights.device_token.domain.DeviceToken;
import es.iespuertodelacruz.routinefights.device_token.domain.ports.primary.IDeviceTokenService;
import es.iespuertodelacruz.routinefights.notification.domain.Notification;
import es.iespuertodelacruz.routinefights.notification.domain.NotificationType;
import es.iespuertodelacruz.routinefights.notification.domain.ports.secondary.INotificationRepository;
import es.iespuertodelacruz.routinefights.shared.dto.UserSummary;
import es.iespuertodelacruz.routinefights.shared.realtime.UserEvent;
import es.iespuertodelacruz.routinefights.shared.realtime.UserEventBus;
import es.iespuertodelacruz.routinefights.shared.services.NotificationsService;
import es.iespuertodelacruz.routinefights.shared.services.NotificationsService.Push;

class NotificationServiceTest {

    private static final String OWNER = "owner@x.com";
    private static final String ACTOR = "actor@x.com";
    private static final String POST = "post-1";

    private INotificationRepository repository;
    private IDeviceTokenService devices;
    private NotificationsService push;
    private UserEventBus bus;
    private IChatRepository chat;
    private NotificationService service;

    @BeforeEach
    void setUp() {
        repository = mock(INotificationRepository.class);
        devices = mock(IDeviceTokenService.class);
        push = mock(NotificationsService.class);
        bus = mock(UserEventBus.class);
        chat = mock(IChatRepository.class);
        service = new NotificationService(repository, devices, push, bus, chat);
        when(devices.findByEmail(OWNER)).thenReturn(List.of(new DeviceToken("t", null, "es-ES")));
    }

    private Notification entry(String type, int actors) {
        return new Notification("n1", type, POST, List.of(new UserSummary(ACTOR, "actor", null)), actors, "nice",
                null, null, LocalDateTime.now(), false);
    }

    @Test
    void likeAccumulatesAndPushesGroupedByPost() {
        when(repository.postOwner(POST)).thenReturn(OWNER);
        when(repository.accumulate(eq(OWNER), eq(NotificationType.LIKE), eq(POST), eq(ACTOR), isNull(), isNull(),
                any())).thenReturn(entry(NotificationType.LIKE, 3));
        when(repository.countUnread(OWNER)).thenReturn(2);
        when(chat.countUnreadConversations(OWNER)).thenReturn(1);

        service.postLiked(ACTOR, POST);

        ArgumentCaptor<Push> sent = ArgumentCaptor.forClass(Push.class);
        verify(push).push(anyList(), sent.capture());
        assertEquals("notifLikeMany", sent.getValue().bodyKey());
        assertEquals("LIKE:" + POST, sent.getValue().tag());
        assertEquals(3, sent.getValue().badge());
        assertEquals(2, sent.getValue().args().get("others"));
        assertEquals(POST, sent.getValue().data().get("targetId"));
        verify(bus).publish(OWNER, UserEvent.notification(2));
    }

    @Test
    void ownActionsAndMissingPostsAreIgnored() {
        when(repository.postOwner(POST)).thenReturn(ACTOR);
        service.postLiked(ACTOR, POST);
        service.postCommented(ACTOR, POST, "me");
        service.postUnliked(ACTOR, POST);
        service.userFollowed(ACTOR, ACTOR);
        when(repository.postOwner("gone")).thenReturn(null);
        service.postLiked(ACTOR, "gone");
        service.postUnliked(ACTOR, "gone");
        service.postCommented(ACTOR, "gone", "x");
        verify(repository, never()).accumulate(anyString(), anyString(), anyString(), anyString(), any(), any(),
                any());
        verify(repository, never()).retract(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void commentUsesSingleTextAndFollowGroupsPerUser() {
        when(repository.postOwner(POST)).thenReturn(OWNER);
        when(repository.accumulate(eq(OWNER), eq(NotificationType.COMMENT), eq(POST), eq(ACTOR), eq("great"),
                isNull(), any())).thenReturn(entry(NotificationType.COMMENT, 1));
        when(repository.accumulate(eq(OWNER), eq(NotificationType.FOLLOW), eq(OWNER), eq(ACTOR), isNull(),
                isNull(), any())).thenReturn(entry(NotificationType.FOLLOW, 1));

        service.postCommented(ACTOR, POST, "  great  ");
        service.userFollowed(ACTOR, OWNER);

        ArgumentCaptor<Push> sent = ArgumentCaptor.forClass(Push.class);
        verify(push, org.mockito.Mockito.times(2)).push(anyList(), sent.capture());
        assertEquals("notifCommentOne", sent.getAllValues().get(0).bodyKey());
        assertEquals("notifFollowOne", sent.getAllValues().get(1).bodyKey());
    }

    @Test
    void retractionsUpdateTheBadge() {
        when(repository.postOwner(POST)).thenReturn(OWNER);
        service.postUnliked(ACTOR, POST);
        service.userUnfollowed(ACTOR, OWNER);
        verify(repository).retract(OWNER, NotificationType.LIKE, POST, ACTOR);
        verify(repository).retract(OWNER, NotificationType.FOLLOW, OWNER, ACTOR);
    }

    @Test
    void noDevicesMeansNoPush() {
        when(repository.postOwner(POST)).thenReturn("nodevice@x.com");
        when(repository.accumulate(anyString(), anyString(), anyString(), anyString(), any(), any(), any()))
                .thenReturn(entry(NotificationType.LIKE, 1));
        service.postLiked(ACTOR, POST);
        verify(push, never()).push(anyList(), any());
    }

    @Test
    void failuresNeverEscape() {
        when(repository.postOwner(POST)).thenThrow(new RuntimeException("db down"));
        service.postLiked(ACTOR, POST);
        verify(push, never()).push(anyList(), any());
    }

    @Test
    void streakRiskPicksTitleAndBody() {
        when(repository.accumulate(eq(OWNER), eq(NotificationType.STREAK_RISK), eq("act"), isNull(), eq("Run"),
                eq("5"), any())).thenReturn(entry(NotificationType.STREAK_RISK, 0));

        service.streakAtRisk(OWNER, "act", "Run", 5, 2, "weekly", true);

        ArgumentCaptor<Push> sent = ArgumentCaptor.forClass(Push.class);
        verify(push).push(anyList(), sent.capture());
        assertEquals("streakLastCallTitle", sent.getValue().titleKey());
        assertEquals("streakRiskBodyWeeklyMany", sent.getValue().bodyKey());
    }

    @Test
    void streakBodyKeys() {
        assertEquals("streakRiskBodyDaily", NotificationService.streakBodyKey("daily", 1));
        assertEquals("streakRiskBodyDaily", NotificationService.streakBodyKey(null, 1));
        assertEquals("streakRiskBodyMonthlyMany", NotificationService.streakBodyKey("monthly", 3));
        assertEquals("streakRiskBodyYearly", NotificationService.streakBodyKey("yearly", 1));
    }

    @Test
    void messagePushCarriesTheChat() {
        when(repository.countUnread(OWNER)).thenReturn(1);
        service.messageReceived(OWNER, ACTOR, "actor", "chat-1", "hey", 2, 3);

        ArgumentCaptor<Push> sent = ArgumentCaptor.forClass(Push.class);
        verify(push).push(anyList(), sent.capture());
        assertEquals("notifMessageMany", sent.getValue().bodyKey());
        assertEquals("MESSAGE:chat-1", sent.getValue().tag());
        assertEquals(4, sent.getValue().badge());
        assertEquals("chat-1", sent.getValue().data().get("conversationId"));

        service.messageReceived("nodevice@x.com", ACTOR, "actor", "chat-1", "hey", 1, 1);
        verify(push).push(anyList(), any());
    }

    @Test
    void inboxQueries() {
        when(repository.findByRecipient(OWNER, 50, 50)).thenReturn(List.of());
        assertTrue(service.findMine(OWNER, 1, 500).isEmpty());
        when(repository.countUnread(OWNER)).thenReturn(4);
        assertEquals(4, service.countUnread(OWNER));

        service.markAllRead(OWNER);
        verify(bus).publish(OWNER, UserEvent.notification(0));
        service.markRead(OWNER, "n1");
        verify(repository).markRead(OWNER, "n1");
    }

    @Test
    void helpers() {
        assertEquals("", NotificationService.shorten(null));
        assertEquals("a b", NotificationService.shorten(" a \n b "));
        assertEquals(NotificationService.PREVIEW_LENGTH, NotificationService.shorten("x".repeat(200)).length());
        assertEquals("bob", NotificationService.displayName(new UserSummary("bob@x.com", null, null)));
        assertEquals("", NotificationService.displayName(new UserSummary(null, " ", null)));
        Map<String, Object> args = NotificationService.args(new Notification("n", "LIKE", "p", List.of(), 0, null,
                null, null, null, false));
        assertEquals("", args.get("actor"));
        assertEquals(0, args.get("others"));
        assertFalse(NotificationService.many(entry("LIKE", 1)));
    }
}
