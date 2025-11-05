package es.iespuertodelacruz.routinefights.device_token.infrastructure.adapters.secondary.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;

@Node("DeviceToken")
public class DeviceTokenEntity {
    @Id
    private String token;
    private String language;
    @Relationship(type = "Has_Device", direction = Relationship.Direction.INCOMING)
    private UserEntity user;

    public DeviceTokenEntity() {
    }

    public DeviceTokenEntity(String token, String language, UserEntity user) {
        this.token = token;
        this.language = language;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
