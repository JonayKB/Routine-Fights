package es.iespuertodelacruz.routinefights.shared.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.activity.commons.TimeRates;
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

    public record StreakAtRisk(String email, String activityId, String activity, long streak, long remaining,
            String timeRate) {
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
     * Live streaks that go out when the current period ends today: the user kept
     * the previous period (or already started this one) but hasn't posted the
     * required times yet. Only activities whose period ends on {@code today} are
     * checked (daily always, weekly on Sundays, monthly on the last day...).
     */
    public List<StreakAtRisk> streaksAtRisk(LocalDate today) {
        List<StreakAtRisk> result = new ArrayList<>();
        for (String timeRate : ratesEndingOn(today)) {
            LocalDate[] current = periodOf(timeRate, today);
            LocalDate[] previous = periodOf(timeRate, current[0].minusDays(1));
            result.addAll(neo4jClient.query("""
                    MATCH (u:User)-[:Participated]->(a:Activity {timeRate: $timeRate})
                    WHERE u.deletedAt IS NULL
                    OPTIONAL MATCH (u)-[:Posted]->(p:Post)-[:`Related-To`]->(a)
                    WHERE p.createdAt >= $previousStart AND p.createdAt < $nextStart
                    WITH u, a, coalesce(a.timesRequiered, 1) AS required,
                         [x IN collect(p) WHERE x.createdAt >= $currentStart] AS cur,
                         [x IN collect(p) WHERE x.createdAt < $currentStart] AS prev,
                         collect(p) AS recent
                    WHERE size(cur) < required AND (size(prev) >= required OR size(cur) > 0)
                    WITH u, a, required - size(cur) AS remaining,
                         reduce(best = 0, x IN recent | CASE WHEN coalesce(x.streak, 0) > best
                             THEN x.streak ELSE best END) AS streak
                    WHERE streak > 0
                    RETURN u.email AS email, elementId(a) AS activityId, a.name AS activity,
                           streak, remaining
                    """)
                    .bindAll(Map.of("timeRate", timeRate,
                            "previousStart", previous[0].atStartOfDay(),
                            "currentStart", current[0].atStartOfDay(),
                            "nextStart", current[1].plusDays(1).atStartOfDay()))
                    .fetchAs(StreakAtRisk.class)
                    .mappedBy((type, row) -> new StreakAtRisk(row.get("email").asString(),
                            row.get("activityId").asString(), row.get("activity").asString(""),
                            row.get("streak").asLong(0), row.get("remaining").asLong(1), timeRate))
                    .all());
        }
        return result;
    }

    /** Time rates whose current period ends on {@code day}. */
    static List<String> ratesEndingOn(LocalDate day) {
        List<String> rates = new ArrayList<>();
        rates.add(TimeRates.DAILY);
        if (day.getDayOfWeek() == DayOfWeek.SUNDAY) {
            rates.add(TimeRates.WEEKLY);
        }
        if (day.getDayOfMonth() == day.lengthOfMonth()) {
            rates.add(TimeRates.MONTHLY);
        }
        if (day.getDayOfYear() == day.lengthOfYear()) {
            rates.add(TimeRates.YEARLY);
        }
        return rates;
    }

    /** First and last day of the period of that time rate containing {@code day}. */
    static LocalDate[] periodOf(String timeRate, LocalDate day) {
        return switch (timeRate) {
            case TimeRates.WEEKLY -> new LocalDate[] { day.with(DayOfWeek.MONDAY), day.with(DayOfWeek.SUNDAY) };
            case TimeRates.MONTHLY -> new LocalDate[] { day.withDayOfMonth(1), day.withDayOfMonth(day.lengthOfMonth()) };
            case TimeRates.YEARLY -> new LocalDate[] { day.withDayOfYear(1), day.withDayOfYear(day.lengthOfYear()) };
            default -> new LocalDate[] { day, day };
        };
    }
}
