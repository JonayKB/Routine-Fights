package es.iespuertodelacruz.routinefights.integrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;

import es.iespuertodelacruz.routinefights.chat.domain.ChatMessage;
import es.iespuertodelacruz.routinefights.chat.domain.Conversation;
import es.iespuertodelacruz.routinefights.chat.domain.ports.secondary.IChatRepository;
import es.iespuertodelacruz.routinefights.device_token.domain.ports.primary.IDeviceTokenService;
import es.iespuertodelacruz.routinefights.notification.domain.Notification;
import es.iespuertodelacruz.routinefights.notification.domain.NotificationType;
import es.iespuertodelacruz.routinefights.notification.domain.ports.secondary.INotificationRepository;
import es.iespuertodelacruz.routinefights.shared.services.InsightsService;
import es.iespuertodelacruz.routinefights.shared.services.InsightsService.StreakAtRisk;

/** Runs the Cypher of chats, the notification inbox and streak reminders on a real Neo4j. */
class ChatAndNotificationsIntegrationTest extends BaseDatabaseIntegration {

    private static final String ANA = "ana@test.com";
    private static final String BEA = "bea@test.com";
    private static final String CARLA = "carla@test.com";

    @Autowired
    private Neo4jClient client;
    @Autowired
    private IChatRepository chat;
    @Autowired
    private INotificationRepository notifications;
    @Autowired
    private InsightsService insights;
    @Autowired
    private IDeviceTokenService devices;

    @BeforeEach
    void seed() {
        client.query("MATCH (n) WHERE NOT n:User OR n.email IN [$a, $b, $c] DETACH DELETE n")
                .bindAll(Map.of("a", ANA, "b", BEA, "c", CARLA)).run();
        client.query("""
                CREATE (:User {email: $a, username: 'ana', image: 'ana.png'}),
                       (:User {email: $b, username: 'bea'}),
                       (:User {email: $c, username: 'carla'})
                """).bindAll(Map.of("a", ANA, "b", BEA, "c", CARLA)).run();
    }

    private String createPost(String ownerEmail) {
        return client.query("""
                MATCH (u:User {email: $email})
                CREATE (u)-[:Posted]->(p:Post {image: 'post.png', createdAt: $at})
                RETURN elementId(p) AS id
                """).bindAll(Map.of("email", ownerEmail, "at", LocalDateTime.now()))
                .fetchAs(String.class).mappedBy((t, r) -> r.get("id").asString()).one().orElseThrow();
    }

    @Test
    void eachPairOfUsersHasOneChat() {
        LocalDateTime t0 = LocalDateTime.now();
        String id = chat.findOrCreateConversation(ANA, BEA, t0);
        assertNotNull(id);
        // The same pair always gets the same chat, whoever starts it.
        assertEquals(id, chat.findOrCreateConversation(BEA, ANA, t0));
        assertEquals(id, chat.findConversation(BEA, ANA));
        assertNull(chat.findConversation(ANA, CARLA));
        assertNull(chat.findOrCreateConversation(ANA, "nobody@test.com", t0));

        assertTrue(chat.isMember(id, ANA));
        assertFalse(chat.isMember(id, CARLA));
        assertEquals(BEA, chat.otherMember(id, ANA));
        // Empty chats are not listed.
        assertTrue(chat.findConversations(ANA, 0, 10).isEmpty());
    }

    @Test
    void messagesUnreadCountsAndReceipts() {
        LocalDateTime t0 = LocalDateTime.now().minusMinutes(5);
        String id = chat.findOrCreateConversation(ANA, BEA, t0);

        ChatMessage first = chat.saveMessage(id, ANA, "hola", t0.plusSeconds(1));
        chat.saveMessage(id, ANA, "¿qué tal?", t0.plusSeconds(2));
        assertEquals("hola", first.text());
        assertEquals(ANA, first.senderEmail());
        assertEquals(id, first.conversationId());

        assertEquals(2, chat.countUnreadMessages(id, BEA));
        assertEquals(0, chat.countUnreadMessages(id, ANA));
        assertEquals(1, chat.countUnreadConversations(BEA));

        List<Conversation> beas = chat.findConversations(BEA, 0, 10);
        assertEquals(1, beas.size());
        Conversation view = beas.get(0);
        assertEquals(ANA, view.other().email());
        assertEquals("ana", view.other().username());
        assertEquals("¿qué tal?", view.lastMessage().text());
        assertFalse(view.lastMessage().mine());
        assertEquals(2, view.unread());

        List<ChatMessage> page = chat.findMessages(id, LocalDateTime.now().plusMinutes(1), 1);
        assertEquals(1, page.size());
        assertEquals("¿qué tal?", page.get(0).text());
        assertEquals("hola", chat.findMessages(id, page.get(0).createdAt(), 10).get(0).text());

        chat.markRead(id, BEA, LocalDateTime.now());
        assertEquals(0, chat.countUnreadConversations(BEA));
        assertNotNull(chat.findConversationView(id, ANA).otherReadAt());
    }

