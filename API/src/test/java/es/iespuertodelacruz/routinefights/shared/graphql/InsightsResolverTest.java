package es.iespuertodelacruz.routinefights.shared.graphql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import es.iespuertodelacruz.routinefights.shared.services.InsightsService;
import es.iespuertodelacruz.routinefights.shared.services.InsightsService.RankingEntry;

class InsightsResolverTest {
    private InsightsService service;
    private InsightsResolver resolver;

    @BeforeEach
    void setUp() {
        service = mock(InsightsService.class);
        resolver = new InsightsResolver(service);
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken("me@example.com", null, List.of()));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void postDaysUsesCurrentUser() {
        when(service.postDays(eq("me@example.com"), eq("a1"), any(LocalDateTime.class)))
                .thenReturn(List.of("2026-11-06"));
        assertEquals(List.of("2026-11-06"), resolver.myPostDaysByActivity("a1", 5000));
        verify(service).postDays(eq("me@example.com"), eq("a1"), any(LocalDateTime.class));
    }

    @Test
    void rankingLimitIsClamped() {
        RankingEntry entry = new RankingEntry("ana", "ana@x.com", null, 300L);
        when(service.eventRanking("e1", InsightsResolver.MAX_RANKING)).thenReturn(List.of(entry));
        assertEquals(List.of(entry), resolver.communityEventRanking("e1", 999));
    }

    @Test
    void bestStreakDelegates() {
        when(service.bestStreak("ana@x.com")).thenReturn(16);
        assertEquals(16, resolver.bestStreakByEmail("ana@x.com"));
    }
}
