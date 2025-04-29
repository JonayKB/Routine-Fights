package es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.secondary.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.secondary.entities.CommentEntity;

@Repository
public interface ICommentEntityRepository extends Neo4jRepository<CommentEntity, String> {
    List<CommentEntity> findByPostId(String postID);
}
