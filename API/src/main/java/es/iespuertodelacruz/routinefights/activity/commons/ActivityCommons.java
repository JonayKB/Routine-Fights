package es.iespuertodelacruz.routinefights.activity.commons;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Transient;

import es.iespuertodelacruz.routinefights.shared.utils.EntitiesTimestamps;

public abstract class ActivityCommons extends EntitiesTimestamps {
    private String name;
    private String description;
    private String image;
    private String timeRate;
    private Integer timesRequiered;
    private Integer streak;
    @Transient
    private Integer timesRemaining;

    

    protected ActivityCommons(String name, String description, String image, String timeRate, Integer timesRequiered,
            LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.name = name;
        this.description = description;
        this.image = image;
        this.timeRate = timeRate;
        this.timesRequiered = timesRequiered;

    }

    protected ActivityCommons() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTimeRate() {
        return this.timeRate;
    }

    public void setTimeRate(String timeRate) {
        this.timeRate = timeRate;
    }

    public Integer getTimesRequiered() {
        return this.timesRequiered;
    }

    public void setTimesRequiered(Integer timesRequiered) {
        this.timesRequiered = timesRequiered;
    }
    public Integer getStreak() {
        return this.streak;
    }
    public void setStreak(Integer streak) {
        this.streak = streak;
    }
    public Integer getTimesRemaining() {
        return this.timesRemaining;
    }
    public void setTimesRemaining(Integer timesRemaining) {
        this.timesRemaining = timesRemaining;
    }
}
