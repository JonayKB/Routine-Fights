package es.iespuertodelacruz.routinefights.badge.infrastructure.adapters.secondary.repositories;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.iespuertodelacruz.routinefights.badge.infrastructure.adapters.secondary.entities.BadgeEntity;

@Repository
public interface IBadgeEntityRepository extends Neo4jRepository<BadgeEntity, String> {

    List<BadgeEntity> findByCommunityEventId(String communityEventId);

    @Query("MATCH (u:User {email:$email})-[r:Has_Badge]->(b:Badge) RETURN b,r,u")
    List<BadgeEntity> findByUserEmail(@Param("email") String email);

    @Query("MATCH (u:User {email: $userEmail}), (b:Badge) WHERE elementId(b) = $badgeId MERGE (u)-[r:Has_Badge]->(b) RETURN exists((u)-[:Has_Badge]->(b))")
    Boolean addBadgeToUser(@Param("userEmail") String userEmail, @Param("badgeId") String badgeId);

    @Query("UNWIND $userEmail AS email MATCH (u:User {email: email}), (b:Badge) WHERE elementId(b) = $badgeId MERGE (u)-[r:Has_Badge]->(b) RETURN exists((u)-[:Has_Badge]->(b))")
    List<Boolean> addBadgeToUser(@Param("userEmail") List<String> userEmail, @Param("badgeId") String badgeId);

}
