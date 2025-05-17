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
            OPTIONAL MATCH (p)-[r1:`Related-To`]->(a:Activity)
            OPTIONAL MATCH (p)<-[r2:`To_Report`]-(rep:Report)
            OPTIONAL MATCH (p)<-[r3:`Liked`]-(u2:User)
            OPTIONAL MATCH (p)<-[r4:`On`]-(c:Comment)
            OPTIONAL MATCH (u:User)-[pp:`Posted`]->(p)
            WHERE p.createdAt < $lastDate
            RETURN p,
                   collect(r1) AS relatedToRels,
                   collect(a) AS activities,
                   collect(r2) AS reportRels,
                   collect(rep) AS reports,
                   collect(r3) AS likeRels,
                   collect(u2) AS likedByUsers,
                   collect(r4) AS commentRels,
                   collect(c) AS comments,
                   collect(pp) AS postedRels,
                   u AS author
            ORDER BY p.createdAt DESC
            LIMIT $limit

                                    """)
    List<PostEntity> getPagination(@Param("lastDate") LocalDateTime lastDate, @Param("limit") int limit);

    @Query("""
            MATCH (u:User)-[pp:`Posted`]->(p:Post)
            OPTIONAL MATCH (p)-[r1:`Related-To`]->(a:Activity)
            OPTIONAL MATCH (p)<-[r2:`To_Report`]-(rep:Report)
            OPTIONAL MATCH (p)<-[r3:`Liked`]-(u2:User)
            OPTIONAL MATCH (p)<-[r4:`On`]-(c:Comment)
            WHERE elementId(u) = $userID
              AND p.createdAt < $lastDate
            RETURN p, collect(r1), collect(a), collect(r2), collect(rep), collect(r3), collect(u2), collect(r4), collect(c), u, pp
            ORDER BY p.createdAt DESC
            LIMIT $limit
            """)
    List<PostEntity> getPaginationByUser(@Param("lastDate") LocalDateTime lastDate, @Param("limit") int limit,
            @Param("userID") String userID);

    List<PostEntity> getPaginationByActivity(@Param("lastDate") LocalDateTime lastDate, @Param("limit") int limit,
            @Param("activityID") String activityID);

    @Query("""
            MATCH (u:User)-[:`Follows`]->(u2:User)-[pp:`Posted`]->(p:Post)
            OPTIONAL MATCH (p)-[r1:`Related-To`]->(a:Activity)
            OPTIONAL MATCH (p)<-[r2:`To_Report`]-(rep:Report)
            OPTIONAL MATCH (p)<-[r3:`Liked`]-(u3:User)
            OPTIONAL MATCH (p)<-[r4:`On`]-(c:Comment)
            WHERE elementId(u) = $userID
              AND p.createdAt < $lastDate
            RETURN p, collect(r1), collect(a), collect(r2), collect(rep), collect(r3), collect(u3), collect(r4), collect(c), u2, pp
            ORDER BY p.createdAt DESC
            LIMIT $limit
            """)
    List<PostEntity> getPaginationFollowing(@Param("lastDate") LocalDateTime lastDate, @Param("limit") int limit,
            @Param("userID") String userID);

    @Query("""
            MATCH (u:User)-[pp:`Participated`]->(a:Activity)<-[:`Related-To`]-(p:Post)<-[r:Posted]-(u2:User)
            OPTIONAL MATCH (p)-[r1:`Related-To`]->(a2:Activity)
            OPTIONAL MATCH (p)<-[r2:`To_Report`]-(rep:Report)
            OPTIONAL MATCH (p)<-[r3:`Liked`]-(u3:User)
            OPTIONAL MATCH (p)<-[r4:`On`]-(c:Comment)
            WHERE elementId(u) = $userID
              AND p.createdAt < $lastDate
            RETURN p, collect(r1), collect(a2), collect(r2), collect(rep), collect(r3), collect(u3), collect(r4), collect(c), u2, r
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
            MATCH (p:Post)
            RETURN p.image
            """)
    Set<String> findAllImages();

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
            MATCH (u:User)-[:Posted]->(p:Post)-[:`Related-To`]->(a:Activity)
            WHERE elementId(u) = $userID AND elementId(a) = $activityID
            RETURN p.streak
            ORDER BY p.createdAt DESC
            """)
    Integer getStreakByActivity(@Param("activityID") String activityID, @Param("userID") String userID);

    @Query("""
            MATCH (u:User)
            WHERE elementId(u) = $userID
            MATCH (a:Activity)
            WHERE elementId(a) = $activityID
            CREATE (p:Post {image: $image, pointsToAdd: $pointsToAdd, createdAt: $createdAt, streak: $streak})
            CREATE (u)-[:Posted]->(p)
            CREATE (p)-[:`Related-To`]->(a)
            RETURN p
            """)
    PostEntity create(@Param("image") String image, @Param("pointsToAdd") Integer pointsToAdd,
            @Param("createdAt") LocalDateTime createdAt, @Param("streak") Integer streak,
            @Param("activityID") String activityID, @Param("userID") String userID);

}
