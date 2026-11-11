package es.iespuertodelacruz.routinefights.chat.infrastructure.adapters.primary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import es.iespuertodelacruz.routinefights.chat.domain.ports.primary.IChatService;

class ChatResolverTest {

    private static final String ME = "me@x.com";
    private final IChatService service = mock(IChatService.class);
    private final ChatResolver resolver = new ChatResolver(service);

    @BeforeEach
    void login() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(ME, null));
    }

    @AfterEach
    void logout() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void delegatesAsTheSignedInUser() {
        resolver.conversations(null, null);
        verify(service).conversations(ME, 0, 30);
        resolver.conversations(1, 10);
        verify(service).conversations(ME, 1, 10);
        resolver.conversation("c");
        verify(service).conversation(ME, "c");
        resolver.conversationWith("you@x.com");
        verify(service).conversationWith(ME, "you@x.com");
        resolver.messages("c", null, null);
        verify(service).messages(ME, "c", null, 40);
        resolver.messages("c", "2026-01-01T00:00:00", 5);
        verify(service).messages(ME, "c", "2026-01-01T00:00:00", 5);
        resolver.sendMessage(null, "you@x.com", "hi");
        verify(service).send(ME, null, "you@x.com", "hi");
        assertTrue(resolver.markConversationRead("c"));
        verify(service).markRead(ME, "c");
        assertTrue(resolver.sendTyping("c"));
        verify(service).typing(ME, "c");
        when(service.unreadConversations(ME)).thenReturn(2);
        assertEquals(2, resolver.unreadConversationsCount());
    }
}
