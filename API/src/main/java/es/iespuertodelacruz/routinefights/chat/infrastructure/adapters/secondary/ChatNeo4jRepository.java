package es.iespuertodelacruz.routinefights.chat.infrastructure.adapters.secondary;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.Value;
import org.neo4j.driver.types.MapAccessor;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Repository;

import es.iespuertodelacruz.routinefights.chat.domain.ChatMessage;
import es.iespuertodelacruz.routinefights.chat.domain.Conversation;
import es.iespuertodelacruz.routinefights.chat.domain.ports.secondary.IChatRepository;
import es.iespuertodelacruz.routinefights.shared.dto.UserSummary;

/**
 * {@code (:User)-[:Member {lastReadAt}]->(:Conversation)<-[:In]-(:Message)<-[:Sent]-(:User)}.
 * A conversation has exactly two members and a {@code key} made of both user
 * ids, so the same pair can never get two chats.
 */
@Repository
public class ChatNeo4jRepository implements IChatRepository {

    private static final String EMAIL = "email";
    private static final String OTHER = "other";
    private static final String ID = "id";
    private static final String TOTAL = "total";

    /** Columns read by {@link #mapConversation}, from me, mine, c, theirs and o. */
    private static final String CONVERSATION_VIEW = """
            OPTIONAL MATCH (s:User)-[:Sent]->(m:Message)-[:In]->(c)
            WITH me, mine, c, theirs, o, m, s ORDER BY m.createdAt DESC
            WITH me, mine, c, theirs, o, head(collect(CASE WHEN m IS NULL THEN null
                ELSE {id: elementId(m), text: m.text, sender: s.email, createdAt: m.createdAt} END)) AS last
            RETURN elementId(c) AS id, o.email AS otherEmail, o.username AS otherUsername, o.image AS otherImage,
                   last, c.updatedAt AS updatedAt, theirs.lastReadAt AS otherReadAt,
                   COUNT { MATCH (o)-[:Sent]->(x:Message)-[:In]->(c)
                           WHERE mine.lastReadAt IS NULL OR x.createdAt > mine.lastReadAt } AS unread
            """;

    private final Neo4jClient client;

    public ChatNeo4jRepository(Neo4jClient client) {
        this.client = client;
    }

    @Override
    public String findOrCreateConversation(String email, String otherEmail, LocalDateTime at) {
        return client.query("""
                MATCH (a:User {email: $email}), (b:User {email: $other})
                WHERE b.deletedAt IS NULL
                WITH a, b, CASE WHEN elementId(a) < elementId(b)
                    THEN elementId(a) + '|' + elementId(b) ELSE elementId(b) + '|' + elementId(a) END AS key
                MERGE (c:Conversation {key: key})
                  ON CREATE SET c.createdAt = $at, c.updatedAt = $at
                MERGE (a)-[:Member]->(c)
                MERGE (b)-[:Member]->(c)
                RETURN elementId(c) AS id
                """)
                .bindAll(Map.of(EMAIL, email, OTHER, otherEmail, "at", at))
                .fetchAs(String.class)
                .mappedBy((t, row) -> row.get(ID).asString())
                .one()
                .orElse(null);
    }

    @Override
    public String findConversation(String email, String otherEmail) {
        return client.query("""
                MATCH (:User {email: $email})-[:Member]->(c:Conversation)<-[:Member]-(:User {email: $other})
                RETURN elementId(c) AS id
                LIMIT 1
                """)
                .bindAll(Map.of(EMAIL, email, OTHER, otherEmail))
                .fetchAs(String.class)
                .mappedBy((t, row) -> row.get(ID).asString())
                .one()
                .orElse(null);
    }

    @Override
    public boolean isMember(String conversationId, String email) {
        return count("""
                MATCH (:User {email: $email})-[:Member]->(c:Conversation)
                WHERE elementId(c) = $id
                RETURN count(c) AS total
                """, Map.of(EMAIL, email, ID, conversationId)) > 0;
    }

    @Override
    public String otherMember(String conversationId, String email) {
        return client.query("""
                MATCH (:User {email: $email})-[:Member]->(c:Conversation)<-[:Member]-(o:User)
                WHERE elementId(c) = $id
                RETURN o.email AS email
                LIMIT 1
                """)
                .bindAll(Map.of(EMAIL, email, ID, conversationId))
                .fetchAs(String.class)
                .mappedBy((t, row) -> row.get(EMAIL).asString(null))
                .one()
                .orElse(null);
    }

