package es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.secondary.repositories;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.secondary.entities.CommentEntity;

@Repository
public interface ICommentEntityRepository extends Neo4jRepository<CommentEntity, String> {
    @Query("MATCH (u:User)-[r:Commented]->(c:Comment)-[r2:On]->(p:Post) WHERE elementId(p) = $postID RETURN c,r2,p,u,r")
    List<CommentEntity> findByPostId(@Param("postID")String postID);
}
