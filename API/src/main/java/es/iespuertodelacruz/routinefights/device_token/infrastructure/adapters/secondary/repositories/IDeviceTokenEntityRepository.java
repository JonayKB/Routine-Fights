package es.iespuertodelacruz.routinefights.device_token.infrastructure.adapters.secondary.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.iespuertodelacruz.routinefights.device_token.infrastructure.adapters.secondary.entities.DeviceTokenEntity;

@Repository
public interface IDeviceTokenEntityRepository extends Neo4jRepository<DeviceTokenEntity, String> {

    /**
     * Stores a device token once and links it to its current owner. A device that
     * changes account is moved to the new user instead of being duplicated.
     */
    @Query("""
            MATCH (u:User {email: $email})
            MERGE (d:DeviceToken {token: $token})
            SET d.language = $language
            WITH u, d
            OPTIONAL MATCH (:User)-[old:Has_Device]->(d)
            DELETE old
            WITH u, d
            MERGE (u)-[:Has_Device]->(d)
            RETURN d.token
            """)
    String saveForUser(@Param("email") String email, @Param("token") String token,
            @Param("language") String language);
}
