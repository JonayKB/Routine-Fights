package es.iespuertodelacruz.routinefights.badge.commons;

public class BadgeCommons {
    private String image;
    private Integer level;

    public BadgeCommons() {
    }
    public BadgeCommons(String image, Integer level) {
        this.image = image;
        this.level = level;
    }
    public String getImage() {
        return this.image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public Integer getLevel() {
        return this.level;
    }
    public void setLevel(Integer level) {
        this.level = level;
    }
    
    
}
