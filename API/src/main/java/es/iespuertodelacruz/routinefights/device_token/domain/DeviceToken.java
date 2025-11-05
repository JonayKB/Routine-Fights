package es.iespuertodelacruz.routinefights.device_token.domain;

import es.iespuertodelacruz.routinefights.user.domain.User;

public class DeviceToken {
    private String token;
    private String language;
    private User user;

    public DeviceToken() {
    }

    public DeviceToken(String token, User user, String language) {
        this.token = token;
        this.user = user;
        this.language = language;
    }

    public String getToken() {
        return token;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
