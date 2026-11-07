package es.iespuertodelacruz.routinefights.shared.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.activity.domain.Activity;
import es.iespuertodelacruz.routinefights.activity.domain.ports.primary.IActivityService;
import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.domain.ports.primary.IUserService;

/**
 * Read-only queries that power the streak calendar, profile flame, event
 * ranking and streak-at-risk reminders.
 */
@Service
public class InsightsService {

    public record RankingEntry(String username, String email, String image, Long points) {
    }

    public record StreakAtRisk(String token, String language, String activity, Long streak) {
    }

    private final Neo4jClient neo4jClient;
    private final IUserService userService;
    private final IActivityService activityService;

    public InsightsService(Neo4jClient neo4jClient, IUserService userService, IActivityService activityService) {
        this.neo4jClient = neo4jClient;
        this.userService = userService;
        this.activityService = activityService;
    }

    /** Distinct days (yyyy-MM-dd) on which the user posted in the activity since {@code since}. */
    public List<String> postDays(String email, String activityID, LocalDateTime since) {
        return List.copyOf(neo4jClient.query("""
                MATCH (u:User {email: $email})-[:Posted]->(p:Post)-[:`Related-To`]->(a:Activity)
                WHERE elementId(a) = $activityID AND p.createdAt >= $since
                RETURN DISTINCT toString(date(p.createdAt)) AS day
                ORDER BY day
                """)
                .bindAll(Map.of("email", email, "activityID", activityID, "since", since))
                .fetchAs(String.class)
                .mappedBy((type, row) -> row.get("day").asString())
                .all());
    }

    /** The hottest current streak among the user's activities, 0 when there is none. */
    public int bestStreak(String email) {
        User user = userService.findByEmailOnlyBase(email);
        if (user == null) {
            return 0;
        }
        return activityService.getSubscribedActivitiesWithStreak(user.getId()).stream()
                .map(Activity::getStreak)
                .filter(s -> s != null)
                .max(Integer::compare)
                .orElse(0);
    }

    /** Users who contributed most points to a community event. */
    public List<RankingEntry> eventRanking(String eventID, int limit) {
        return List.copyOf(neo4jClient.query("""
                MATCH (ce:CommunityEvent)-[:Related]->(:Activity)<-[:`Related-To`]-(p:Post)<-[:Posted]-(u:User)
                WHERE elementId(ce) = $id AND p.createdAt >= ce.startDate AND p.createdAt <= ce.finishDate
                RETURN u.username AS username, u.email AS email, u.image AS image,
                       sum(coalesce(p.pointsToAdd, 0)) AS points
                ORDER BY points DESC, username ASC
                LIMIT $limit
                """)
                .bindAll(Map.of("id", eventID, "limit", limit))
                .fetchAs(RankingEntry.class)
                .mappedBy((type, row) -> new RankingEntry(row.get("username").asString(null),
                        row.get("email").asString(null), row.get("image").asString(null),
                        row.get("points").asLong(0)))
                .all());
    }

    /**
     * Devices whose owner kept a daily streak alive yesterday but has not posted
     * in that activity today.
     */
    public List<StreakAtRisk> dailyStreaksAtRisk(LocalDate today) {
        LocalDateTime todayStart = today.atStartOfDay();
        LocalDateTime yesterdayStart = todayStart.minusDays(1);
        return List.copyOf(neo4jClient.query("""
                MATCH (u:User)-[:Participated]->(a:Activity {timeRate: 'daily'})
                WHERE u.deletedAt IS NULL
                MATCH (u)-[:Posted]->(p:Post)-[:`Related-To`]->(a)
                WITH u, a, p ORDER BY p.createdAt DESC
                WITH u, a, collect(p)[0] AS last
                WHERE last.createdAt >= $yesterdayStart AND last.createdAt < $todayStart
                MATCH (u)-[:Has_Device]->(d:DeviceToken)
                RETURN d.token AS token, d.language AS language, a.name AS activity, last.streak AS streak
                """)
                .bindAll(Map.of("yesterdayStart", yesterdayStart, "todayStart", todayStart))
                .fetchAs(StreakAtRisk.class)
                .mappedBy((type, row) -> new StreakAtRisk(row.get("token").asString(),
                        row.get("language").asString("en-US"), row.get("activity").asString(""),
                        row.get("streak").asLong(0)))
                .all());
    }
}
