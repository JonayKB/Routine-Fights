package es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.repositories;

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
            WHERE a.id = $activityID AND u.id = $userID
            RETURN a, u, r
            """)
    boolean userIsOnActivity(@Param("userID") String userID, @Param("activityID") String activityID);

    @NonNull
    Optional<ActivityEntity> findById(@NonNull String id);

}
