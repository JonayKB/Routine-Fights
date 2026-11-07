package es.iespuertodelacruz.routinefights.shared.graphql;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import es.iespuertodelacruz.routinefights.shared.services.InsightsService;
import es.iespuertodelacruz.routinefights.shared.services.InsightsService.RankingEntry;

@Controller
public class InsightsResolver {

    static final int MAX_DAYS = 366;
    static final int MAX_RANKING = 50;

    private final InsightsService insightsService;

    public InsightsResolver(InsightsService insightsService) {
        this.insightsService = insightsService;
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @QueryMapping("myPostDaysByActivity")
    public List<String> myPostDaysByActivity(@Argument String activityID, @Argument Integer days) {
        int window = Math.max(1, Math.min(days == null ? 84 : days, MAX_DAYS));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return insightsService.postDays(email, activityID, LocalDateTime.now().minusDays(window));
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @QueryMapping("bestStreakByEmail")
    public int bestStreakByEmail(@Argument String email) {
        return insightsService.bestStreak(email);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @QueryMapping("communityEventRanking")
    public List<RankingEntry> communityEventRanking(@Argument String id, @Argument Integer limit) {
        int size = Math.max(1, Math.min(limit == null ? 10 : limit, MAX_RANKING));
        return insightsService.eventRanking(id, size);
    }
}
