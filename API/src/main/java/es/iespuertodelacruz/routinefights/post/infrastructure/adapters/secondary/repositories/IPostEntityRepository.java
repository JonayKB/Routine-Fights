package es.iespuertodelacruz.routinefights.post.infrastructure.adapters.secondary.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.iespuertodelacruz.routinefights.post.infrastructure.adapters.secondary.entities.PostEntity;

@Repository
public interface IPostEntityRepository extends Neo4jRepository<PostEntity, String> {
        @Query("""
                        MATCH (p:Post)<-[r:Posted]-(u:User)
                        WHERE p.createdAt < $lastDate
                        RETURN p, u, r
                        ORDER BY p.createdAt DESC
                        LIMIT $limit
                        """)
        List<PostEntity> getPagination(@Param("lastDate") LocalDateTime lastDate, @Param("limit") int limit);

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

}
