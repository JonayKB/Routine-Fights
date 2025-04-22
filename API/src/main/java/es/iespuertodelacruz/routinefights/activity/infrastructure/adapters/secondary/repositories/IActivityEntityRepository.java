package es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.entities.ActivityEntity;

@Service
public interface IActivityEntityRepository extends Neo4jRepository<ActivityEntity, String> {
        @Query("""
                        MATCH (a:Activity)<-[r:Participated]-(u:User)
                        WHERE elementId(a) = $activityID AND elementId(u) = $userID
                        RETURN COUNT(*) > 0
                        """)
        boolean userIsOnActivity(@Param("userID") String userID, @Param("activityID") String activityID);

        @NonNull
        Optional<ActivityEntity> findById(@NonNull String id);

        @Query("""
                        MATCH (a:Activity)<-[c:Created]-(u:User)
                        RETURN a,c,u
                        SKIP $offset LIMIT $limit
                        """)
        List<ActivityEntity> getPagination(@Param("offset") int offset, @Param("limit") int limit);

        @Query("""
                        MATCH (x:User)
                        WHERE elementId(x) = $userID
                        MATCH (a:Activity)<-[c:Created]-(u:User)
                        WHERE NOT (x)-[:Participated]->(a) AND lower(a.name) CONTAINS lower($activityName)
                        RETURN a,c,u
                        SKIP $offset LIMIT $limit
                        """)
        List<ActivityEntity> getPaginationNotSubscribed(@Param("offset") int offset, @Param("limit")int limit,@Param("userID") String userID, @Param("activityName") String activityName);

        @Query("""
                        MATCH (u:User)
                        WHERE elementId(u) = $userID
                        MATCH (cc:User)-[c:Created]->(a:Activity)
                        WHERE NOT EXISTS { (u)-[:Participated]->(a) }
                        RETURN a, cc, c
                        """)
        List<ActivityEntity> getSubscribedActivities(@Param("userID") String userID);

        @Query("""
                        MATCH (u:User)-[:Participated]->(a:Activity)<-[:`Related-To`]-(p:Post)
                        WHERE elementId(u)=$userID
                        SET a.streak=p.streak
                        RETURN a;
                        """)
        List<ActivityEntity> getSubscribedActivitiesWithStreak(@Param("userID") String userID);

        @Query("""
                        MATCH (u:User)-[:Participated]->(a:Activity)<-[:`Related-To`]-(p:Post)
                        WHERE elementId(u) = $userID
                        AND lower(a.name) CONTAINS lower($activityName)
                        SET a.streak = p.streak
                        RETURN DISTINCT a;

                        """)
        List<ActivityEntity> getSubscribedActivitiesWithStreak(@Param("userID") String userID,
                        @Param("activityName") String activityName);

}
