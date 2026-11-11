package es.iespuertodelacruz.routinefights.shared.realtime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;

import com.auth0.jwt.exceptions.JWTVerificationException;

import es.iespuertodelacruz.routinefights.chat.domain.ChatMessage;
import es.iespuertodelacruz.routinefights.shared.security.JwtService;
import reactor.test.StepVerifier;

class RealtimeTest {

    @Test
    void busDeliversOnlyToListeners() {
        UserEventBus bus = new UserEventBus();
        bus.publish("nobody@x.com", UserEvent.ofNotification(1));
        bus.publish(null, UserEvent.ofNotification(1));
        assertFalse(bus.isOnline("ana@x.com"));

        StepVerifier.create(bus.listen("ana@x.com").take(2))
                .then(() -> {
                    assertTrue(bus.isOnline("ana@x.com"));
                    bus.publish("bea@x.com", UserEvent.ofTyping("c"));
                    bus.publish("ana@x.com", UserEvent.ofNotification(3));
                    bus.publish("ana@x.com", UserEvent.ofRead("c", "now"));
                })
                .expectNext(UserEvent.ofNotification(3))
                .expectNext(UserEvent.ofRead("c", "now"))
                .verifyComplete();
        assertFalse(bus.isOnline("ana@x.com"));
    }

    @Test
    void busKeepsTheChannelWhileAnotherDeviceListens() {
        UserEventBus bus = new UserEventBus();
        var first = bus.listen("ana@x.com").subscribe();
        var second = bus.listen("ana@x.com").subscribe();
        first.dispose();
        assertTrue(bus.isOnline("ana@x.com"));
        second.dispose();
        assertFalse(bus.isOnline("ana@x.com"));
    }

    @Test
    void eventFactories() {
        ChatMessage m = new ChatMessage("m", "c", "hi", "a", LocalDateTime.now(), true);
        UserEvent e = UserEvent.ofMessage(m, 2);
        assertEquals(UserEvent.KIND_MESSAGE, e.kind());
        assertEquals("c", e.conversationId());
        assertEquals(2, e.unreadMessages());
        assertEquals(UserEvent.KIND_TYPING, UserEvent.ofTyping("c").kind());
    }

    @Test
    void subscriptionNeedsAnAuthenticatedSocket() {
        RealtimeResolver resolver = new RealtimeResolver(new UserEventBus());
        assertThrows(AccessDeniedException.class, () -> resolver.userEvents(null));
        StepVerifier.create(resolver.userEvents("ana@x.com").take(java.time.Duration.ofMillis(50)))
                .verifyComplete();
    }

    @Test
    void connectionInitReadsTheToken() {
        JwtService jwt = mock(JwtService.class);
        when(jwt.validateAndGetClaims("good")).thenReturn(Map.of("mail", "ana@x.com", "role", "ROLE_USER"));
        when(jwt.validateAndGetClaims("bad")).thenThrow(new JWTVerificationException("bad"));
        WebSocketAuthInterceptor interceptor = new WebSocketAuthInterceptor(jwt);

        assertEquals("ana@x.com", interceptor.emailFrom(Map.of("Authorization", "Bearer good")));
        assertEquals("ana@x.com", interceptor.emailFrom(Map.of("authorization", "good")));
        assertNull(interceptor.emailFrom(Map.of("Authorization", "Bearer bad")));
        assertNull(interceptor.emailFrom(Map.of("Authorization", "")));
        assertNull(interceptor.emailFrom(Map.of()));
        assertNull(interceptor.emailFrom(null));

        Map<String, Object> attributes = new HashMap<>();
        var info = new org.springframework.graphql.server.WebSocketSessionInfo() {
            public String getId() { return "s"; }
            public Map<String, Object> getAttributes() { return attributes; }
            public URI getUri() { return URI.create("ws://localhost/graphql-ws"); }
            public HttpHeaders getHeaders() { return new HttpHeaders(); }
            public reactor.core.publisher.Mono<java.security.Principal> getPrincipal() { return reactor.core.publisher.Mono.empty(); }
            public java.net.InetSocketAddress getRemoteAddress() { return null; }
        };
        StepVerifier.create(interceptor.handleConnectionInitialization(info, Map.of("Authorization", "Bearer good")))
                .verifyComplete();
        assertEquals("ana@x.com", attributes.get(WebSocketAuthInterceptor.EMAIL));
        StepVerifier.create(interceptor.handleConnectionInitialization(info, Map.of()))
                .expectError(IllegalStateException.class)
                .verify();
    }
}
