package es.iespuertodelacruz.routinefights.integrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.time.Duration;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.graphql.client.WebSocketGraphQlClientInterceptor;
import org.springframework.graphql.test.tester.WebSocketGraphQlTester;
import org.springframework.web.reactive.socket.client.StandardWebSocketClient;

import es.iespuertodelacruz.routinefights.chat.domain.ports.primary.IChatService;
import es.iespuertodelacruz.routinefights.shared.realtime.UserEvent;
import es.iespuertodelacruz.routinefights.shared.realtime.UserEventBus;
import es.iespuertodelacruz.routinefights.shared.security.JwtService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/** The app's live connection end to end: WebSocket, token in connection_init, events. */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RealtimeWebSocketIntegrationTest extends BaseDatabaseIntegration {

    private static final String ANA = "ws-ana@test.com";
    private static final String BEA = "ws-bea@test.com";
    private static final String SUBSCRIPTION = """
            subscription { userEvents { kind conversationId message { text senderEmail mine } } }
            """;

    @LocalServerPort
    private int port;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private IChatService chatService;
    @Autowired
    private Neo4jClient client;
    @Autowired
    private UserEventBus bus;

    @BeforeEach
    void seed() {
        client.query("MATCH (u:User) WHERE u.email IN [$a, $b] DETACH DELETE u")
                .bindAll(Map.of("a", ANA, "b", BEA)).run();
        client.query("CREATE (:User {email: $a, username: 'ana'}), (:User {email: $b, username: 'bea'})")
                .bindAll(Map.of("a", ANA, "b", BEA)).run();
    }

    private WebSocketGraphQlTester tester(Map<String, Object> init) {
        return WebSocketGraphQlTester
                .builder(URI.create("ws://localhost:" + port + "/graphql-ws"), new StandardWebSocketClient())
                .interceptor(new WebSocketGraphQlClientInterceptor() {
                    @Override
                    public Mono<Object> connectionInitPayload() {
                        return Mono.just(init);
                    }
                })
                .build();
    }

    @Test
    void messagesArriveLiveToTheRecipient() {
        String token = jwtService.generateToken(BEA, "ROLE_USER");
        WebSocketGraphQlTester bea = tester(Map.of("Authorization", "Bearer " + token));

        Flux<UserEvent> events = bea.document(SUBSCRIPTION)
                .executeSubscription()
                .toFlux("userEvents", UserEvent.class);

        StepVerifier.create(events.take(1))
                .then(() -> {
                    // Send once Bea's subscription is listening on the server.
                    Flux.interval(Duration.ofMillis(20))
                            .filter(tick -> bus.isOnline(BEA))
                            .blockFirst(Duration.ofSeconds(5));
                    chatService.send(ANA, null, BEA, "hola Bea");
                })
                .assertNext(event -> {
                    assertEquals(UserEvent.KIND_MESSAGE, event.kind());
                    assertEquals("hola Bea", event.message().text());
                    assertEquals(ANA, event.message().senderEmail());
                    assertTrue(!event.message().mine());
                })
                .expectComplete()
                .verify(Duration.ofSeconds(10));
        bea.stop().block(Duration.ofSeconds(5));
    }

    @Test
    void connectionsWithoutAValidTokenAreRefused() {
        WebSocketGraphQlTester anonymous = tester(Map.of("Authorization", "Bearer not-a-token"));
        StepVerifier.create(anonymous.document(SUBSCRIPTION).executeSubscription().toFlux("userEvents",
                UserEvent.class))
                .expectError()
                .verify(Duration.ofSeconds(10));
    }
}
