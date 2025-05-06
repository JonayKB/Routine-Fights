package es.iespuertodelacruz.routinefights.shared.utils;

import java.time.LocalDateTime;

import es.iespuertodelacruz.routinefights.activity.commons.TimeRates;
import es.iespuertodelacruz.routinefights.post.exceptions.UploadPostException;

public class TimeRatesDate {
    public LocalDateTime[] getLastIterationDates(String timerate) {
        LocalDateTime start;
        LocalDateTime end;

        switch (timerate) {
            case TimeRates.DAILY:
                start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
                        .minusDays(1);
                end = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999)
                        .minusDays(1);
                break;
            case TimeRates.WEEKLY:
                start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
                        .minusWeeks(1).with(java.time.DayOfWeek.MONDAY);
                end = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999)
                        .minusWeeks(1).with(java.time.DayOfWeek.SUNDAY);
                break;
            case TimeRates.MONTHLY:
                start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
                        .minusMonths(1).withDayOfMonth(1);
                end = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999)
                        .minusMonths(1)
                        .withDayOfMonth(LocalDateTime.now().minusMonths(1).toLocalDate().lengthOfMonth());
                break;
            case TimeRates.YEARLY:
                start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
                        .minusYears(1).withDayOfYear(1);
                end = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999)
                        .minusYears(1)
                        .withDayOfYear(LocalDateTime.now().minusYears(1).toLocalDate().lengthOfYear());
                break;
            default:
                throw new UploadPostException("Invalid time rate");
        }

        return new LocalDateTime[] { start, end };
    }

    public LocalDateTime[] getActualIterationDates(String timerate) {
        LocalDateTime start;
        LocalDateTime end;

        switch (timerate) {
            case TimeRates.DAILY:
                start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
                end = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);
                break;
            case TimeRates.WEEKLY:
                start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
                        .with(java.time.DayOfWeek.MONDAY);
                end = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999)
                        .with(java.time.DayOfWeek.SUNDAY);
                break;
            case TimeRates.MONTHLY:
                start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
                        .withDayOfMonth(1);
                end = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999)
                        .withDayOfMonth(LocalDateTime.now().toLocalDate().lengthOfMonth());
                break;
            case TimeRates.YEARLY:
                start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
                        .withDayOfYear(1);
                end = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999)
                        .withDayOfYear(LocalDateTime.now().toLocalDate().lengthOfYear());
                break;
            default:
                throw new UploadPostException("Invalid time rate");
        }

        return new LocalDateTime[] { start, end };
    }
}
