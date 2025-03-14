package es.iespuertodelacruz.routinefights.activity.commons;

public enum TimeRateEnum {
    DAILY("daily"),
    WEEKLY("weekly"),
    MONTHLY("monthly"),
    YEARLY("yearly");

    private String value;

    TimeRateEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