    @Test
    void likesPileUpInOneEntryUntilRead() {
        String post = createPost(ANA);
        assertEquals(ANA, notifications.postOwner(post));
        assertNull(notifications.postOwner("4:missing:1"));

        LocalDateTime now = LocalDateTime.now();
        Notification one = notifications.accumulate(ANA, NotificationType.LIKE, post, BEA, null, null, now);
        assertEquals(1, one.actorCount());
        assertEquals("post.png", one.targetImage());

        Notification two = notifications.accumulate(ANA, NotificationType.LIKE, post, CARLA, null, null,
                now.plusSeconds(1));
        assertEquals(one.id(), two.id());
        assertEquals(2, two.actorCount());
        assertEquals(CARLA, two.actors().get(0).email());
        // Liking twice doesn't count twice.
        assertEquals(2, notifications.accumulate(ANA, NotificationType.LIKE, post, CARLA, null, null,
                now.plusSeconds(2)).actorCount());
        assertEquals(1, notifications.countUnread(ANA));

        notifications.retract(ANA, NotificationType.LIKE, post, CARLA);
        assertEquals(1, notifications.findByRecipient(ANA, 0, 10).get(0).actorCount());
        notifications.retract(ANA, NotificationType.LIKE, post, BEA);
        assertTrue(notifications.findByRecipient(ANA, 0, 10).isEmpty());

        Notification again = notifications.accumulate(ANA, NotificationType.LIKE, post, BEA, null, null, now);
        notifications.markRead(ANA, again.id());
        assertEquals(0, notifications.countUnread(ANA));
        // Once read, new likes start a fresh entry.
        Notification fresh = notifications.accumulate(ANA, NotificationType.LIKE, post, CARLA, null, null, now);
        assertNotEquals(again.id(), fresh.id());
        notifications.accumulate(ANA, NotificationType.FOLLOW, ANA, BEA, null, null, now);
        assertEquals(2, notifications.countUnread(ANA));
        notifications.markAllRead(ANA);
        assertEquals(0, notifications.countUnread(ANA));
        assertEquals(3, notifications.findByRecipient(ANA, 0, 10).size());

        assertEquals(3, notifications.deleteOlderThan(LocalDateTime.now().plusDays(1)));
    }

    @Test
    void entriesWithoutActorKeepTheirText() {
        Notification n = notifications.accumulate(ANA, NotificationType.STREAK_RISK, "activity-1", null, "Correr",
                "7", LocalDateTime.now());
        assertEquals(0, n.actorCount());
        assertEquals("Correr", n.preview());
        assertEquals("7", n.extra());
        assertNull(n.targetImage());
    }

    @Test
    void streakAtRiskOnlyWhenThePeriodIsNotDoneYet() {
        LocalDate today = LocalDate.now();
        LocalDateTime yesterday = today.minusDays(1).atTime(12, 0);
        client.query("""
                MATCH (ana:User {email: $a}), (bea:User {email: $b}), (carla:User {email: $c})
                CREATE (act:Activity {name: 'Correr', timeRate: 'daily', timesRequiered: 1})
                CREATE (ana)-[:Participated]->(act), (bea)-[:Participated]->(act), (carla)-[:Participated]->(act)
                // Ana kept it yesterday: at risk.
                CREATE (ana)-[:Posted]->(:Post {createdAt: $yesterday, streak: 4})-[:`Related-To`]->(act)
                // Bea also posted today: safe.
                CREATE (bea)-[:Posted]->(:Post {createdAt: $yesterday, streak: 2})-[:`Related-To`]->(act)
                CREATE (bea)-[:Posted]->(:Post {createdAt: $today, streak: 3})-[:`Related-To`]->(act)
                // Carla has nothing going on: nothing to lose.
                """)
                .bindAll(Map.of("a", ANA, "b", BEA, "c", CARLA, "yesterday", yesterday,
                        "today", today.atTime(0, 30)))
                .run();

        List<StreakAtRisk> atRisk = insights.streaksAtRisk(today).stream()
                .filter(r -> r.timeRate().equals("daily")).toList();
        assertEquals(1, atRisk.size());
        assertEquals(ANA, atRisk.get(0).email());
        assertEquals(4, atRisk.get(0).streak());
        assertEquals(1, atRisk.get(0).remaining());
        assertEquals("Correr", atRisk.get(0).activity());
    }

    @Test
    void devicesAreFoundAndForgotten() {
        devices.save(ANA, "token-1", "es-ES");
        devices.save(ANA, "token-2", "en-US");
        assertEquals(2, devices.findByEmail(ANA).size());
        assertEquals("es-ES", devices.findByEmail(ANA).stream()
                .filter(d -> d.getToken().equals("token-1")).findFirst().orElseThrow().getLanguage());
        devices.delete("token-1");
        devices.delete(" ");
        assertEquals(1, devices.findByEmail(ANA).size());
    }
}
