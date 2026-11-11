package es.iespuertodelacruz.routinefights.shared.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

class InsightsServicePeriodsTest {

    @Test
    void ratesEndingOnADay() {
        assertEquals(List.of("daily"), InsightsService.ratesEndingOn(LocalDate.of(2026, 11, 11)));
        assertEquals(List.of("daily", "weekly"), InsightsService.ratesEndingOn(LocalDate.of(2026, 11, 15)));
        assertEquals(List.of("daily", "monthly"), InsightsService.ratesEndingOn(LocalDate.of(2026, 11, 30)));
        assertEquals(List.of("daily", "monthly", "yearly"),
                InsightsService.ratesEndingOn(LocalDate.of(2025, 12, 31)));
    }

    @Test
    void periods() {
        LocalDate wed = LocalDate.of(2026, 11, 11);
        assertEquals(wed, InsightsService.periodOf("daily", wed)[0]);
        assertEquals(LocalDate.of(2026, 11, 9), InsightsService.periodOf("weekly", wed)[0]);
        assertEquals(LocalDate.of(2026, 11, 15), InsightsService.periodOf("weekly", wed)[1]);
        assertEquals(LocalDate.of(2026, 11, 30), InsightsService.periodOf("monthly", wed)[1]);
        assertEquals(LocalDate.of(2026, 1, 1), InsightsService.periodOf("yearly", wed)[0]);
    }
}