    @Override
    public ChatMessage saveMessage(String conversationId, String senderEmail, String text, LocalDateTime at) {
        return client.query("""
                MATCH (s:User {email: $email})-[:Member]->(c:Conversation)
                WHERE elementId(c) = $id
                CREATE (s)-[:Sent]->(m:Message {text: $text, createdAt: $at})-[:In]->(c)
                SET c.updatedAt = $at
                RETURN elementId(m) AS id, elementId(c) AS conversationId, m.text AS text,
                       s.email AS sender, m.createdAt AS createdAt
                """)
                .bindAll(Map.of(EMAIL, senderEmail, ID, conversationId, "text", text, "at", at))
                .fetchAs(ChatMessage.class)
                .mappedBy((t, row) -> mapMessage(row))
                .one()
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));
    }

    @Override
    public List<Conversation> findConversations(String email, int skip, int limit) {
        return List.copyOf(client.query("""
                MATCH (me:User {email: $email})-[mine:Member]->(c:Conversation)<-[theirs:Member]-(o:User)
                WHERE EXISTS { MATCH (:Message)-[:In]->(c) }
                WITH me, mine, c, theirs, o ORDER BY c.updatedAt DESC SKIP $skip LIMIT $limit
                """ + CONVERSATION_VIEW + " ORDER BY updatedAt DESC")
                .bindAll(Map.of(EMAIL, email, "skip", skip, "limit", limit))
                .fetchAs(Conversation.class)
                .mappedBy((t, row) -> mapConversation(row, email))
                .all());
    }

    @Override
    public Conversation findConversationView(String conversationId, String email) {
        return client.query("""
                MATCH (me:User {email: $email})-[mine:Member]->(c:Conversation)<-[theirs:Member]-(o:User)
                WHERE elementId(c) = $id
                """ + CONVERSATION_VIEW)
                .bindAll(Map.of(EMAIL, email, ID, conversationId))
                .fetchAs(Conversation.class)
                .mappedBy((t, row) -> mapConversation(row, email))
                .one()
                .orElse(null);
    }

    @Override
    public List<ChatMessage> findMessages(String conversationId, LocalDateTime before, int limit) {
        return List.copyOf(client.query("""
                MATCH (s:User)-[:Sent]->(m:Message)-[:In]->(c:Conversation)
                WHERE elementId(c) = $id AND m.createdAt < $before
                RETURN elementId(m) AS id, elementId(c) AS conversationId, m.text AS text,
                       s.email AS sender, m.createdAt AS createdAt
                ORDER BY m.createdAt DESC
                LIMIT $limit
                """)
                .bindAll(Map.of(ID, conversationId, "before", before, "limit", limit))
                .fetchAs(ChatMessage.class)
                .mappedBy((t, row) -> mapMessage(row))
                .all());
    }

    @Override
    public void markRead(String conversationId, String email, LocalDateTime at) {
        client.query("""
                MATCH (:User {email: $email})-[mine:Member]->(c:Conversation)
                WHERE elementId(c) = $id
                SET mine.lastReadAt = $at
                """)
                .bindAll(Map.of(EMAIL, email, ID, conversationId, "at", at))
                .run();
    }

    @Override
    public int countUnreadMessages(String conversationId, String email) {
        return (int) count("""
                MATCH (:User {email: $email})-[mine:Member]->(c:Conversation)<-[:Member]-(o:User)
                WHERE elementId(c) = $id
                MATCH (o)-[:Sent]->(x:Message)-[:In]->(c)
                WHERE mine.lastReadAt IS NULL OR x.createdAt > mine.lastReadAt
                RETURN count(x) AS total
                """, Map.of(EMAIL, email, ID, conversationId));
    }

    @Override
    public int countUnreadConversations(String email) {
        return (int) count("""
                MATCH (:User {email: $email})-[mine:Member]->(c:Conversation)<-[:Member]-(o:User)
                WHERE EXISTS { MATCH (o)-[:Sent]->(x:Message)-[:In]->(c)
                               WHERE mine.lastReadAt IS NULL OR x.createdAt > mine.lastReadAt }
                RETURN count(DISTINCT c) AS total
                """, Map.of(EMAIL, email));
    }

    private long count(String cypher, Map<String, Object> params) {
        return client.query(cypher)
                .bindAll(new HashMap<>(params))
                .fetchAs(Long.class)
                .mappedBy((t, row) -> row.get(TOTAL).asLong())
                .one()
                .orElse(0L);
    }

    static ChatMessage mapMessage(MapAccessor row) {
        return new ChatMessage(row.get(ID).asString(), row.get("conversationId").asString(null),
                row.get("text").asString(""), row.get("sender").asString(null), dateOf(row.get("createdAt")),
                false);
    }

    static Conversation mapConversation(MapAccessor row, String readerEmail) {
        Value last = row.get("last");
        ChatMessage lastMessage = null;
        String id = row.get(ID).asString();
        if (!last.isNull()) {
            lastMessage = new ChatMessage(last.get(ID).asString(), id, last.get("text").asString(""),
                    last.get("sender").asString(null), dateOf(last.get("createdAt")), false)
                    .forReader(readerEmail);
        }
        UserSummary other = new UserSummary(row.get("otherEmail").asString(null),
                row.get("otherUsername").asString(null), row.get("otherImage").asString(null));
        return new Conversation(id, other, lastMessage, row.get("unread").asInt(0), dateOf(row.get("updatedAt")),
                dateOf(row.get("otherReadAt")));
    }

    private static LocalDateTime dateOf(Value value) {
        return value == null || value.isNull() ? null : value.asLocalDateTime();
    }
}
