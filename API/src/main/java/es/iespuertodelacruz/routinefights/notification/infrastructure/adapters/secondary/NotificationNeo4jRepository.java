package es.iespuertodelacruz.routinefights.notification.infrastructure.adapters.secondary;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.Value;
import org.neo4j.driver.types.MapAccessor;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Repository;

import es.iespuertodelacruz.routinefights.notification.domain.Notification;
import es.iespuertodelacruz.routinefights.notification.domain.ports.secondary.INotificationRepository;
import es.iespuertodelacruz.routinefights.shared.dto.UserSummary;

/**
 * Inbox entries are {@code (:Notification)} nodes linked to their recipient by
 * {@code Notified}; each person who caused one is linked by {@code Acted}.
 */
@Repository
public class NotificationNeo4jRepository implements INotificationRepository {

    private static final String EMAIL = "email";
    private static final String TYPE = "type";
    private static final String TARGET = "targetId";
    private static final String ACTOR = "actor";

    /** Returns one inbox entry as the columns read by {@link #map}. */
    private static final String VIEW = """
            OPTIONAL MATCH (a:User)-[act:Acted]->(n)
            WITH n, a, act ORDER BY act.at DESC
            WITH n, collect(CASE WHEN a IS NULL THEN null
                ELSE {email: a.email, username: a.username, image: a.image} END) AS actors
            OPTIONAL MATCH (p:Post) WHERE n.type IN ['LIKE', 'COMMENT'] AND elementId(p) = n.targetId
            RETURN elementId(n) AS id, n.type AS type, n.targetId AS targetId, n.preview AS preview,
                   n.extra AS extra, p.image AS targetImage, n.updatedAt AS updatedAt, n.read AS read,
                   actors[0..3] AS actors, size(actors) AS actorCount
            """;

    private final Neo4jClient client;

    public NotificationNeo4jRepository(Neo4jClient client) {
        this.client = client;
    }

    @Override
    public Notification accumulate(String recipientEmail, String type, String targetId, String actorEmail,
            String preview, String extra, LocalDateTime at) {
        Map<String, Object> params = new HashMap<>();
        params.put(EMAIL, recipientEmail);
        params.put(TYPE, type);
        params.put(TARGET, targetId);
        params.put(ACTOR, actorEmail);
        params.put("preview", preview);
        params.put("extra", extra);
        params.put("at", at);
        return client.query("""
                MATCH (r:User {email: $email})
                MERGE (r)-[:Notified]->(n:Notification {type: $type, targetId: $targetId, read: false})
                  ON CREATE SET n.createdAt = $at
                SET n.updatedAt = $at,
                    n.preview = coalesce($preview, n.preview),
                    n.extra = coalesce($extra, n.extra)
                WITH n
                OPTIONAL MATCH (actor:User {email: $actor})
                FOREACH (_ IN CASE WHEN actor IS NULL THEN [] ELSE [1] END |
                  MERGE (actor)-[act:Acted]->(n) SET act.at = $at)
                WITH DISTINCT n
                """ + VIEW)
                .bindAll(params)
                .fetchAs(Notification.class)
                .mappedBy((t, row) -> map(row))
                .one()
                .orElse(null);
    }

    @Override
    public void retract(String recipientEmail, String type, String targetId, String actorEmail) {
        client.query("""
                MATCH (:User {email: $email})-[:Notified]->(n:Notification {type: $type, targetId: $targetId, read: false})
                MATCH (:User {email: $actor})-[act:Acted]->(n)
                DELETE act
                WITH n
                WHERE NOT EXISTS { MATCH (:User)-[:Acted]->(n) }
                DETACH DELETE n
                """)
                .bindAll(Map.of(EMAIL, recipientEmail, TYPE, type, TARGET, targetId, ACTOR, actorEmail))
                .run();
    }

    @Override
    public List<Notification> findByRecipient(String email, int skip, int limit) {
        return List.copyOf(client.query("""
                MATCH (:User {email: $email})-[:Notified]->(n:Notification)
                WITH n ORDER BY n.updatedAt DESC SKIP $skip LIMIT $limit
                """ + VIEW + " ORDER BY updatedAt DESC")
                .bindAll(Map.of(EMAIL, email, "skip", skip, "limit", limit))
                .fetchAs(Notification.class)
                .mappedBy((t, row) -> map(row))
                .all());
    }

    @Override
    public int countUnread(String email) {
        return client.query("""
                MATCH (:User {email: $email})-[:Notified]->(n:Notification {read: false})
                RETURN count(n) AS total
                """)
                .bind(email).to(EMAIL)
                .fetchAs(Long.class)
                .mappedBy((t, row) -> row.get("total").asLong())
                .one()
                .orElse(0L)
                .intValue();
    }

    @Override
    public void markAllRead(String email) {
        client.query("""
                MATCH (:User {email: $email})-[:Notified]->(n:Notification {read: false})
                SET n.read = true
                """)
                .bind(email).to(EMAIL)
                .run();
    }

    @Override
    public void markRead(String email, String id) {
        client.query("""
                MATCH (:User {email: $email})-[:Notified]->(n:Notification)
                WHERE elementId(n) = $id
                SET n.read = true
                """)
                .bindAll(Map.of(EMAIL, email, "id", id))
                .run();
    }

    @Override
    public String postOwner(String postId) {
        return client.query("""
                MATCH (u:User)-[:Posted]->(p:Post)
                WHERE elementId(p) = $id
                RETURN u.email AS email
                LIMIT 1
                """)
                .bind(postId).to("id")
                .fetchAs(String.class)
                .mappedBy((t, row) -> row.get(EMAIL).asString(null))
                .one()
                .orElse(null);
    }

    @Override
    public long deleteOlderThan(LocalDateTime limit) {
        return client.query("""
                MATCH (n:Notification) WHERE n.updatedAt < $limit
                DETACH DELETE n
                RETURN count(*) AS deleted
                """)
                .bind(limit).to("limit")
                .fetchAs(Long.class)
                .mappedBy((t, row) -> row.get("deleted").asLong())
                .one()
                .orElse(0L);
    }

    static Notification map(MapAccessor row) {
        List<UserSummary> actors = row.get("actors").asList(NotificationNeo4jRepository::toUser);
        Value updatedAt = row.get("updatedAt");
        return new Notification(
                row.get("id").asString(),
                row.get(TYPE).asString(),
                row.get(TARGET).asString(null),
                actors,
                row.get("actorCount").asInt(0),
                row.get("preview").asString(null),
                row.get("extra").asString(null),
                row.get("targetImage").asString(null),
                updatedAt.isNull() ? null : updatedAt.asLocalDateTime(),
                row.get("read").asBoolean(false));
    }

    private static UserSummary toUser(Value value) {
        return new UserSummary(value.get(EMAIL).asString(null), value.get("username").asString(null),
                value.get("image").asString(null));
    }
}
