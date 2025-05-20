package es.iespuertodelacruz.routinefights.badge.infrastructure.adapters.secondary.repositories;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.iespuertodelacruz.routinefights.badge.infrastructure.adapters.secondary.entities.BadgeEntity;

@Repository
public interface IBadgeEntityRepository extends Neo4jRepository<BadgeEntity, String> {

    @Query("MATCH (b:Badge)-[r:Associated_With]->(c:CommunityEvent) WHERE elementId(c) = $communityEventId RETURN b,r,c")
    List<BadgeEntity> findByCommunityEventId(@Param("communityEventId") String communityEventId);

    @Query("MATCH (u:User {email:$email})-[r:Has_Badge]->(b:Badge)-[a:Associated_With]->(c:CommunityEvent) RETURN b,r,u,a,c")
    List<BadgeEntity> findByUserEmail(@Param("email") String email);

    @Query("MATCH (u:User {email:$userEmail}) MATCH (b:Badge) WHERE elementId(b) = $badgeId MERGE (u)-[r:Has_Badge]->(b) RETURN exists((u)-[:Has_Badge]->(b))")
    Boolean addBadgeToUser(@Param("userEmail") String userEmail, @Param("badgeId") String badgeId);

    @Query("UNWIND $userIds AS userEmail MATCH (u:User{email:userEmail}) MATCH (b:Badge) WHERE elementId(b) = $badgeId MERGE (u)-[r:Has_Badge]->(b) RETURN exists((u)-[:Has_Badge]->(b))")
    List<Boolean> addBadgeToUser(@Param("userEmails") List<String> userEmail, @Param("badgeId") String badgeId);

    @Query("MATCH (c:CommunityEvent) WHERE elementId(c) = $communityEventId CREATE (b:Badge {image: $image, level: $level})-[r:Associated_With]->(c) RETURN b,r,c")
    BadgeEntity create(@Param("image") String image, @Param("level") Integer level, @Param("communityEventId") String communityEventId);

}
