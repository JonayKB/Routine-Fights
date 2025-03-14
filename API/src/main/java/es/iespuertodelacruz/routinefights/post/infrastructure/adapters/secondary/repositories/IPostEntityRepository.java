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

}
