package es.iespuertodelacruz.routinefights.shared.utils;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class EntitiesTimestamps {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public EntitiesTimestamps() {
    }

    public EntitiesTimestamps(LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }


    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return this.deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof EntitiesTimestamps)) {
            return false;
        }
        EntitiesTimestamps entitiesTimestamps = (EntitiesTimestamps) o;
        return Objects.equals(createdAt, entitiesTimestamps.createdAt) && Objects.equals(updatedAt, entitiesTimestamps.updatedAt) && Objects.equals(deletedAt, entitiesTimestamps.deletedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdAt, updatedAt, deletedAt);
    }

    @Override
    public String toString() {
        return "{" +
            " createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            "}";
    }
    
}
