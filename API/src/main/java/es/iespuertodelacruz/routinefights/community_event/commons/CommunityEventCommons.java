package es.iespuertodelacruz.routinefights.community_event.commons;

import java.time.LocalDateTime;

import es.iespuertodelacruz.routinefights.shared.utils.EntitiesTimestamps;

public class CommunityEventCommons extends EntitiesTimestamps {
    private String name;
    private Integer totalRequired;
    private LocalDateTime startDate;
    private LocalDateTime finishDate;
    private String image;

    public CommunityEventCommons() {
    }
    public CommunityEventCommons(String name, Integer totalRequired, LocalDateTime startDate, LocalDateTime finishDate,
            LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt, String image) {
        super(createdAt, updatedAt, deletedAt);
        this.name = name;
        this.totalRequired = totalRequired;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.image = image;
    }


    public CommunityEventCommons(String name, Integer totalRequired, LocalDateTime startDate, LocalDateTime finishDate) {
        this.name = name;
        this.totalRequired = totalRequired;
        this.startDate = startDate;
        this.finishDate = finishDate;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalRequired() {
        return this.totalRequired;
    }

    public void setTotalRequired(Integer totalRequired) {
        this.totalRequired = totalRequired;
    }

    public LocalDateTime getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getFinishDate() {
        return this.finishDate;
    }

    public void setFinishDate(LocalDateTime finishDate) {
        this.finishDate = finishDate;
    }
    public String getImage() {
        return this.image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public CommunityEventCommons name(String name) {
        setName(name);
        return this;
    }

    public CommunityEventCommons totalRequired(Integer totalRequired) {
        setTotalRequired(totalRequired);
        return this;
    }

    public CommunityEventCommons startDate(LocalDateTime startDate) {
        setStartDate(startDate);
        return this;
    }

    public CommunityEventCommons finishDate(LocalDateTime finishDate) {
        setFinishDate(finishDate);
        return this;
    }


    @Override
    public String toString() {
        return "{" +
            " name='" + getName() + "'" +
            ", totalRequired='" + getTotalRequired() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", finishDate='" + getFinishDate() + "'" +
            "}";
    }
    
}
