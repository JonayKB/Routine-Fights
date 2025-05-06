package es.iespuertodelacruz.routinefights.post.commons;

import java.time.LocalDateTime;


import es.iespuertodelacruz.routinefights.shared.utils.EntitiesTimestamps;


public abstract class PostCommons extends EntitiesTimestamps {
    private String image;

    private Integer streak;
    private Integer pointsToAdd;
    private LocalDateTime filedAt;

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getStreak() {
        return this.streak;
    }

    public void setStreak(Integer streak) {
        this.streak = streak;
    }

    public Integer getPointsToAdd() {
        return this.pointsToAdd;
    }

    public void setPointsToAdd(Integer pointsToAdd) {
        this.pointsToAdd = pointsToAdd;
    }

    public LocalDateTime getFiledAt() {
        return this.filedAt;
    }

    public void setFiledAt(LocalDateTime filedAt) {
        this.filedAt = filedAt;
    }

    protected PostCommons() {
    }

    protected PostCommons(String image, Integer streak, Integer pointsToAdd, LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime deletedAt, LocalDateTime filedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.image = image;
        this.streak = streak;
        this.pointsToAdd = pointsToAdd;
        this.filedAt = filedAt;
    }

}
