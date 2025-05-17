package es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.secondary.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.iespuertodelacruz.routinefights.communityEvent.infrastructure.adapters.secondary.entities.CommunityEventEntity;

@Repository
public interface ICommunityEventEntityRepository extends Neo4jRepository<CommunityEventEntity, String> {

    @Query("MATCH (ce:CommunityEvent) WHERE ce.finishDate > $currentDate AND ce.startDate < $currentDate RETURN ce")
    List<CommunityEventEntity> getActiveCommunityEvents(@Param("currentDate") LocalDateTime currentDate);

    @Query("MATCH (ce:CommunityEvent) WHERE ce.finishDate > $currentDate RETURN ce ORDER BY ce.startDate ASC LIMIT 1")
    CommunityEventEntity getNearestCommunityEvent(@Param("currentDate") LocalDateTime currentDate);

    @Query("MATCH (ce:CommunityEvent)-[:Related]->(a:Activity)<-[:`Related-To`]-(p:Post) WHERE elementId(ce) = $id AND p.createdAt >= ce.startDate AND p.createdAt <= ce.finishDate RETURN SUM(p.pointsToAdd)")
    Integer getActualCommunityEventPoints(@Param("id") String id);

    @Query("MATCH (ce:CommunityEvent)-[r:Related]->(a:Activity)<-[rr:`Related-To`]-(p:Post)<-[:`Posted`]-(u:User) WHERE elementId(ce) = $id AND p.createdAt >= ce.startDate AND p.createdAt <= ce.finishDate RETURN DISTINCT elementId(u)")
    List<String> getUsersParticipatingInCommunityEvent(@Param("id") String id);

    @Query("MATCH (ce:CommunityEvent) WHERE elementId(ce) = $id RETURN ce")
    CommunityEventEntity findByIdOnlyBase(@Param("id") String id);

    @Query("MATCH (ce:CommunityEvent) RETURN ce.image")
    Set<String> getAllImages();

    @Query("MATCH (ce:CommunityEvent)-[:Related]->(a:Activity)<-[:`Related-To`]-(p:Post)<-[:`Posted`]-(u:User) WHERE p.createdAt >= ce.startDate AND p.createdAt <= ce.finishDate AND date(ce.finishDate)=date($currentDay) RETURN DISTINCT elementId(u)")
    List<String> getUsersParticipatingInCommunityEventEndsToday(@Param("currentDay") LocalDateTime currentDate);

    @Query("MATCH (ce:CommunityEvent) WHERE date(ce.finishDate)=date($currentDay) RETURN ce")
    CommunityEventEntity getCommunityEventEndsToday(@Param("currentDay") LocalDateTime now);

    @Query("""
            CREATE (ce:CommunityEvent {name: $name, createdAt: $createdAt, startDate: $startDate, finishDate: $finishDate, totalRequired: $totalRequired, image: $image})
            WITH ce
            UNWIND $activitiesIDs AS activityID
            MATCH (a:Activity) WHERE elementId(a) = activityID
            CREATE (ce)-[r:Related]->(a)
            RETURN ce, r, a
            """)
    CommunityEventEntity create(@Param("name") String name, @Param("createdAt") LocalDateTime createdAt,
            @Param("startDate") LocalDateTime startDate, @Param("finishDate") LocalDateTime finishDate,
            @Param("totalRequired") Integer totalRequired, @Param("image") String image,
            @Param("activitiesIDs") List<String> activitiesIDs);
}
