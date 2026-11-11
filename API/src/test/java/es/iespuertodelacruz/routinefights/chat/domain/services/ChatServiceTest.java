package es.iespuertodelacruz.routinefights.chat.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.iespuertodelacruz.routinefights.chat.domain.ChatMessage;
import es.iespuertodelacruz.routinefights.chat.domain.Conversation;
import es.iespuertodelacruz.routinefights.chat.domain.ports.secondary.IChatRepository;
import es.iespuertodelacruz.routinefights.notification.domain.ports.primary.INotificationService;
import es.iespuertodelacruz.routinefights.shared.dto.UserSummary;
import es.iespuertodelacruz.routinefights.shared.realtime.UserEvent;
import es.iespuertodelacruz.routinefights.shared.realtime.UserEventBus;

class ChatServiceTest {

    private static final String ME = "me@x.com";
    private static final String YOU = "you@x.com";
    private static final String CHAT = "chat-1";

    private IChatRepository repository;
    private UserEventBus bus;
    private INotificationService notifications;
    private ChatService service;

    @BeforeEach
    void setUp() {
        repository = mock(IChatRepository.class);
        bus = mock(UserEventBus.class);
        notifications = mock(INotificationService.class);
        service = new ChatService(repository, bus, notifications);
    }

    private static final ChatMessage SAVED = new ChatMessage("m1", CHAT, "hola", ME, LocalDateTime.now(), false);

    private ChatMessage saved() {
        return SAVED;
    }

    @Test
    void sendingToAUserStartsTheChatAndDeliversIt() {
        when(repository.findOrCreateConversation(eq(ME), eq(YOU), any())).thenReturn(CHAT);
        when(repository.saveMessage(eq(CHAT), eq(ME), eq("hola"), any())).thenReturn(saved());
        when(repository.otherMember(CHAT, ME)).thenReturn(YOU);
        when(repository.countUnreadConversations(YOU)).thenReturn(1);
        when(repository.findConversationView(CHAT, YOU)).thenReturn(new Conversation(CHAT,
                new UserSummary(ME, "me", null), null, 2, null, null));

        ChatMessage result = service.send(ME, null, YOU, "  hola ");

        assertTrue(result.mine());
        verify(bus).publish(eq(YOU), eq(UserEvent.message(saved().forReader(YOU), 1)));
        verify(notifications).messageReceived(YOU, ME, "me", CHAT, "hola", 2, 1);
    }

    @Test
    void sendingToAChatChecksMembership() {
        when(repository.isMember(CHAT, ME)).thenReturn(true);
        when(repository.saveMessage(eq(CHAT), eq(ME), anyString(), any())).thenReturn(saved());
        when(repository.otherMember(CHAT, ME)).thenReturn(YOU);
        service.send(ME, CHAT, null, "hola");
        verify(notifications).messageReceived(eq(YOU), eq(ME), eq("me"), eq(CHAT), eq("hola"), eq(1), anyInt());

        assertThrows(IllegalArgumentException.class, () -> service.send("other@x.com", CHAT, null, "hola"));
    }

    @Test
    void invalidMessagesAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> service.send(ME, null, YOU, " "));
        assertThrows(IllegalArgumentException.class, () -> service.send(ME, null, YOU, null));
        assertThrows(IllegalArgumentException.class,
                () -> service.send(ME, null, YOU, "x".repeat(ChatService.MAX_LENGTH + 1)));
        assertThrows(IllegalArgumentException.class, () -> service.send(ME, null, ME, "hi"));
        assertThrows(IllegalArgumentException.class, () -> service.send(ME, null, null, "hi"));
        assertThrows(IllegalArgumentException.class, () -> service.send(ME, null, "ghost@x.com", "hi"));
    }

    @Test
    void readingAndTypingReachTheOtherMember() {
        when(repository.isMember(CHAT, ME)).thenReturn(true);
        when(repository.otherMember(CHAT, ME)).thenReturn(YOU);

        service.markRead(ME, CHAT);
        service.typing(ME, CHAT);

        verify(repository).markRead(eq(CHAT), eq(ME), any());
        verify(bus).publish(YOU, UserEvent.typing(CHAT));
        assertThrows(IllegalArgumentException.class, () -> service.typing(YOU, CHAT));
        assertThrows(IllegalArgumentException.class, () -> service.markRead(ME, null));
    }

    @Test
    void queries() {
        when(repository.isMember(CHAT, ME)).thenReturn(true);
        when(repository.findMessages(eq(CHAT), any(), eq(ChatService.MAX_PAGE))).thenReturn(List.of(saved()));
        assertTrue(service.messages(ME, CHAT, "2026-01-01T10:00:00Z", 1000).get(0).mine());
        assertThrows(IllegalArgumentException.class, () -> service.messages(ME, CHAT, "yesterday", 10));

        when(repository.findConversations(ME, 0, 1)).thenReturn(List.of());
        assertTrue(service.conversations(ME, -1, 0).isEmpty());

        assertNull(service.conversationWith(ME, YOU));
        when(repository.findConversation(ME, YOU)).thenReturn(CHAT);
        service.conversationWith(ME, YOU);
        service.conversation(ME, CHAT);
        verify(repository, org.mockito.Mockito.times(2)).findConversationView(CHAT, ME);

        when(repository.countUnreadConversations(ME)).thenReturn(3);
        assertEquals(3, service.unreadConversations(ME));
    }

    @Test
    void helpers() {
        assertTrue(ChatService.parse(null).isAfter(LocalDateTime.now()));
        assertEquals(LocalDateTime.parse("2026-01-01T10:00"), ChatService.parse("2026-01-01T10:00:00"));
        assertEquals("ana", ChatService.displayName(new UserSummary("ana@x.com", "", null)));
        assertEquals("", ChatService.displayName(new UserSummary(null, null, null)));
    }
}
