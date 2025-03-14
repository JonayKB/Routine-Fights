package es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    List<ActivityEntity> getPagination(int offset, int limit);

}
