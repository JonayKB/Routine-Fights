package es.iespuertodelacruz.routinefights.post.infrastructure.adapters.secondary.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.iespuertodelacruz.routinefights.post.infrastructure.adapters.secondary.entities.PostEntity;

@Repository
public interface IPostEntityRepository extends Neo4jRepository<PostEntity, String> {
    @Query("""
            MATCH (p:Post)
            WHERE p.createdAt < $lastDate
            RETURN p
            ORDER BY p.createdAt DESC
            LIMIT $limit
            """)
    List<PostEntity> getPagination(@Param("lastDate") LocalDateTime lastDate, @Param("limit") int limit);

    @Query("""
            MATCH (u:User)-[pp:`Posted`]->(p:Post)
            WHERE elementId(u) = $userID
              AND p.createdAt < $lastDate
            RETURN p,u,pp
            ORDER BY p.createdAt DESC
            LIMIT $limit
            """)
    List<PostEntity> getPaginationByUser(@Param("lastDate") LocalDateTime lastDate, @Param("limit") int limit,
            @Param("userID") String userID);

    List<PostEntity> getPaginationByActivity(@Param("lastDate") LocalDateTime lastDate, @Param("limit") int limit,
            @Param("activityID") String activityID);

    @Query("""
            MATCH (u:User)-[:`Follows`]->(u2:User)-[pp:`Posted`]->(p:Post)
            WHERE elementId(u) = $userID
              AND p.createdAt < $lastDate
            RETURN p,u2,pp
            ORDER BY p.createdAt DESC
            LIMIT $limit
            """)
    List<PostEntity> getPaginationFollowing(@Param("lastDate") LocalDateTime lastDate, @Param("limit") int limit,
            @Param("userID") String userID);

    @Query("""
            MATCH (u:User)-[pp:`Participated`]->(a:Activity)<-[:`Related-To`]-(p:Post)<-[r:Posted]-(u2:User)
            WHERE elementId(u) = $userID
              AND p.createdAt < $lastDate
            RETURN p,r,u2
            ORDER BY p.createdAt DESC
            LIMIT $limit
            """)
    List<PostEntity> getPaginationSubscribedActivities(@Param("lastDate") LocalDateTime lastDate,
            @Param("limit") int limit,
            @Param("userID") String userID);

    @Query("""
            MATCH (a:Activity)<-[rr:`Related-To`]-(p:Post)<-[r:Posted]-(u:User)
                    WHERE elementId(a) = $activityID
                      AND p.createdAt >= $startDate
                      AND p.createdAt < $endDate
                      AND elementId(u) = $userID
                    RETURN p, u, r
                                """)
    List<PostEntity> getRangeByActivity(@Param("activityID") String activityID, @Param("userID") String userID,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("""
            MATCH (a:Activity)<-[rr:`Related-To`]-(p:Post)<-[r:Posted]-(u:User)
            WHERE elementId(a)= $activityID
              AND p.createdAt >= $startDate
              AND p.createdAt < $endDate
                AND elementId(u) = $userID
            RETURN count(p)
            """)
    Integer getRangeCountByActivity(@Param("activityID") String activityID, @Param("userID") String userID,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("""
            MATCH (p:Post)
            RETURN p.image
            """)
    Set<String> findAllImages();

    @Query("""
            MATCH (u:User)-[:Posted]->(p:Post)-[:`Related-To`]->(a:Activity)
            WHERE elementId(u) = $userID AND elementId(a) = $activityID
            RETURN p.streak
            ORDER BY p.createdAt DESC
            """)
    Integer getStreakByActivity(@Param("activityID") String activityID, @Param("userID") String userID);

}
